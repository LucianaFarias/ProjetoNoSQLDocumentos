package com.ProjetoDocumento.API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.ProjetoDocumento.ConexaoBD.ConexaoMongoDB;

public class APIMongoDB {

    private static final String API_KEY = "AIzaSyB39ayMiyxg4xoUHz4j8Aj2BkxfRlORPbk";  //chave da API
    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final int REQUEST_LIMIT = 10000; // Limite de requisições diárias
    private static int requestCount = 0;

    // Usando a conexão existente com o MongoDB
    private static final MongoCollection<Document> collection = ConexaoMongoDB.getDatabase().getCollection("livros");

    // Método para buscar livros e salvar no banco de dados
    public static void buscarLivros(String query) {
        try {
            if (requestCount >= REQUEST_LIMIT) {
                System.out.println("Limite de requisições alcançado. Tente novamente amanhã.");
                return;
            }

            int startIndex = 0;
            int maxResults = 40; // Número máximo de resultados por página
            boolean hasMoreResults = true;
            int totalSavedBooks = 0; // Contador total de livros salvos

            while (hasMoreResults && requestCount < REQUEST_LIMIT) {
                // Adicionar termos específicos e filtro de idioma à consulta
                String adjustedQuery = query + "+brasil";
                String encodedQuery = URLEncoder.encode(adjustedQuery, StandardCharsets.UTF_8.toString());
                String urlString = GOOGLE_BOOKS_API_URL + encodedQuery + "&langRestrict=pt&startIndex=" + startIndex + "&maxResults=" + maxResults + "&key=" + API_KEY;
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Verificar o código de resposta da solicitação
                int responseCode = connection.getResponseCode();
                requestCount++;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();
                    JsonArray items = jsonResponse.getAsJsonArray("items");
                    int totalItems = jsonResponse.get("totalItems").getAsInt();

                    if (items != null) {
                        for (JsonElement item : items) {
                            try {
                                JsonObject volumeInfo = item.getAsJsonObject().getAsJsonObject("volumeInfo");
                                String titulo = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Título desconhecido";
                                String autorNome = volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : "Autor desconhecido";
                                int anoPublicacao = volumeInfo.has("publishedDate") ? Integer.parseInt(volumeInfo.get("publishedDate").getAsString().split("-")[0]) : 0;
                                String categoria = volumeInfo.has("categories") ? volumeInfo.getAsJsonArray("categories").get(0).getAsString() : "Categoria desconhecida";

                                // MongoDB
                                Document livroDoc = new Document("titulo", titulo)
                                        .append("autor", autorNome)
                                        .append("anoPublicacao", anoPublicacao)
                                        .append("categoria", categoria);
                                collection.insertOne(livroDoc);
                                totalSavedBooks++;
                            } catch (Exception e) {
                                System.out.println("Erro ao processar livro: " + e.getMessage());
                            }
                        }
                    } else {
                        System.out.println("Nenhum livro encontrado.");
                    }

                    // Verificar se há mais resultados para buscar
                    startIndex += maxResults;
                    hasMoreResults = startIndex < totalItems;
                } else {
                    // Capturar e exibir a mensagem de erro detalhada
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    errorReader.close();
                    System.out.println("Erro na solicitação: " + responseCode);
                    System.out.println("Mensagem de erro: " + errorResponse.toString());
                    break;
                }

                // Calcular o tempo de processamento
                calcularTempoDeProcessamento();
            }

            // Exibir mensagem final com total de livros salvos
            System.out.println("Total de livros salvos no banco de dados: " + totalSavedBooks);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void calcularTempoDeProcessamento() {
        long startTime = System.currentTimeMillis();

        // Adicionar um intervalo entre as requisições
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Tempo de processamento: " + elapsedTime + " ms");
    }

    public static void main(String[] args) {
        buscarLivros("sua consulta aqui"); // Substitua "sua consulta aqui" pela consulta desejada

        // Adicionar mensagem final
        System.out.println("Busca de livros finalizada. Verifique o banco de dados MongoDB.");
    }
}

package com.ProjetoDocumento.API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.ProjetoDocumento.model.Autor;
import com.ProjetoDocumento.model.Livro;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class APIMySQL {
    private static final String API_KEY = "xxxxxxxxx";  // Substitua pela sua chave da API
    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final EntityManagerFactory entityManagerFactory;
    private static final int REQUEST_LIMIT = 10000; // Limite de requisições diárias
    private static int requestCount = 0;

    static {
        entityManagerFactory = Persistence.createEntityManagerFactory("livrosPU");
    }

    public void buscarLivros(String query) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

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
                        transaction.begin();
                        for (JsonElement item : items) {
                            try {
                                JsonObject volumeInfo = item.getAsJsonObject().getAsJsonObject("volumeInfo");
                                String titulo = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Título desconhecido";
                                String autorNome = volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : "Autor desconhecido";
                                int anoPublicacao = volumeInfo.has("publishedDate") ? Integer.parseInt(volumeInfo.get("publishedDate").getAsString().split("-")[0]) : 0;
                                String categoria = volumeInfo.has("categories") ? volumeInfo.getAsJsonArray("categories").get(0).getAsString() : "Categoria desconhecida";

                                // Criar instância de Autor
                                Autor autor = new Autor();
                                autor.setNome(autorNome);

                                // Criar instância de Livro
                                Livro livro = new Livro();
                                livro.setTitulo(titulo);
                                livro.setAutor(autor);
                                livro.setAnoPublicacao(anoPublicacao);
                                livro.setCategoria(categoria);

                                // Salvar no banco de dados usando JPA
                                entityManager.persist(livro);
                                totalSavedBooks++;
                            } catch (Exception e) {
                                System.out.println("Erro ao processar livro: " + e.getMessage());
                            }
                        }
                        transaction.commit();
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
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
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

}

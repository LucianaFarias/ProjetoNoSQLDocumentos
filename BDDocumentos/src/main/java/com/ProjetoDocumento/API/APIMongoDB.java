package com.ProjetoDocumento.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import org.bson.Document;

public class APIMongoDB {
  private static final String API_KEY = "xxxxxxxx";  //chave da API
  private static final int REQUEST_LIMIT = 10000; // Limite de requisições diárias
  private static int requestCount = 0;

    // Método principal para buscar livros
    public static List<Document> buscarLivros(String titulo) {
        List<Document> livros = new ArrayList<>();
        // 1. Construir a URL da API
        String urlString = buildApiUrl(titulo);
        // 2. Realizar a requisição e obter a resposta
        String response = makeApiRequest(urlString);
        livros=processarResposta(response);
        calcularTempoDeProcessamento();
        // 3. Extrair e processar os dados da resposta
        return livros;

    }

    private static String buildApiUrl(String titulo) {
        StringBuilder urlString = new StringBuilder(GOOGLE_BOOKS_API_URL);
        try {
            if (!titulo.isEmpty()) {
                urlString.append("intitle:").append(URLEncoder.encode(titulo, "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlString.toString();
    }

    private static String makeApiRequest(String urlString) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("Erro ao buscar livros: " + responseCode);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }
    private static List<Document> processarResposta(String response) {
        List<Document> livrosEncontrados = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
        JsonArray items = jsonResponse.has("items") ? jsonResponse.getAsJsonArray("items") : null;
        if (items != null) {
            for (JsonElement itemElement : items) {
                JsonObject volumeInfo = itemElement.getAsJsonObject().getAsJsonObject("volumeInfo");
                if (volumeInfo != null) {
                    String tituloLivro = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Título não disponível";
                    String autorLivro = volumeInfo.has("authors") && volumeInfo.getAsJsonArray("authors").size() > 0
                            ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : "Autor não disponível";
                    String anoPublicacao = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString() : "Data não disponível";
                    String categoria = volumeInfo.has("categories") && volumeInfo.getAsJsonArray("categories").size() > 0
                            ? volumeInfo.getAsJsonArray("categories").get(0).getAsString() : "Categoria não disponível";

                    // Cria um documento para o livro encontrado
                    Document livro = new Document()
                            .append("titulo", tituloLivro)
                            .append("autor", autorLivro)
                            .append("anoPublicacao", anoPublicacao)
                            .append("categoria", categoria);
                    livrosEncontrados.add(livro);
                }
            }
        }
        return livrosEncontrados;
    }

    private static void calcularTempoDeProcessamento() {
        long startTime = System.currentTimeMillis();

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
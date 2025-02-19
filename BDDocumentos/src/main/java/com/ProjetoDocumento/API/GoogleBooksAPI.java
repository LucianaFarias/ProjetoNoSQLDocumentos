package com.ProjetoDocumento.API;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.ProjetoDocumento.ConexaoBD.ConexaoMongoDB;
import com.ProjetoDocumento.ConexaoBD.ConexaoMySQL;

public class GoogleBooksAPI {

    private static final String API_KEY = "SUA_NOVA_CHAVE_DA_API";  // Substitua pela sua nova chave da API
    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    // Usando a conexão existente com o MongoDB
    private static final MongoCollection<Document> collection = ConexaoMongoDB.getDatabase().getCollection("livros");

    // Usando a conexão existente com o MySQL
    private static final Connection mysqlConnection = ConexaoMySQL.getConnection();

    // Método para buscar livros e salvar no banco de dados
    public static void buscarTodosLivros(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String url = GOOGLE_BOOKS_API_URL + encodedQuery + "&key=" + API_KEY;
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Adicionando verificação para imprimir a resposta da API
            System.out.println("Resposta da API: " + response.body());

            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray items = jsonResponse.getAsJsonArray("items");

            if (items != null) {
                for (JsonElement item : items) {
                    JsonObject volumeInfo = item.getAsJsonObject().getAsJsonObject("volumeInfo");
                    String titulo = volumeInfo.get("title").getAsString();
                    String autorNome = volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : "Autor desconhecido";
                    int anoPublicacao = volumeInfo.has("publishedDate") ? Integer.parseInt(volumeInfo.get("publishedDate").getAsString().split("-")[0]) : 0;
                    String categoria = volumeInfo.has("categories") ? volumeInfo.getAsJsonArray("categories").get(0).getAsString() : "Categoria desconhecida";

                    // MongoDB
                    Document livroDoc = new Document("titulo", titulo)
                            .append("autor", autorNome)
                            .append("anoPublicacao", anoPublicacao)
                            .append("categoria", categoria);
                    collection.insertOne(livroDoc);

                    System.out.println("Livro salvo no banco: " + titulo);

                    // MySQL
                    String sql = "INSERT INTO livros (titulo, autor, ano_publicacao, categoria) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement stmt = mysqlConnection.prepareStatement(sql)) {
                        stmt.setString(1, titulo);
                        stmt.setString(2, autorNome);
                        stmt.setInt(3, anoPublicacao);
                        stmt.setString(4, categoria);
                        stmt.executeUpdate();
                        System.out.println("Livro salvo no MySQL: " + titulo);
                    }
                }
            } else {
                System.out.println("Nenhum livro encontrado.");
            }
        } catch (IOException | InterruptedException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        buscarTodosLivros("Harry Potter"); // Exemplo de busca por título
    }
}
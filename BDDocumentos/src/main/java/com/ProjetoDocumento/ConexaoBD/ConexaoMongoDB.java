package com.ProjetoDocumento.ConexaoBD;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConexaoMongoDB {
    private static final String URI = "mongodb://localhost:27017"; // Altere conforme necessário
    private static final String DATABASE_NAME = "CriticasLiterarias"; // Nome do banco

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    static {
        conectar();
    }

    private static void conectar() {
        try {
            mongoClient = MongoClients.create(URI);
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Conectado ao MongoDB com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao MongoDB: " + e.getMessage());
        }
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    public static void fecharConexao() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexão com MongoDB encerrada.");
        }
    }
}
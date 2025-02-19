package com.ProjetoDocumento.ConexaoBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {

    public static Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/criticas_Literarias";
        String user = "root";
        String password = "12345";

        try (Connection connection = ConexaoMySQL.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("Conectado ao banco de dados MySQL com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
}
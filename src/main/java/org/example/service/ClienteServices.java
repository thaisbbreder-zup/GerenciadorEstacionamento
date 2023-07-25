package org.example.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.Main.connection;

public class ClienteServices {

    public static int insereClienteNoBanco(String nome, String cpf) {
        String sql = "INSERT INTO clientes (nome, cpf) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, cpf);
            preparedStatement.executeUpdate(); // Obtém a chave gerada (ID do cliente inserido)
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idClienteInserido = generatedKeys.getInt(1);
                System.out.println("\nCliente com id = " + idClienteInserido + " cadastrado no banco de dados com sucesso!");
                return idClienteInserido;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar cliente no banco de dados.");
        }
        return 0;
    }
}

package org.example.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.example.Main.connection;

public class CarroServices {
    public static int insereCarroNoBanco(int id_cliente, String nome_carro, String placa, LocalDate data_entrada, LocalTime hora_entrada) {
        String sql = "INSERT INTO carros (id_cliente, nome_carro, placa, data_entrada, hora_entrada) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, id_cliente);
            preparedStatement.setString(2, nome_carro);
            preparedStatement.setString(3, placa);
            preparedStatement.setDate(4, java.sql.Date.valueOf(data_entrada));
            preparedStatement.setTime(5, java.sql.Time.valueOf(hora_entrada));
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idCarroInserido = generatedKeys.getInt(1);
                System.out.println("\nCarro cadastrado no banco de dados com sucesso!\n");
                return idCarroInserido;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar carro no banco de dados.");
        }
        return 0;
    }
}

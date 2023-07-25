package org.example.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.example.Main.connection;

public class GerenciadorPermanencia {
    public static void inserePermanenciaNoBanco(int idCarro, LocalDate dataSaida, LocalTime horaSaida, double valorTotal) {
        String sql = "INSERT INTO permanencias (idcarro, data_saida, hora_saida, valor) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idCarro);
            preparedStatement.setDate(2, java.sql.Date.valueOf(dataSaida));
            preparedStatement.setTime(3, java.sql.Time.valueOf(horaSaida));
            preparedStatement.setDouble(4, valorTotal); // Definir o valor total calculado

            preparedStatement.executeUpdate();
            System.out.println("Histórico cadastrado no banco de dados com sucesso!\n");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar carro no banco de dados.");
        }
    }


    public static void atualizaDataHoraEntradaNoBanco(int idCliente, LocalDate dataSaida, LocalTime horaSaida) {
        String sql = "UPDATE carros SET data_saida = ?, hora_saida = ? WHERE id_cliente = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, java.sql.Date.valueOf(dataSaida));
            preparedStatement.setTime(2, java.sql.Time.valueOf(horaSaida));
            preparedStatement.setInt(3, idCliente);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data/Hora de entrada atualizada com sucesso para a permanência com ID " + idCliente);
            } else {
                System.out.println("Nenhum registro encontrado para a permanência com ID " + idCliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar a data/hora de saída.");
        }
    }

    public static void atualizaDataHoraSaidaNoBanco(int idCarroPermanencia, LocalDate dataSaida, LocalTime horaSaida) {
        String sql = "UPDATE permanencias SET data_saida = ?, hora_saida = ? WHERE idcarro = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, java.sql.Date.valueOf(dataSaida));
            preparedStatement.setTime(2, java.sql.Time.valueOf(horaSaida));
            preparedStatement.setInt(3, idCarroPermanencia);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data/Hora de saída atualizada com sucesso para ID " + idCarroPermanencia);
            } else {
                System.out.println("Nenhum registro encontrado para ID " + idCarroPermanencia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar a data/hora de saída.");
        }
    }

    public static void excluirCarro(int idCarro) {
        try {
            // Obter o ID do cliente associado ao carro
            String sqlObterIdCliente = "SELECT id_cliente FROM carros WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlObterIdCliente);
            preparedStatement.setInt(1, idCarro);
            ResultSet resultSet = preparedStatement.executeQuery();

            int idCliente = -1; // Valor inválido para indicar que não foi encontrado
            if (resultSet.next()) {
                idCliente = resultSet.getInt("id_cliente");
            }

            if (idCliente != -1) {
                // Excluir o registro da tabela de permanências associado ao carro
                String sqlExcluirPermanencia = "DELETE FROM permanencias WHERE idcarro = ?";
                preparedStatement = connection.prepareStatement(sqlExcluirPermanencia);
                preparedStatement.setInt(1, idCarro);
                preparedStatement.executeUpdate();

                // Excluir o registro da tabela de carros associado ao cliente
                String sqlExcluirCarro = "DELETE FROM carros WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlExcluirCarro);
                preparedStatement.setInt(1, idCarro);
                preparedStatement.executeUpdate();

                // Excluir o registro do cliente
                String sqlExcluirCliente = "DELETE FROM clientes WHERE id = ?";
                preparedStatement = connection.prepareStatement(sqlExcluirCliente);
                preparedStatement.setInt(1, idCliente);
                preparedStatement.executeUpdate();

                System.out.println("Carro com ID " + idCarro + " e cliente com ID " + idCliente + " excluídos do banco de dados.");
            } else {
                System.out.println("Não foi possível encontrar o cliente associado ao carro com ID " + idCarro + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao excluir registros do carro e cliente.");
        }
    }
}

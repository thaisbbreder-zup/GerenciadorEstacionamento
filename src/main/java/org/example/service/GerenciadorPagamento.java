package org.example.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.example.Main.connection;

public class GerenciadorPagamento {
    public static void visualizarInformacoes() {
        String sql = "SELECT c.id as cliente_id, c.nome as cliente_nome, c.cpf, " +
                "ca.id as carro_id, ca.nome_carro, ca.placa, " +
                "p.id as permanencia_id, p.data_saida, p.hora_saida " +
                "FROM clientes c " +
                "LEFT JOIN carros ca ON c.id = ca.id_cliente " +
                "LEFT JOIN permanencias p ON ca.id = p.idcarro";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int clienteId = resultSet.getInt("cliente_id");
                String clienteNome = resultSet.getString("cliente_nome");
                String cpf = resultSet.getString("cpf");
                int carroId = resultSet.getInt("carro_id");
                String nomeCarro = resultSet.getString("nome_carro");
                String placa = resultSet.getString("placa");
                int permanenciaId = resultSet.getInt("permanencia_id");
                LocalDate dataSaida = resultSet.getDate("data_saida").toLocalDate();
                LocalTime horaSaida = resultSet.getTime("hora_saida").toLocalTime();

                System.out.println("Cliente ID: " + clienteId + "\n Nome: " + clienteNome + "\n CPF: " + cpf);
                System.out.println("Carro ID: " + carroId + "\n Nome do Carro: " + nomeCarro + "\n Placa: " + placa);
                System.out.println("Permanência ID: " + permanenciaId + "\n Data de Saída: " + dataSaida + "\n Hora de Saída: " + horaSaida);
                System.out.println("---------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao obter as informações das tabelas.");
        }
    }

    public static double calcularValorPermanencia(LocalDate dataEntrada, LocalTime horaEntrada, LocalDate dataSaida, LocalTime horaSaida) {
        // Calcula a diferença de tempo entre a entrada e a saída
        LocalDateTime entrada = LocalDateTime.of(dataEntrada, horaEntrada);
        LocalDateTime saida = LocalDateTime.of(dataSaida, horaSaida);
        Duration duracaoPermanencia = Duration.between(entrada, saida);
        double minutosPermanencia = duracaoPermanencia.toMinutes();

        double valorHora = 10.0; // Valor da primeira hora
        double valorAdicional30Min = 2.0; // Valor adicional para cada 30 minutos
        double valorMeioPeriodo = 90.0; // Valor para meio período (12 horas)
        double valorTotal = 0.0;

        if (minutosPermanencia <= 70) { // Até 1 hora
            valorTotal = valorHora;
        } else if (minutosPermanencia <= 120) { // Até 1 hora e 30 minutos
            valorTotal = valorHora + valorAdicional30Min;
        } else { // Mais de 1 hora e 30 minutos (meio período)
            double periodosDe12h = Math.ceil((minutosPermanencia - 90) / (12 * 60));
            valorTotal = valorMeioPeriodo * periodosDe12h;
        }
        return valorTotal;
    }

    public static double calcularValorPagamento(int idCarro) {
        String sql = "SELECT c.data_entrada, c.hora_entrada, p.data_saida, p.hora_saida " +
                "FROM carros c " +
                "LEFT JOIN permanencias p ON c.id = p.idcarro " +
                "WHERE c.id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idCarro);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                LocalDate dataEntrada = resultSet.getDate("data_entrada").toLocalDate();
                LocalTime horaEntrada = resultSet.getTime("hora_entrada").toLocalTime();
                LocalDate dataSaida = resultSet.getDate("data_saida").toLocalDate();
                LocalTime horaSaida = resultSet.getTime("hora_saida").toLocalTime();

                double valorTotal = calcularValorPermanencia(dataEntrada, horaEntrada, dataSaida, horaSaida);

                return valorTotal;
            } else {
                System.out.println("Nenhum registro encontrado para o carro com ID " + idCarro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao obter informações do carro do banco de dados.");
        }

        return 0.0;
    }

}

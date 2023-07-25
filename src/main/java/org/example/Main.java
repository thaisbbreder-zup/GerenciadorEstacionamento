package org.example;

import org.example.connection.Conexao;
import org.example.service.CarroServices;
import org.example.service.ClienteServices;
import org.example.service.GerenciadorPagamento;
import org.example.service.GerenciadorPermanencia;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static org.example.service.GerenciadorPagamento.calcularValorPermanencia;
import static org.example.service.GerenciadorPermanencia.inserePermanenciaNoBanco;


public class Main {
    public static Connection connection;
    private static Statement statement;
    private static LocalDate dataEntrada;
    private static LocalTime horaEntrada;

    public static void main(String[] args) {
        connection = Conexao.getConnection();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Scanner entradaDoUsuario = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n================ MENU PRINCIPAL ================");
            System.out.println(" 1\uFE0F⃣  CADASTRAR NOVO CARRO");
            System.out.println(" 2\uFE0F⃣  VISUALIZAR HISTÓRICO DE PERMANENCIA");
            System.out.println(" 3\uFE0F⃣  ATUALIZAR DATA/HORA DE SAÍDA");
            System.out.println(" 4\uFE0F⃣  ATUALIZAR DATA/HORA DE ENTRADA");
            System.out.println(" 5\uFE0F⃣  PAGAMENTO");
            System.out.println(" 6\uFE0F⃣  SAIR");

            opcao = entradaDoUsuario.nextInt();
            entradaDoUsuario.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("Você selecionou: - CADASTRAR NOVO CARRO");
                    System.out.println("---------------------------------");
                    System.out.println("Insira os dados do cliente ");
                    System.out.println("Nome do cliente: ");
                    String nome = entradaDoUsuario.nextLine();

                    System.out.println("CPF: ");
                    String cpf = entradaDoUsuario.nextLine();

                    int idClienteInserido = ClienteServices.insereClienteNoBanco(nome, cpf);

                    System.out.println("\nInsira os dados do carro ");
                    System.out.println("Modelo do carro: ");
                    String nome_carro = entradaDoUsuario.nextLine();

                    System.out.println("Placa: ");
                    String placa = entradaDoUsuario.nextLine();

                    System.out.println("Data de entrada (AAAA-MM-DD): ");
                    String dataEntradaString = entradaDoUsuario.nextLine();
                    LocalDate data_entrada = LocalDate.parse(dataEntradaString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    System.out.println("Hora de entrada (HH:mm:ss): ");
                    String horaEntradaString = entradaDoUsuario.nextLine();
                    LocalTime hora_entrada = LocalTime.parse(horaEntradaString, DateTimeFormatter.ofPattern("HH:mm:ss"));

                    int idCarroInserido = CarroServices.insereCarroNoBanco(idClienteInserido, nome_carro, placa, data_entrada, hora_entrada);

                    System.out.println("Previsão data de saída (AAAA-MM-DD): ");
                    String dataSaidaString = entradaDoUsuario.nextLine();
                    LocalDate data_saida = LocalDate.parse(dataSaidaString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    System.out.println("Previsão da hora de saída (HH:mm:ss): ");
                    String horaSaidaString = entradaDoUsuario.nextLine();
                    LocalTime hora_saida = LocalTime.parse(horaSaidaString, DateTimeFormatter.ofPattern("HH:mm:ss"));

                    double valorTotal = calcularValorPermanencia(data_entrada, hora_entrada, data_saida, hora_saida);
                    inserePermanenciaNoBanco(idCarroInserido, data_saida, hora_saida, valorTotal);
                    System.out.println("Valor a ser pago: R$" + valorTotal);
                    break;
                case 2:
                    System.out.println("Você selecionou: - VISUALIZAR INFORMAÇÕES DAS TABELAS");
                    System.out.println("---------------------------------");
                    GerenciadorPagamento.visualizarInformacoes();
                    break;
                case 3:
                    System.out.println("Você selecionou: - ATUALIZAR DATA/HORA DE SAÍDA");
                    System.out.println("---------------------------------");

                    System.out.println("ID do carro: ");
                    int idCarroPermanencia = entradaDoUsuario.nextInt();
                    entradaDoUsuario.nextLine();

                    System.out.println("Nova data de saída (AAAA-MM-DD): ");
                    String dataSaidaStr = entradaDoUsuario.nextLine();
                    LocalDate novaDataSaida = LocalDate.parse(dataSaidaStr);

                    System.out.println("Nova hora de saída (HH:mm:ss): ");
                    String horaSaidaStr = entradaDoUsuario.nextLine();
                    LocalTime novaHoraSaida = LocalTime.parse(horaSaidaStr);

                    GerenciadorPermanencia.atualizaDataHoraSaidaNoBanco(idCarroPermanencia, novaDataSaida, novaHoraSaida);
                    break;
                case 4:
                    System.out.println("Você selecionou: - ATUALIZAR DATA/HORA DE ENTRADA");
                    System.out.println("---------------------------------");

                    System.out.println("ID do cliente: ");
                    int idCliente = entradaDoUsuario.nextInt();
                    entradaDoUsuario.nextLine();

                    System.out.println("Nova data de entrada (AAAA-MM-DD): ");
                    String dataEntradaStr = entradaDoUsuario.nextLine();
                    LocalDate novaDataEntrada = LocalDate.parse(dataEntradaStr);

                    System.out.println("Nova hora de entrada (HH:mm:ss): ");
                    String horaEntradaStr = entradaDoUsuario.nextLine();
                    LocalTime novaHoraEntrada = LocalTime.parse(horaEntradaStr);
                    GerenciadorPermanencia.atualizaDataHoraEntradaNoBanco(idCliente, novaDataEntrada, novaHoraEntrada);
                    break;
                case 5:
                    System.out.println("Você selecionou: - PAGAMENTO");
                    System.out.println("---------------------------------");

                    System.out.println("Digite o ID do carro para calcular o pagamento:");
                    int idCarroPagamento = entradaDoUsuario.nextInt();
                    entradaDoUsuario.nextLine();

                    double valorTotalPagamento = GerenciadorPagamento.calcularValorPagamento(idCarroPagamento);
                    System.out.println("Valor a ser pago: R$" + valorTotalPagamento);

                    System.out.println("O cliente realizou o pagamento? (S/N)");
                    String resposta = entradaDoUsuario.nextLine();

                    if (resposta.equalsIgnoreCase("S")) {
                        GerenciadorPermanencia.excluirCarro(idCarroPagamento);
                    } else {
                        System.out.println("O carro não será excluído.");
                    }
                    break;
                case 6:
                    System.out.println("Você selecionou: - SAIR");
                    System.out.println("---------------------------------");
                    System.out.println("OK, saindo do sistema...");
                    break;
                default:
            }
        } while (opcao != 6);
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao fechar a conexão com o banco de dados.");
        }
    }
}
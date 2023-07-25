package org.example.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexao {

    public static Connection getConnection() {
        try {
            Properties propriedades = carregarPropriedades();
            String url = propriedades.getProperty("dburl");
            Connection conexao = DriverManager.getConnection(url, propriedades);
            System.out.println("Conexão realizada com sucesso!");
            return conexao;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Properties carregarPropriedades() {
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/db.properties")) {
            Properties propriedades = new Properties();
            propriedades.load(inputStream);
            return propriedades;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
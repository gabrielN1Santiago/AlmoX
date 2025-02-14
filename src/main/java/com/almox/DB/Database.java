package com.almox.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Vector;

import javax.swing.JOptionPane;

public class Database {

    public static Vector<Vector<Object>> getProductsFromDatabase() {
        Vector<Vector<Object>> dados = new Vector<>();

        String url = "jdbc:mysql://localhost:3306/estoque";
        String user = "root";
        String password = "root";

        String query = "SELECT nome, categoria, quantidade FROM produtos";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("nome"));
                row.add(rs.getString("categoria"));
                row.add(rs.getInt("quantidade"));
                dados.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dados;
    }


    public static void updateQuantity(String nomeItem, int novaQuantidade) {

        String url = "jdbc:mysql://localhost:3306/estoque";
        String usuario = "root";
        String senha = "root";

        String sql = "UPDATE produtos SET quantidade = ? WHERE nome = ?";

        try (Connection conn = DriverManager.getConnection(url, usuario, senha);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, novaQuantidade);
            stmt.setString(2, nomeItem);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Quantidade atualizada no banco de dados.");
            } else {
                System.out.println("Erro ao atualizar quantidade. Item não encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void insertRecord(String nomeItem, String categoria, int quantidade) {
        String url = "jdbc:mysql://localhost:3306/estoque";
        String usuario = "root";
        String senha = "root";

        String verificaSql = "SELECT quantidade FROM produtos WHERE nome = ? AND categoria = ?";
        String insertSql = "INSERT INTO produtos (nome, categoria, quantidade) VALUES (?, ?, ?)";
        String updateSql = "UPDATE produtos SET quantidade = quantidade + ? WHERE nome = ? AND categoria = ?";

        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {

            PreparedStatement stmtVerifica = conn.prepareStatement(verificaSql);
            stmtVerifica.setString(1, nomeItem);
            stmtVerifica.setString(2, categoria);
            ResultSet rs = stmtVerifica.executeQuery();

            if (rs.next()) {

                int quantidadeExistente = rs.getInt("quantidade");
                PreparedStatement stmtUpdate = conn.prepareStatement(updateSql);
                stmtUpdate.setInt(1, quantidade);
                stmtUpdate.setString(2, nomeItem);
                stmtUpdate.setString(3, categoria);
                stmtUpdate.executeUpdate();
                System.out.println("Quantidade atualizada. Novo estoque: " + (quantidadeExistente + quantidade));
            } else {

                PreparedStatement stmtInsert = conn.prepareStatement(insertSql);
                stmtInsert.setString(1, nomeItem);
                stmtInsert.setString(2, categoria);
                stmtInsert.setInt(3, quantidade);
                stmtInsert.executeUpdate();
                System.out.println("Novo item inserido no banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao inserir item no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

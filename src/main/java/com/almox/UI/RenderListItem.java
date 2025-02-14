package com.almox.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.almox.DB.Database;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Vector;

public class RenderListItem {

    public static Color corTema = new Color(128, 125, 107);

    public static JPanel renderItems(Vector<Vector<Object>> data) {

        // Painel principal onde a tabela será colocada
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(corTema); // Cor de fundo do painel

        // Obtém os dados da tabela de produtos no banco de dados
        Vector<Vector<Object>> dados = data;
        String[] colunas = {"Nome", "Categoria", "Quantidade"};

        // Convert colunas (String[]) into a Vector<String>
        Vector<String> colunasVector = new Vector<>(Arrays.asList(colunas));

        // Criação do modelo de tabela com os dados obtidos do banco de dados
        DefaultTableModel modeloTabela = new DefaultTableModel(dados, colunasVector);


        // Criação da JTable com o modelo de tabela
        JTable tabela = new JTable(modeloTabela) {
            // Desabilita a edição de células
            public boolean isCellEditable(int row, int column) {
                return false;  // Impede a edição das células
            }
        };

        tabela.setBackground(corTema); // Cor de fundo da tabela
        tabela.setForeground(Color.WHITE);  // Cor do texto da tabela

        // Configurações da aparência da tabela
        tabela.setShowGrid(true);
        tabela.setGridColor(Color.WHITE);  // Cor das linhas da grade da tabela

        // Colocando a tabela dentro de um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBackground(corTema); // Cor de fundo do JScrollPane
        scrollPane.getViewport().setBackground(corTema); // Aplique a cor de fundo no viewport

        // Campo de texto para inserir a quantidade a ser retirada
        JTextField campoQuantidade = new JTextField(5);
        campoQuantidade.setBackground(Color.WHITE);
        campoQuantidade.setForeground(Color.BLACK);

        // Label para o campo de quantidade
        JLabel labelQuantidade = new JLabel("Quantidade a retirar:");
        labelQuantidade.setForeground(Color.WHITE);

        // Painel para a entrada da quantidade
        JPanel painelQuantidade = new JPanel();
        painelQuantidade.setBackground(corTema);
        painelQuantidade.add(labelQuantidade);
        painelQuantidade.add(campoQuantidade);

        // Adicionando o painel de quantidade ao painel principal
        painel.add(painelQuantidade, BorderLayout.NORTH);

        // Adicionando o botão para remover o item
        JButton botaoRemover = new JButton("Remover Item");
        botaoRemover.setBackground(Color.RED); // Cor de fundo do botão
        botaoRemover.setForeground(Color.WHITE); // Cor do texto do botão

        // Ação do botão de remover
        botaoRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    // Verifica a quantidade a ser retirada
                    String quantidadeStr = campoQuantidade.getText();
                    try {
                        int quantidade = Integer.parseInt(quantidadeStr);

                        // Obtém os dados do item selecionado
                        String nomeItem = (String) tabela.getValueAt(linhaSelecionada, 0);
                        int quantidadeAtual = (int) tabela.getValueAt(linhaSelecionada, 2);

                        // Verifica se a quantidade a ser retirada é válida
                        if (quantidade <= 0 || quantidade > quantidadeAtual) {
                            JOptionPane.showMessageDialog(painel, "Quantidade inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Atualiza o banco de dados
                        Database.updateQuantity(nomeItem, quantidadeAtual);

                        // Atualiza a tabela
                        //modeloTabela.setValueAt(String.valueOf(quantidadeAtual - quantidade), linhaSelecionada, 2);
                        //data = Banco.getProdutosFromDatabase();
                        RenderAddItem.atualizarPainelListagem(painel);

                        JOptionPane.showMessageDialog(painel, "Item removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(painel, "Por favor, insira um número válido para a quantidade.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(painel, "Nenhum item selecionado!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Adiciona o botão abaixo da tabela
        painel.add(botaoRemover, BorderLayout.SOUTH);

        painel.add(scrollPane, BorderLayout.CENTER);  // Adiciona o JScrollPane ao painel

        return painel;
    }

}




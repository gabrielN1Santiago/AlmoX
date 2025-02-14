package com.almox.UI;

import javax.swing.*;

import com.almox.DB.Database;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import java.util.stream.IntStream;

public class RenderAddItem {

    public static Color corTema = new Color(128, 125, 107);

    private static JPanel painel = new JPanel();

    private static JTextField campoNome = new JTextField("Digite nome do item:");

    private static JComboBox<Integer> comboBoxQuantidade = new JComboBox<>(IntStream.rangeClosed(1, 500).boxed().toArray(Integer[]::new));

    private static JRadioButton[] options = {
        new JRadioButton("Materiais de Escritório", true),
        new JRadioButton("Equipamentos de Informática"),
        new JRadioButton("Ferramentas e Equipamentos"),
        new JRadioButton("Produtos de Limpeza"),
        new JRadioButton("EPI (Equipamento de Proteção Individual)"),
        new JRadioButton("Material de Construção"),
        new JRadioButton("Produtos de Manutenção"),
        new JRadioButton("Móveis e Utensílios"),
        new JRadioButton("Ferramentas e Equipamentos"),
        new JRadioButton("Produtos de Segurança")
    };

    public static JPanel renderAddItem(Vector<Vector<Object>> dados, JPanel painelListItem){
        painel.setBackground(corTema);
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.add(Box.createVerticalStrut(70));

        JLabel titulopag = new JLabel("Adicionar item");
        titulopag.setFont(new Font("Arial", Font.PLAIN, 24));
        titulopag.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(titulopag);

        painel.add(Box.createVerticalStrut(30));

        addLabelNome();

        addNomeInput();

        painel.add(Box.createVerticalStrut(30));//Espaço de 30px

        // adicionando lavel do input de opções
        addLabelRadio();

        //adicionando input de opções
        addRadioOptions();

        painel.add(Box.createVerticalStrut(30));

        // Adicionando o campo numérico (JComboBox) ao painel
        addLabelQuantidade();
        comboBoxQuantidade.setMaximumSize(new Dimension(300, 40)); // Tamanho máximo do JComboBox
        painel.add(comboBoxQuantidade);

        painel.add(Box.createVerticalStrut(30));

        addButtonSubmit(painelListItem);

        return painel;
    }

    public static void addLabelNome(){
        JLabel labelNome = new JLabel("NOME");
        labelNome.setFont(new Font("Arial", Font.PLAIN, 24));
        labelNome.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelNome);
    }

    public static void addNomeInput(){
        campoNome.setForeground(Color.GRAY);
        campoNome.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campoNome.getText().equals("Digite nome do item:")) {
                    campoNome.setText("");
                    campoNome.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campoNome.getText().isEmpty()) {
                    campoNome.setText("Digite nome do item:");
                    campoNome.setForeground(Color.GRAY);
                }
            }
        });
        campoNome.setMaximumSize(new Dimension(300, 40));
        painel.add(campoNome);
    }

    public static void addLabelRadio(){
        JLabel labelOpcoes = new JLabel("Escolha a categoria:");
        labelOpcoes.setFont(new Font("Arial", Font.PLAIN, 24));
        labelOpcoes.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelOpcoes);
    }

    public static void addRadioOptions() {
        ButtonGroup group = new ButtonGroup();

        for (JRadioButton option : getOptions()) {
            group.add(option);
        }

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS)); // Layout vertical para os radio buttons
        radioPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Alinha o painel no centro
        radioPanel.setBackground(Color.LIGHT_GRAY);

        for (JRadioButton option : getOptions()) {
            radioPanel.add(option);
        }

        painel.add(radioPanel);
    }

    public static void addLabelQuantidade(){
        JLabel labelQuantidade = new JLabel("Escolha a quantidade:");
        labelQuantidade.setFont(new Font("Arial", Font.PLAIN, 24));
        labelQuantidade.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(labelQuantidade);
    }

    public static void addButtonSubmit(JPanel painelListItem){
        JButton submitButoon = new JButton("Adicionar");
        submitButoon.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButoon.setMaximumSize(new Dimension(150, 40));
        painel.add(submitButoon);

        submitButoon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeItem = campoNome.getText();
                Integer quantidade = (Integer) comboBoxQuantidade.getSelectedItem();

                if (nomeItem.isEmpty() || nomeItem.equals("Digite nome do item:")) {
                    JOptionPane.showMessageDialog(painel, "Por favor, insira o nome do item.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JRadioButton[] radioButtons = getOptions();
                String categoriaSelecionada = "";
                for (JRadioButton option : radioButtons) {
                    if (option.isSelected()) {
                        categoriaSelecionada = option.getText();
                    }
                }

                if (categoriaSelecionada.isEmpty()) {
                    JOptionPane.showMessageDialog(painel, "Por favor, selecione uma categoria.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (quantidade == null || quantidade <= 0) {
                    JOptionPane.showMessageDialog(painel, "Por favor, escolha uma quantidade válida.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Inserir item no banco de dados
                Database.insertRecord(nomeItem, categoriaSelecionada, 0);

                // Atualizar o painel de listagem após inserir o item
                atualizarPainelListagem(painelListItem);

                // Exibir mensagem de sucesso
                JOptionPane.showMessageDialog(painel, "Item adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static void atualizarPainelListagem(JPanel painelListItem) {
        // Atualize o painel de listagem com os novos dados
        Vector<Vector<Object>> novosDados = Database.getProductsFromDatabase();
        painelListItem.removeAll();  // Remove os componentes antigos
    
        // Recrie os componentes da listagem com os novos dados
        painelListItem.add(RenderListItem.renderItems(novosDados));
    
        // Atualize o layout do painel para exibir as mudanças
        painelListItem.revalidate();
        painelListItem.repaint();
    }

    public static JRadioButton[] getOptions() {
        return options;
    }
}
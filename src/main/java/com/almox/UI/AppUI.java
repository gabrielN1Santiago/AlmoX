package com.almox.UI;

import java.util.Vector;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.almox.DB.Database;

public class AppUI {
    


    public static Color corTema = new Color(128, 125, 107);

    public static void appUI() {

        UIManager.put("RadioButton.background", Color.LIGHT_GRAY);

        JFrame frame = new JFrame("Almoxarifado");
        frame.setLayout(new BorderLayout());

        // Título principal
        JLabel tituloPrincipal = new JLabel("Almoxarifado", SwingConstants.CENTER);
        tituloPrincipal.setFont(new Font("Arial", Font.BOLD, 24));
        tituloPrincipal.setForeground(Color.LIGHT_GRAY);
        tituloPrincipal.setOpaque(true);
        tituloPrincipal.setBackground(corTema);
        frame.add(tituloPrincipal, BorderLayout.NORTH);

        // Painel com CardLayout
        JPanel painelPrincipal = new JPanel(new CardLayout());

        
        Vector<Vector<Object>> dados = Database.getProductsFromDatabase();


        JPanel painelListItem = RenderListItem.renderItems(dados);
        JPanel painelAddItem = RenderAddItem.renderAddItem(dados, painelListItem);


        painelPrincipal.add(painelAddItem, "Adicionar Item");
        painelPrincipal.add(painelListItem, "Listar Itens");


        JPanel painelBotoes = new JPanel();
        painelBotoes.setBackground(corTema);
        JButton btnAddItem = new JButton("Adicionar Item");
        JButton btnListItem = new JButton("Listar Itens");


        btnAddItem.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) painelPrincipal.getLayout();
            cardLayout.show(painelPrincipal, "Adicionar Item");
        });


        btnListItem.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) painelPrincipal.getLayout();
            cardLayout.show(painelPrincipal, "Listar Itens");
        });


        painelBotoes.add(btnAddItem);
        painelBotoes.add(btnListItem);


        frame.add(painelBotoes, BorderLayout.SOUTH);
        frame.add(painelPrincipal, BorderLayout.CENTER);

        frame.setSize(1200, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

}

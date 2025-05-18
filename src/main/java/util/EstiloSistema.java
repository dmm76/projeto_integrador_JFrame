package util;

import javax.swing.*;
import java.awt.*;

public class EstiloSistema {

    // Paleta de Cores
    public static final Color COR_FUNDO = Color.decode("#DCEFFF");
    public static final Color COR_TEXTO = Color.decode("#1E3A5F");
    public static final Color COR_CAMPO = Color.WHITE;
    public static final Color COR_BOTAO = Color.decode("#3A72B8");
    public static final Color COR_MENU = Color.decode("#2C3E50");
    public static final Color COR_TEXTO_MENU = Color.WHITE;

    // Fontes
    public static final Font FONTE_LABEL = new Font("Arial", Font.BOLD, 14);
    public static final Font FONTE_BOTAO = new Font("Arial", Font.BOLD, 16);
    public static final Font FONTE_MENU = new Font("Arial", Font.BOLD, 14);
    public static final Font FONTE_ITEM_MENU = new Font("Arial", Font.PLAIN, 13);

    // Aplicar estilo a botões
    public static void aplicarEstiloBotao(JButton botao) {
        botao.setFont(FONTE_BOTAO);
        botao.setForeground(Color.WHITE);
        botao.setBackground(COR_BOTAO);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    // Aplicar estilo a labels
    public static void aplicarEstiloLabel(JLabel label) {
        label.setForeground(COR_TEXTO);
        label.setFont(FONTE_LABEL);
    }

    // Aplicar estilo a campos de texto
    public static void aplicarEstiloCampo(JTextField campo) {
        campo.setBackground(COR_CAMPO);
        campo.setForeground(Color.BLACK);
        campo.setFont(new Font("Arial", Font.PLAIN, 13));
    }

    // Aplicar estilo a menus
    public static void aplicarEstiloMenu(JMenu menu) {
        menu.setForeground(COR_TEXTO_MENU);
        menu.setFont(FONTE_MENU);
    }

    // Aplicar estilo a itens de menu
    public static void aplicarEstiloMenuItem(JMenuItem item) {
        item.setOpaque(true);
        item.setBackground(COR_MENU);
        item.setForeground(COR_TEXTO_MENU);
        item.setFont(FONTE_ITEM_MENU);
    }

    //adicionar uma borda leve no botao entrar do menu login
    public static void destacarBotaoDefault(JButton botao) {
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
    }
    public static void estilizarBotaoMaterialDesign(JButton botao) {
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setOpaque(true);
        botao.setBackground(COR_BOTAO);
        botao.setForeground(Color.WHITE);
        botao.setFont(FONTE_BOTAO);

        // Borda padrão
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BOTAO.darker(), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Efeito de hover (apenas muda cor, não mexe na borda)
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!botao.isFocusOwner()) {
                    botao.setBackground(COR_BOTAO.darker());
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!botao.isFocusOwner()) {
                    botao.setBackground(COR_BOTAO);
                }
            }
        });

        // Efeito de foco (destaca borda e mantém cor original)
        botao.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                botao.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 3),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                botao.setBackground(COR_BOTAO.darker()); // ⚠️ agora escurece como o hover
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                botao.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COR_BOTAO.darker(), 2),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                botao.setBackground(COR_BOTAO);
            }
        });
    }

}

package util;

import javax.swing.*;

public class InputUtil {

    public static String solicitarTexto(String mensagem) {
        String input = JOptionPane.showInputDialog(null, mensagem);
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return null;
        }
        return input.trim();
    }

    public static Integer solicitarInteiro(String mensagem) {
        String input = JOptionPane.showInputDialog(null, mensagem);
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return null;
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor inválido. Informe um número inteiro.");
            return null;
        }
    }

    public static Double solicitarDouble(String mensagem) {
        String input = JOptionPane.showInputDialog(null, mensagem);
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return null;
        }
        try {
            return Double.parseDouble(input.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor inválido. Informe um número decimal.");
            return null;
        }
    }

    /**
     * Solicita um ID inteiro com uma mensagem extra (como uma lista de opções).
     *
     * @param mensagemLista A lista ou título exibido antes do input
     * @param mensagemPrompt A mensagem abaixo da lista solicitando o ID
     * @return o ID digitado, ou null se cancelado ou inválido
     */
    public static Integer solicitarIdComMensagem(String mensagemLista, String mensagemPrompt) {
        String input = JOptionPane.showInputDialog(null, mensagemLista + "\n" + mensagemPrompt);
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return null;
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido. Digite um número inteiro.");
            return null;
        }
    }
}
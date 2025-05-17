package view;

import dao.UsuarioSistemaDao;
import model.UsuarioSistema;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;

    public LoginForm() {
        setTitle("Login - Sistema de Bar e Restaurante");
        setSize(350, 230);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Definição de cores
        Color fundoCor = new Color(200, 220, 240); // Cinza azulado
        Color textoCor = new Color(30, 50, 80); // Azul escuro
        Color campoFundo = Color.WHITE;
        Color botaoCor = new Color(100, 150, 200); // Azul médio

        // Fonte personalizada
        Font fonteLabel = new Font("Arial", Font.BOLD, 14);
        Font fonteBotao = new Font("Arial", Font.BOLD, 16);

        // Painel principal estilizado
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(fundoCor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes estilizados
        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setForeground(textoCor);
        lblLogin.setFont(fonteLabel);

        txtLogin = new JTextField(20);
        txtLogin.setBackground(campoFundo);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setForeground(textoCor);
        lblSenha.setFont(fonteLabel);

        txtSenha = new JPasswordField(20);
        txtSenha.setBackground(campoFundo);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(fonteBotao);
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setBackground(botaoCor);
        btnEntrar.setIcon(new ImageIcon("icons/login.png")); // Ícone de login

        // Adicionando componentes ao painel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblLogin, gbc);
        gbc.gridx = 1;
        panel.add(txtLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblSenha, gbc);
        gbc.gridx = 1;
        panel.add(txtSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(btnEntrar, gbc);

        add(panel);

        btnEntrar.addActionListener(e -> autenticar());
    }

    private void autenticar() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioSistemaDao dao = new UsuarioSistemaDao(em);

        try {
            UsuarioSistema usuario = dao.autenticar(login, senha);
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNomeUsuario() + "!");
                new MainFrame(usuario);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
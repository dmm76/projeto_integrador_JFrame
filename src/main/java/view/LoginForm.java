package view;

import static util.EstiloSistema.*;
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
        setSize(380, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Painel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logo
        ImageIcon originalIcon = new ImageIcon("src/main/java/util/images/BR_Sistema_LOGO2.png");
        Image imagemReduzida = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(imagemReduzida));
        logo.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(logo, gbc);

        // Campo Login
        JLabel lblLogin = new JLabel("Login:");
        aplicarEstiloLabel(lblLogin);
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(lblLogin, gbc);

        txtLogin = new JTextField(20);
        aplicarEstiloCampo(txtLogin);
        gbc.gridx = 1;
        panel.add(txtLogin, gbc);

        // Campo Senha
        JLabel lblSenha = new JLabel("Senha:");
        aplicarEstiloLabel(lblSenha);
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(lblSenha, gbc);

        txtSenha = new JPasswordField(20);
        aplicarEstiloCampo(txtSenha);
        gbc.gridx = 1;
        panel.add(txtSenha, gbc);

        // Botão Entrar
        btnEntrar = new JButton("Entrar");
        aplicarEstiloBotao(btnEntrar);
        btnEntrar.setIcon(new ImageIcon("icons/login.png"));
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
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

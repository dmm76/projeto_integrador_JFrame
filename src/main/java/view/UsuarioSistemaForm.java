package view;

import dao.UsuarioSistemaDao;
import model.UsuarioSistema;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static util.EstiloSistema.*;

public class UsuarioSistemaForm extends JPanel {

    private final JTextField txtNome = new JTextField(20);
    private final JTextField txtLogin = new JTextField(20);
    private final JPasswordField txtSenha = new JPasswordField(20);
    private final JComboBox<String> cbPerfil = new JComboBox<>(new String[]{"admin", "operador"});

    private final JButton btnCadastrar = new JButton("Cadastrar");
    private final JButton btnLimpar = new JButton("Limpar");
    private final JButton btnAlterar = new JButton("Alterar");
    private final JButton btnRemover = new JButton("Remover");

    private final JTable tabela;
    private final DefaultTableModel tableModel;

    public UsuarioSistemaForm() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COR_FUNDO);

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        adicionarCampo(formPanel, gbc, "Nome:", txtNome, 0);
        adicionarCampo(formPanel, gbc, "Login:", txtLogin, 1);
        adicionarCampo(formPanel, gbc, "Senha:", txtSenha, 2);
        adicionarCampo(formPanel, gbc, "Perfil:", cbPerfil, 3);

        // Painel de botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botoesPanel.setBackground(COR_FUNDO);
        estilizarBotao(btnCadastrar);
        estilizarBotao(btnLimpar);
        estilizarBotao(btnAlterar);
        estilizarBotao(btnRemover);
        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnLimpar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(botoesPanel, gbc);

        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Login", "Perfil"}, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        // Adiciona ao painel principal
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        btnCadastrar.addActionListener(e -> salvarUsuario());
        btnAlterar.addActionListener(e -> alterarUsuario());
        btnRemover.addActionListener(e -> removerUsuario());
        tabela.getSelectionModel().addListSelectionListener(this::preencherCampos);
        btnLimpar.addActionListener(e -> {
            limparCampos();
            btnCadastrar.setEnabled(true);
        });

        desabilitarCadastroAoFocar(txtNome, txtLogin, txtSenha, cbPerfil);
        carregarUsuarios();
    }

    private void adicionarCampo(JPanel panel, GridBagConstraints gbc, String label, JComponent campo, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(COR_TEXTO);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        panel.add(campo, gbc);
    }

    private void estilizarBotao(JButton botao) {
        botao.setBackground(COR_BOTAO);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(FONTE_BOTAO);
    }

    private void desabilitarCadastroAoFocar(JComponent... componentes) {
        for (JComponent comp : componentes) {
            if (comp instanceof JTextField || comp instanceof JPasswordField) {
                comp.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent evt) {
                        btnCadastrar.setEnabled(false);
                    }
                });
            } else if (comp instanceof JComboBox) {
                ((JComboBox<?>) comp).addActionListener(e -> btnCadastrar.setEnabled(false));
            }
        }
    }

    private void salvarUsuario() {
        if (!btnCadastrar.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Clique em 'Limpar' para cadastrar um novo usuário.");
            return;
        }

        String nome = txtNome.getText().trim();
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        String perfil = (String) cbPerfil.getSelectedItem();

        if (senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A senha não pode estar em branco.");
            return;
        }

        UsuarioSistema usuario = new UsuarioSistema(nome, login, senha, perfil);
        EntityManager em = JPAUtil.getEntityManager();
        UsuarioSistemaDao dao = new UsuarioSistemaDao(em);

        em.getTransaction().begin();
        dao.cadastrar(usuario);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
        limparCampos();
        carregarUsuarios();
    }


    private void carregarUsuarios() {
        tableModel.setRowCount(0);
        EntityManager em = JPAUtil.getEntityManager();
        UsuarioSistemaDao dao = new UsuarioSistemaDao(em);
        List<UsuarioSistema> lista = dao.buscarTodos();
        for (UsuarioSistema u : lista) {
            tableModel.addRow(new Object[]{u.getIdUsuarioSistema(), u.getNomeUsuario(), u.getLoginUsuario(), u.getPerfilUsuario()});
        }
        em.close();
    }

    private void alterarUsuario() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para alterar.");
            return;
        }

        String novaSenha = new String(txtSenha.getPassword()).trim();
        if (novaSenha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A senha não pode estar em branco.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        EntityManager em = JPAUtil.getEntityManager();
        UsuarioSistemaDao dao = new UsuarioSistemaDao(em);
        UsuarioSistema usuario = dao.buscarPorID(id);

        if ("admin".equalsIgnoreCase(usuario.getLoginUsuario())) {
            JOptionPane.showMessageDialog(this, "O usuário 'admin' não pode ser alterado.");
            em.close();
            return;
        }

        usuario.setNomeUsuario(txtNome.getText().trim());
        usuario.setLoginUsuario(txtLogin.getText().trim());
        usuario.setSenhaUsuario(novaSenha);
        usuario.setPerfilUsuario((String) cbPerfil.getSelectedItem());

        em.getTransaction().begin();
        dao.alterar(usuario);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
        limparCampos();
        carregarUsuarios();
    }


    private void removerUsuario() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para remover.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        EntityManager em = JPAUtil.getEntityManager();
        UsuarioSistemaDao dao = new UsuarioSistemaDao(em);
        UsuarioSistema usuario = dao.buscarPorID(id);

        if ("admin".equalsIgnoreCase(usuario.getLoginUsuario())) {
            JOptionPane.showMessageDialog(this, "O usuário 'admin' não pode ser removido.");
            em.close();
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover este usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        em.getTransaction().begin();
        dao.remover(usuario);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Usuário removido com sucesso!");
        limparCampos();
        carregarUsuarios();
    }

    private void preencherCampos(ListSelectionEvent e) {
        int row = tabela.getSelectedRow();
        if (row != -1) {
            txtNome.setText((String) tableModel.getValueAt(row, 1));
            txtLogin.setText((String) tableModel.getValueAt(row, 2));
            cbPerfil.setSelectedItem((String) tableModel.getValueAt(row, 3));
            txtSenha.setText("");
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        cbPerfil.setSelectedIndex(0);
        tabela.clearSelection();
    }
}

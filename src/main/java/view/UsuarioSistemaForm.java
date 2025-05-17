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

public class UsuarioSistemaForm extends JPanel {

    private JTextField txtNome, txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cbPerfil;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnCadastrar, btnBuscar, btnAlterar, btnRemover;

    public UsuarioSistemaForm() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = gbc.gridy = 0;

        txtNome = new JTextField(20);
        txtLogin = new JTextField(20);
        txtSenha = new JPasswordField(20);
        cbPerfil = new JComboBox<>(new String[]{"admin", "operador"});

        btnCadastrar = new JButton("Cadastrar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnRemover = new JButton("Remover");

        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx++;
        formPanel.add(txtNome, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Login:"), gbc);
        gbc.gridx++;
        formPanel.add(txtLogin, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Senha:"), gbc);
        gbc.gridx++;
        formPanel.add(txtSenha, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Perfil:"), gbc);
        gbc.gridx++;
        formPanel.add(cbPerfil, gbc);
        gbc.gridx = 0; gbc.gridy++;

        gbc.gridwidth = 2;
        JPanel botoesPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);
        formPanel.add(botoesPanel, gbc);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Login", "Perfil"}, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> salvarUsuario());
        btnBuscar.addActionListener(e -> carregarUsuarios());
        btnAlterar.addActionListener(e -> alterarUsuario());
        btnRemover.addActionListener(e -> removerUsuario());

        tabela.getSelectionModel().addListSelectionListener(this::preencherCampos);

        carregarUsuarios();
    }

    private void salvarUsuario() {
        String nome = txtNome.getText().trim();
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        String perfil = (String) cbPerfil.getSelectedItem();

        EntityManager em = JPAUtil.getEntityManager();
        UsuarioSistemaDao dao = new UsuarioSistemaDao(em);

        UsuarioSistema usuario = new UsuarioSistema(nome, login, senha, perfil);
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
        usuario.setSenhaUsuario(new String(txtSenha.getPassword()).trim());
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
    }
}

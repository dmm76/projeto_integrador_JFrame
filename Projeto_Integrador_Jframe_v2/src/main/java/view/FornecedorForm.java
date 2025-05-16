package view;

import dao.FornecedorDao;
import model.Fornecedor;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FornecedorForm extends JPanel {

    private JTextField txtNome, txtCnpj, txtEmail, txtTelefone, txtEndereco;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnCadastrar, btnBuscar, btnAlterar, btnRemover;

    public FornecedorForm() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = gbc.gridy = 0;

        txtNome = new JTextField(20);
        txtCnpj = new JTextField(20);
        txtEmail = new JTextField(20);
        txtTelefone = new JTextField(20);
        txtEndereco = new JTextField(20);

        btnCadastrar = new JButton("Cadastrar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnRemover = new JButton("Remover");

        // Linha 1 - Nome e CNPJ
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx++;
        formPanel.add(txtNome, gbc);
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("CNPJ:"), gbc);
        gbc.gridx++;
        formPanel.add(txtCnpj, gbc);

        // Linha 2 - Email e Endereço
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx++;
        formPanel.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx++;
        formPanel.add(txtEndereco, gbc);

        // Linha 3 - Telefone
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx++;
        formPanel.add(txtTelefone, gbc);

        // Linha 4 - Botões
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel botoesPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);
        formPanel.add(botoesPanel, gbc);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "CNPJ", "Email", "Telefone", "Endereço"}, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Ações
        btnCadastrar.addActionListener(e -> salvarFornecedor());
        btnBuscar.addActionListener(e -> carregarFornecedores());
        btnAlterar.addActionListener(e -> alterarFornecedor());
        btnRemover.addActionListener(e -> removerFornecedor());

        // Atualiza os campos ao clicar na tabela
        tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                    int selectedRow = tabela.getSelectedRow();
                    txtNome.setText((String) tableModel.getValueAt(selectedRow, 1));
                    txtCnpj.setText((String) tableModel.getValueAt(selectedRow, 2));
                    txtEmail.setText((String) tableModel.getValueAt(selectedRow, 3));
                    txtTelefone.setText((String) tableModel.getValueAt(selectedRow, 4));
                    txtEndereco.setText((String) tableModel.getValueAt(selectedRow, 5));
                }
            }
        });

        carregarFornecedores();
    }

    private void salvarFornecedor() {
        String nome = txtNome.getText().trim();
        String cnpj = txtCnpj.getText().trim();
        String email = txtEmail.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String endereco = txtEndereco.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome, email e telefone são obrigatórios.");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        FornecedorDao dao = new FornecedorDao(em);

        Fornecedor fornecedor = new Fornecedor(nome, cnpj, email, telefone, endereco);
        em.getTransaction().begin();
        dao.cadastrar(fornecedor);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Fornecedor cadastrado com sucesso!");
        limparCampos();
        carregarFornecedores();
    }

    private void carregarFornecedores() {
        tableModel.setRowCount(0);
        EntityManager em = JPAUtil.getEntityManager();
        FornecedorDao dao = new FornecedorDao(em);
        List<Fornecedor> lista = dao.buscarTodos();
        for (Fornecedor f : lista) {
            tableModel.addRow(new Object[]{
                    f.getIdFornecedor(),
                    f.getNomeFornecedor(),
                    f.getCnpjFornecedor(),
                    f.getEmailFornecedor(),
                    f.getTelefoneFornecedor(),
                    f.getEnderecoFornecedor()
            });
        }
        em.close();
    }

    private void alterarFornecedor() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor para alterar.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        EntityManager em = JPAUtil.getEntityManager();
        FornecedorDao dao = new FornecedorDao(em);
        Fornecedor fornecedor = dao.buscarPorID(id);

        String novoNome = JOptionPane.showInputDialog(this, "Novo nome:", fornecedor.getNomeFornecedor());
        if (novoNome == null || novoNome.trim().isEmpty()) return;

        em.getTransaction().begin();
        fornecedor.setNomeFornecedor(novoNome);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Fornecedor atualizado com sucesso!");
        carregarFornecedores();
    }

    private void removerFornecedor() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover este fornecedor?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        EntityManager em = JPAUtil.getEntityManager();
        FornecedorDao dao = new FornecedorDao(em);
        Fornecedor fornecedor = dao.buscarPorID(id);

        em.getTransaction().begin();
        dao.remover(fornecedor);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Fornecedor removido com sucesso!");
        carregarFornecedores();
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCnpj.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtEndereco.setText("");
    }
}

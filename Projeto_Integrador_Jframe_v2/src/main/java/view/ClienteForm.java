package view;

import dao.ClienteDao;
import model.Cliente;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ClienteForm extends JPanel {

    private JTextField txtNome, txtCpf, txtEmail, txtTelefone, txtEndereco;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnCadastrar, btnBuscar, btnAlterar, btnRemover;

    public ClienteForm() {
        setLayout(new BorderLayout(10, 10));

        // Painel do formulário com GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField(20);
        txtCpf = new JTextField(20);
        txtEmail = new JTextField(20);
        txtTelefone = new JTextField(20);
        txtEndereco = new JTextField(20);

        btnCadastrar = new JButton("Cadastrar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnRemover = new JButton("Remover");

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtCpf, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTelefone, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtEndereco, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel botoesPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);
        formPanel.add(botoesPanel, gbc);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF"}, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Ações dos botões
        btnCadastrar.addActionListener(e -> salvarCliente());
        btnBuscar.addActionListener(e -> carregarClientes());
        btnAlterar.addActionListener(e -> alterarCliente());
        btnRemover.addActionListener(e -> removerCliente());

        // Seleção da tabela para preencher campos
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabela.getSelectedRow();
                if (row != -1) {
                    txtNome.setText(tableModel.getValueAt(row, 1).toString());
                    txtCpf.setText(tableModel.getValueAt(row, 2).toString());
                    EntityManager em = JPAUtil.getEntityManager();
                    ClienteDao dao = new ClienteDao(em);
                    Cliente cliente = dao.buscarPorID((int) tableModel.getValueAt(row, 0));
                    txtEmail.setText(cliente.getEmailCliente());
                    txtTelefone.setText(cliente.getTelefoneCliente());
                    txtEndereco.setText(cliente.getEnderecoCliente());
                    em.close();
                }
            }
        });

        carregarClientes();
    }

    private void salvarCliente() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().trim();
        String email = txtEmail.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String endereco = txtEndereco.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome, email e telefone são obrigatórios.");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        ClienteDao dao = new ClienteDao(em);

        Cliente cliente = new Cliente(nome, cpf, email, telefone, endereco);
        em.getTransaction().begin();
        dao.cadastrar(cliente);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
        limparCampos();
        carregarClientes();
    }

    private void carregarClientes() {
        tableModel.setRowCount(0);
        EntityManager em = JPAUtil.getEntityManager();
        ClienteDao dao = new ClienteDao(em);
        List<Cliente> lista = dao.buscarTodos();
        for (Cliente c : lista) {
            tableModel.addRow(new Object[]{c.getIdCliente(), c.getNomeCliente(), c.getCpfCliente()});
        }
        em.close();
    }

    private void alterarCliente() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para alterar.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        EntityManager em = JPAUtil.getEntityManager();
        ClienteDao dao = new ClienteDao(em);
        Cliente cliente = dao.buscarPorID(id);

        cliente.setNomeCliente(txtNome.getText().trim());
        cliente.setCpfCliente(txtCpf.getText().trim());
        cliente.setEmailCliente(txtEmail.getText().trim());
        cliente.setTelefoneCliente(txtTelefone.getText().trim());
        cliente.setEnderecoCliente(txtEndereco.getText().trim());

        em.getTransaction().begin();
        dao.alterar(cliente);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Cliente alterado com sucesso!");
        limparCampos();
        carregarClientes();
    }

    private void removerCliente() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover este cliente?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        EntityManager em = JPAUtil.getEntityManager();
        ClienteDao dao = new ClienteDao(em);
        Cliente cliente = dao.buscarPorID(id);

        em.getTransaction().begin();
        dao.remover(cliente);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Cliente removido com sucesso!");
        limparCampos();
        carregarClientes();
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtEndereco.setText("");
    }
}

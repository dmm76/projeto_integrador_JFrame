package view;

import dao.ClienteDao;
import model.Cliente;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import static util.EstiloSistema.*;

public class ClienteForm extends JPanel {

    private final JTextField txtNome = new JTextField(20);
    private final JTextField txtCpf = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtTelefone = new JTextField(20);
    private final JTextField txtEndereco = new JTextField(20);
    private final JButton btnSalvar = new JButton("Cadastrar");
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnAlterar = new JButton("Alterar");
    private final JButton btnRemover = new JButton("Remover");
    private final JTable tabela;
    private final DefaultTableModel tableModel;

    private final Consumer<Void> onSaveCallback;

    public ClienteForm() {
        this(null);
    }

    public ClienteForm(Consumer<Void> onSaveCallback) {
        this.onSaveCallback = onSaveCallback;
        setLayout(new BorderLayout(10, 10));
        setBackground(COR_FUNDO);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int linha = 0;
        adicionarCampo(formPanel, "Nome:", txtNome, gbc, linha++);
        adicionarCampo(formPanel, "CPF:", txtCpf, gbc, linha++);
        adicionarCampo(formPanel, "E-mail:", txtEmail, gbc, linha++);
        adicionarCampo(formPanel, "Telefone:", txtTelefone, gbc, linha++);
        adicionarCampo(formPanel, "Endereço:", txtEndereco, gbc, linha);

        aplicarEstiloCampo(txtNome);
        aplicarEstiloCampo(txtCpf);
        aplicarEstiloCampo(txtEmail);
        aplicarEstiloCampo(txtTelefone);
        aplicarEstiloCampo(txtEndereco);

        aplicarEstiloBotao(btnSalvar);
        aplicarEstiloBotao(btnBuscar);
        aplicarEstiloBotao(btnAlterar);
        aplicarEstiloBotao(btnRemover);

        Dimension buttonSize = new Dimension(130, 30);
        btnSalvar.setPreferredSize(buttonSize);
        btnBuscar.setPreferredSize(buttonSize);
        btnAlterar.setPreferredSize(buttonSize);
        btnRemover.setPreferredSize(buttonSize);

        // botão cadastrar ao lado do campo Endereço
        gbc.gridx = 2;
        formPanel.add(btnSalvar, gbc);

        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 3;
        JPanel botoesPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        botoesPanel.setBackground(COR_FUNDO);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);
        JButton btnLimpar = new JButton("Limpar");
        aplicarEstiloBotao(btnLimpar);
        btnLimpar.setPreferredSize(new Dimension(130, 30));
        btnLimpar.addActionListener(e -> limparCampos());
        botoesPanel.add(btnLimpar);
        formPanel.add(botoesPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF", "Email", "Telefone", "Endereço"}, 0);
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnSalvar.addActionListener(e -> salvarCliente());
        btnBuscar.addActionListener(e -> carregarClientes());
        btnAlterar.addActionListener(e -> alterarCliente());
        btnRemover.addActionListener(e -> removerCliente());

        tabela.getSelectionModel().addListSelectionListener(e ->{
            if (!e.getValueIsAdjusting()) {
                preencherCamposComSelecionado();
                btnSalvar.setEnabled(false);
            }
        });
        carregarClientes();
    }

    private void adicionarCampo(JPanel panel, String rotulo, JTextField campo, GridBagConstraints gbc, int linha) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 1;
        JLabel label = new JLabel(rotulo);
        aplicarEstiloLabel(label);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(campo, gbc);
        gbc.weightx = 0;
    }

    private void salvarCliente() {
        try {
            if (txtNome.getText().trim().isEmpty() ||
                    txtCpf.getText().trim().isEmpty() ||
                    txtEmail.getText().trim().isEmpty() ||
                    txtTelefone.getText().trim().isEmpty() ||
                    txtEndereco.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
                return;
            }

            Cliente cliente = new Cliente(
                    txtNome.getText().trim(),
                    txtCpf.getText().trim(),
                    txtEmail.getText().trim(),
                    txtTelefone.getText().trim(),
                    txtEndereco.getText().trim()
            );

            EntityManager em = JPAUtil.getEntityManager();
            ClienteDao dao = new ClienteDao(em);

            em.getTransaction().begin();
            dao.cadastrar(cliente);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
            limparCampos();
            carregarClientes();
            if (onSaveCallback != null) onSaveCallback.accept(null);

            Window janela = SwingUtilities.getWindowAncestor(this);
            if (janela instanceof JDialog) janela.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar cliente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarCliente() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para alterar.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(row, 0);
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

            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            limparCampos();
            carregarClientes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar cliente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerCliente() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover este cliente?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = (int) tableModel.getValueAt(row, 0);
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover cliente: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCamposComSelecionado() {
        int row = tabela.getSelectedRow();
        if (row != -1) {
            txtNome.setText((String) tableModel.getValueAt(row, 1));
            txtCpf.setText((String) tableModel.getValueAt(row, 2));
            txtEmail.setText((String) tableModel.getValueAt(row, 3));
            txtTelefone.setText((String) tableModel.getValueAt(row, 4));
            txtEndereco.setText((String) tableModel.getValueAt(row, 5));
        }
    }

    private void carregarClientes() {
        tableModel.setRowCount(0);
        EntityManager em = JPAUtil.getEntityManager();
        List<Cliente> clientes = new ClienteDao(em).buscarTodos();
        for (Cliente c : clientes) {
            tableModel.addRow(new Object[]{
                    c.getIdCliente(),
                    c.getNomeCliente(),
                    c.getCpfCliente(),
                    c.getEmailCliente(),
                    c.getTelefoneCliente(),
                    c.getEnderecoCliente()
            });
        }
        em.close();
        btnSalvar.setEnabled(true);
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtEndereco.setText("");
        tabela.clearSelection();
        btnSalvar.setEnabled(true);
    }
}

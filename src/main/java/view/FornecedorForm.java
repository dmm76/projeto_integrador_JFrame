package view;

import dao.FornecedorDao;
import model.Fornecedor;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static util.EstiloSistema.*;

public class FornecedorForm extends JPanel {

    private final JTextField txtNome = new JTextField(20);
    private final JTextField txtCnpj = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtTelefone = new JTextField(20);
    private final JTextField txtEndereco = new JTextField(20);
    private final JTable tabela;
    private final DefaultTableModel tableModel;
    private final JButton btnCadastrar = new JButton("Cadastrar");
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnAlterar = new JButton("Alterar");
    private final JButton btnRemover = new JButton("Remover");

    public FornecedorForm() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COR_FUNDO);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int linha = 0;
        adicionarCampo(formPanel, "Nome:", txtNome, gbc, linha++);
        adicionarCampo(formPanel, "CNPJ:", txtCnpj, gbc, linha++);
        adicionarCampo(formPanel, "Email:", txtEmail, gbc, linha++);
        adicionarCampo(formPanel, "Telefone:", txtTelefone, gbc, linha++);
        adicionarCampo(formPanel, "Endereço:", txtEndereco, gbc, linha);

        aplicarEstiloCampo(txtNome);
        aplicarEstiloCampo(txtCnpj);
        aplicarEstiloCampo(txtEmail);
        aplicarEstiloCampo(txtTelefone);
        aplicarEstiloCampo(txtEndereco);

        aplicarEstiloBotao(btnCadastrar);
        aplicarEstiloBotao(btnBuscar);
        aplicarEstiloBotao(btnAlterar);
        aplicarEstiloBotao(btnRemover);

        Dimension buttonSize = new Dimension(130, 30);
        btnCadastrar.setPreferredSize(buttonSize);
        btnBuscar.setPreferredSize(buttonSize);
        btnAlterar.setPreferredSize(buttonSize);
        btnRemover.setPreferredSize(buttonSize);

        // botão cadastrar ao lado do campo Endereço
        gbc.gridx = 2;
        formPanel.add(btnCadastrar, gbc);

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
        btnLimpar.addActionListener(e -> {
            limparCampos();
            btnCadastrar.setEnabled(true);
        });
        botoesPanel.add(btnLimpar);
        formPanel.add(botoesPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "CNPJ", "Email", "Telefone", "Endereço"}, 0);
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> salvarFornecedor());
        btnBuscar.addActionListener(e -> carregarFornecedores());
        btnAlterar.addActionListener(e -> alterarFornecedor());
        btnRemover.addActionListener(e -> removerFornecedor());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCamposComSelecionado();
                btnCadastrar.setEnabled(false);
            }
        });
        carregarFornecedores();
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

    private void salvarFornecedor() {
        try {
            if (txtNome.getText().trim().isEmpty() ||
                    txtCnpj.getText().trim().isEmpty() ||
                    txtEmail.getText().trim().isEmpty() ||
                    txtTelefone.getText().trim().isEmpty() ||
                    txtEndereco.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
                return;
            }

            Fornecedor fornecedor = new Fornecedor(
                    txtNome.getText().trim(),
                    txtCnpj.getText().trim(),
                    txtEmail.getText().trim(),
                    txtTelefone.getText().trim(),
                    txtEndereco.getText().trim()
            );

            EntityManager em = JPAUtil.getEntityManager();
            FornecedorDao dao = new FornecedorDao(em);

            em.getTransaction().begin();
            dao.cadastrar(fornecedor);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Fornecedor cadastrado com sucesso!");
            limparCampos();
            carregarFornecedores();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar fornecedor: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarFornecedores() {
        tableModel.setRowCount(0);
        EntityManager em = JPAUtil.getEntityManager();
        List<Fornecedor> lista = new FornecedorDao(em).buscarTodos();
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
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor para alterar.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(row, 0);
            EntityManager em = JPAUtil.getEntityManager();
            FornecedorDao dao = new FornecedorDao(em);
            Fornecedor fornecedor = dao.buscarPorID(id);

            fornecedor.setNomeFornecedor(txtNome.getText().trim());
            fornecedor.setCnpjFornecedor(txtCnpj.getText().trim());
            fornecedor.setEmailFornecedor(txtEmail.getText().trim());
            fornecedor.setTelefoneFornecedor(txtTelefone.getText().trim());
            fornecedor.setEnderecoFornecedor(txtEndereco.getText().trim());

            em.getTransaction().begin();
            dao.alterar(fornecedor);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Fornecedor atualizado com sucesso!");
            limparCampos();
            carregarFornecedores();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar fornecedor: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerFornecedor() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover este fornecedor?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = (int) tableModel.getValueAt(row, 0);
            EntityManager em = JPAUtil.getEntityManager();
            FornecedorDao dao = new FornecedorDao(em);
            Fornecedor fornecedor = dao.buscarPorID(id);

            em.getTransaction().begin();
            dao.remover(fornecedor);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Fornecedor removido com sucesso!");
            limparCampos();
            carregarFornecedores();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover fornecedor: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCamposComSelecionado() {
        int row = tabela.getSelectedRow();
        if (row != -1) {
            txtNome.setText((String) tableModel.getValueAt(row, 1));
            txtCnpj.setText((String) tableModel.getValueAt(row, 2));
            txtEmail.setText((String) tableModel.getValueAt(row, 3));
            txtTelefone.setText((String) tableModel.getValueAt(row, 4));
            txtEndereco.setText((String) tableModel.getValueAt(row, 5));
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCnpj.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtEndereco.setText("");
        tabela.clearSelection();
        btnCadastrar.setEnabled(true);
    }
}

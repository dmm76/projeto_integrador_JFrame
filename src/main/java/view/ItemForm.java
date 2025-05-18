package view;

import dao.CategoriaDao;
import dao.FornecedorDao;
import dao.ItemDao;
import dao.MarcaDao;
import model.Categoria;
import model.Fornecedor;
import model.Item;
import model.Marca;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static util.EstiloSistema.*;

public class ItemForm extends JPanel {
    private final JTextField txtNome = new JTextField(20);
    private final JTextField txtDescricao = new JTextField(20);
    private final JTextField txtValor = new JTextField(10);
    private final JComboBox<Marca> cbMarca = new JComboBox<>();
    private final JComboBox<Categoria> cbCategoria = new JComboBox<>();
    private final JComboBox<Fornecedor> cbFornecedor = new JComboBox<>();
    private final JTable tabela;
    private final DefaultTableModel tableModel;
    private final JButton btnCadastrar = new JButton("Cadastrar");
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnAlterar = new JButton("Alterar");
    private final JButton btnRemover = new JButton("Remover");

    public ItemForm() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COR_FUNDO);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int linha = 0;
        adicionarCampo(formPanel, "Nome:", txtNome, gbc, linha++);
        adicionarCampo(formPanel, "Descrição:", txtDescricao, gbc, linha++);
        adicionarCampo(formPanel, "Valor Unitário:", txtValor, gbc, linha++);
        adicionarCampo(formPanel, "Marca:", cbMarca, gbc, linha++);
        adicionarCampo(formPanel, "Categoria:", cbCategoria, gbc, linha++);
        adicionarCampo(formPanel, "Fornecedor:", cbFornecedor, gbc, linha++);

        aplicarEstiloCampo(txtNome);
        aplicarEstiloCampo(txtDescricao);
        aplicarEstiloCampo(txtValor);

        aplicarEstiloBotao(btnCadastrar);
        aplicarEstiloBotao(btnBuscar);
        aplicarEstiloBotao(btnAlterar);
        aplicarEstiloBotao(btnRemover);

        configurarRenderizadores();
        carregarComboBox();

        JPanel botoesPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        botoesPanel.setBackground(COR_FUNDO);
        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 2;
        formPanel.add(botoesPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Valor", "Marca", "Categoria", "Fornecedor"}, 0);
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> salvarItem());
        btnBuscar.addActionListener(e -> carregarItens());
        btnAlterar.addActionListener(e -> alterarItem());
        btnRemover.addActionListener(e -> removerItem());

        tabela.getSelectionModel().addListSelectionListener(this::preencherCamposComSelecionado);
        carregarItens();
    }

    private void adicionarCampo(JPanel panel, String rotulo, JComponent campo, GridBagConstraints gbc, int linha) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 1;
        JLabel label = new JLabel(rotulo);
        aplicarEstiloLabel(label);
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(campo, gbc);
    }

    private void carregarComboBox() {
        EntityManager em = JPAUtil.getEntityManager();
        cbMarca.removeAllItems();
        cbCategoria.removeAllItems();
        cbFornecedor.removeAllItems();

        cbMarca.addItem(null);
        new MarcaDao(em).buscarTodos().forEach(cbMarca::addItem);

        cbCategoria.addItem(null);
        new CategoriaDao(em).buscarTodos().forEach(cbCategoria::addItem);

        cbFornecedor.addItem(null);
        new FornecedorDao(em).buscarTodos().forEach(cbFornecedor::addItem);

        em.close();
    }

    private void configurarRenderizadores() {
        cbMarca.setRenderer((list, value, index, isSelected, cellHasFocus) -> new JLabel(value != null ? value.getDescricao() : "-- Selecione --"));
        cbCategoria.setRenderer((list, value, index, isSelected, cellHasFocus) -> new JLabel(value != null ? value.getDescricao() : "-- Selecione --"));
        cbFornecedor.setRenderer((list, value, index, isSelected, cellHasFocus) -> new JLabel(value != null ? value.getNomeFornecedor() : "-- Selecione --"));
    }

    private void salvarItem() {
        try {
            if (txtNome.getText().trim().isEmpty() ||
                    txtDescricao.getText().trim().isEmpty() ||
                    txtValor.getText().trim().isEmpty() ||
                    cbMarca.getSelectedItem() == null ||
                    cbCategoria.getSelectedItem() == null ||
                    cbFornecedor.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
                return;
            }

            double valor = Double.parseDouble(txtValor.getText().trim());
            Item item = new Item(
                    txtNome.getText().trim(),
                    txtDescricao.getText().trim(),
                    valor,
                    (Marca) cbMarca.getSelectedItem(),
                    (Categoria) cbCategoria.getSelectedItem(),
                    (Fornecedor) cbFornecedor.getSelectedItem()
            );

            EntityManager em = JPAUtil.getEntityManager();
            ItemDao dao = new ItemDao(em);

            em.getTransaction().begin();
            dao.cadastrar(item);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Item cadastrado com sucesso!");
            limparCampos();
            carregarItens();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Digite um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarItens() {
        tableModel.setRowCount(0);
        EntityManager em = JPAUtil.getEntityManager();
        List<Item> itens = new ItemDao(em).listarTodos();
        for (Item i : itens) {
            tableModel.addRow(new Object[]{
                    i.getIdItem(),
                    i.getNomeProduto(),
                    i.getValorUnitarioProduto(),
                    i.getMarca(),
                    i.getCategoria(),
                    i.getFornecedor()
            });
        }
        em.close();
    }

    private void alterarItem() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item para alterar.");
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            EntityManager em = JPAUtil.getEntityManager();
            ItemDao dao = new ItemDao(em);
            Item item = dao.buscarPorId(id);

            item.setNomeProduto(txtNome.getText().trim());
            item.setDescricaoProduto(txtDescricao.getText().trim());
            item.setValorUnitarioProduto(Double.parseDouble(txtValor.getText().trim()));
            item.setMarca((Marca) cbMarca.getSelectedItem());
            item.setCategoria((Categoria) cbCategoria.getSelectedItem());
            item.setFornecedor((Fornecedor) cbFornecedor.getSelectedItem());

            em.getTransaction().begin();
            dao.atualizar(item);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Item atualizado com sucesso!");
            limparCampos();
            carregarItens();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerItem() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover este item?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = (int) tableModel.getValueAt(row, 0);
            EntityManager em = JPAUtil.getEntityManager();
            ItemDao dao = new ItemDao(em);
            Item item = dao.buscarPorId(id);

            em.getTransaction().begin();
            dao.remover(item);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Item removido com sucesso!");
            limparCampos();
            carregarItens();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherCamposComSelecionado(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = tabela.getSelectedRow();
        if (row == -1) return;

        int id = (int) tableModel.getValueAt(row, 0);
        EntityManager em = JPAUtil.getEntityManager();
        Item item = new ItemDao(em).buscarPorId(id);
        em.close();

        txtNome.setText(item.getNomeProduto());
        txtDescricao.setText(item.getDescricaoProduto());
        txtValor.setText(String.valueOf(item.getValorUnitarioProduto()));
        cbMarca.setSelectedItem(item.getMarca());
        cbCategoria.setSelectedItem(item.getCategoria());
        cbFornecedor.setSelectedItem(item.getFornecedor());
    }

    private void limparCampos() {
        txtNome.setText("");
        txtDescricao.setText("");
        txtValor.setText("");
        cbMarca.setSelectedIndex(0);
        cbCategoria.setSelectedIndex(0);
        cbFornecedor.setSelectedIndex(0);
    }
}

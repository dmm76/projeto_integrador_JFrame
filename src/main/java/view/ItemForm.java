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

public class ItemForm extends JPanel {

    private JTextField txtNome, txtDescricao, txtValor;
    private JComboBox<Marca> cbMarca;
    private JComboBox<Categoria> cbCategoria;
    private JComboBox<Fornecedor> cbFornecedor;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnCadastrar, btnBuscar, btnAlterar, btnRemover;

    public ItemForm() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = gbc.gridy = 0;

        txtNome = new JTextField(20);
        txtDescricao = new JTextField(20);
        txtValor = new JTextField(10);
        cbMarca = new JComboBox<>();
        cbCategoria = new JComboBox<>();
        cbFornecedor = new JComboBox<>();

        carregarComboBox();
        configurarRenderizadores();

        btnCadastrar = new JButton("Cadastrar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnRemover = new JButton("Remover");

        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx++;
        formPanel.add(txtNome, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx++;
        formPanel.add(txtDescricao, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Valor Unitário:"), gbc);
        gbc.gridx++;
        formPanel.add(txtValor, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Marca:"), gbc);
        gbc.gridx++;
        formPanel.add(cbMarca, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx++;
        formPanel.add(cbCategoria, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Fornecedor:"), gbc);
        gbc.gridx++;
        formPanel.add(cbFornecedor, gbc);
        gbc.gridx = 0; gbc.gridy++;

        gbc.gridwidth = 2;
        JPanel botoesPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);
        formPanel.add(botoesPanel, gbc);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Valor", "Marca", "Categoria", "Fornecedor"}, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> salvarItem());
        btnBuscar.addActionListener(e -> carregarItens());
        btnAlterar.addActionListener(e -> alterarItem());
        btnRemover.addActionListener(e -> removerItem());

        tabela.getSelectionModel().addListSelectionListener(this::preencherCamposComSelecionado);

        carregarItens();
    }

    private void carregarComboBox() {
        EntityManager em = JPAUtil.getEntityManager();

        cbMarca.removeAllItems();
        cbMarca.addItem(null);
        new MarcaDao(em).buscarTodos().forEach(cbMarca::addItem);

        cbCategoria.removeAllItems();
        cbCategoria.addItem(null);
        new CategoriaDao(em).buscarTodos().forEach(cbCategoria::addItem);

        cbFornecedor.removeAllItems();
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
        String nome = txtNome.getText().trim();
        String descricao = txtDescricao.getText().trim();
        double valor = Double.parseDouble(txtValor.getText().trim());

        Marca marca = (Marca) cbMarca.getSelectedItem();
        Categoria categoria = (Categoria) cbCategoria.getSelectedItem();
        Fornecedor fornecedor = (Fornecedor) cbFornecedor.getSelectedItem();

        EntityManager em = JPAUtil.getEntityManager();
        ItemDao dao = new ItemDao(em);

        Item item = new Item(nome, descricao, valor, marca, categoria, fornecedor);
        em.getTransaction().begin();
        dao.cadastrar(item);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Item cadastrado com sucesso!");
        limparCampos();
        carregarItens();
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
        em.merge(item);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Item atualizado com sucesso!");
        carregarItens();
    }

    private void removerItem() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover este item?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) tableModel.getValueAt(row, 0);
        EntityManager em = JPAUtil.getEntityManager();
        Item item = new ItemDao(em).buscarPorId(id);

        em.getTransaction().begin();
        em.remove(item);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Item removido com sucesso!");
        carregarItens();
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

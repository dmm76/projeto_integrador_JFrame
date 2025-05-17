package view;

import dao.ItemDao;
import dao.PedidoDao;
import dao.PedidoItemDao;
import model.Item;
import model.Pedido;
import model.PedidoItem;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ItemPedidoForm extends JPanel {
    private Pedido pedidoFiltrado;
    private JComboBox<Pedido> cbPedido;
    private JComboBox<Item> cbItem;
    private JTextField txtQuantidade, txtValor, txtValorTotal;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnCadastrar, btnBuscar, btnAlterar, btnRemover;

    public ItemPedidoForm() {
        this(null); // construtor padrão chama o que aceita filtro
    }

    public ItemPedidoForm(Pedido pedidoFiltrado) {
        this.pedidoFiltrado = pedidoFiltrado;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = gbc.gridy = 0;

        cbPedido = new JComboBox<>();
        cbItem = new JComboBox<>();
        txtQuantidade = new JTextField(10);
        txtValor = new JTextField(10);
        txtValorTotal = new JTextField(10);
        txtValorTotal.setEditable(false);

        carregarComboBox();

        cbItem.addActionListener(e -> atualizarValorUnitario());

        btnCadastrar = new JButton("Cadastrar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnRemover = new JButton("Remover");

        formPanel.add(new JLabel("Pedido:"), gbc);
        gbc.gridx++;
        formPanel.add(cbPedido, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Produto:"), gbc);
        gbc.gridx++;
        formPanel.add(cbItem, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx++;
        formPanel.add(txtQuantidade, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Valor Unitário:"), gbc);
        gbc.gridx++;
        formPanel.add(txtValor, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Valor Total (Banco):"), gbc);
        gbc.gridx++;
        formPanel.add(txtValorTotal, gbc);
        gbc.gridx = 0; gbc.gridy++;

        gbc.gridwidth = 2;
        JPanel botoesPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);
        formPanel.add(botoesPanel, gbc);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Produto", "Qtd", "Valor Unitário", "Valor Total", "Pedido"}, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> salvarItemPedido());
        btnBuscar.addActionListener(e -> carregarItemPedidos());
        btnAlterar.addActionListener(e -> alterarItemPedido());
        btnRemover.addActionListener(e -> removerItemPedido());

        tabela.getSelectionModel().addListSelectionListener(this::preencherCamposComSelecionado);

        carregarItemPedidos();
    }

    private void carregarComboBox() {
        EntityManager em = JPAUtil.getEntityManager();

        cbPedido.removeAllItems();
        cbItem.removeAllItems();

        Pedido pedidoVazio = new Pedido();
        pedidoVazio.setStatusPedido("-- Selecione --");
        cbPedido.addItem(pedidoVazio);
        new PedidoDao(em).buscarTodos().forEach(cbPedido::addItem);

        Item itemVazio = new Item();
        itemVazio.setNomeProduto("-- Selecione --");
        cbItem.addItem(itemVazio);
        new ItemDao(em).listarTodos().forEach(cbItem::addItem);

        em.close();
    }

    private void atualizarValorUnitario() {
        Item itemSelecionado = (Item) cbItem.getSelectedItem();
        if (itemSelecionado != null && itemSelecionado.getIdItem() != 0) {
            txtValor.setText(String.valueOf(itemSelecionado.getValorUnitarioProduto()));
        } else {
            txtValor.setText("");
        }
    }

    private void salvarItemPedido() {
        Pedido pedido = (Pedido) cbPedido.getSelectedItem();
        Item item = (Item) cbItem.getSelectedItem();
        int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
        double valor = Double.parseDouble(txtValor.getText().trim());

        PedidoItem pedidoItem = new PedidoItem(item, pedido, quantidade, valor);

        EntityManager em = JPAUtil.getEntityManager();
        PedidoItemDao dao = new PedidoItemDao(em);
        em.getTransaction().begin();
        dao.cadastrar(pedidoItem);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Item do Pedido cadastrado com sucesso!");
        limparCampos();
        carregarItemPedidos();
    }

    private void carregarItemPedidos() {
        tableModel.setRowCount(0);
        EntityManager em = JPAUtil.getEntityManager();
        List<PedidoItem> lista = new PedidoItemDao(em).buscarTodos();
        for (PedidoItem pi : lista) {
            tableModel.addRow(new Object[]{
                    pi.getIdPedidoItem(),
                    pi.getItem().getNomeProduto(),
                    pi.getQuantidadeItem(),
                    pi.getValorItem(),
                    pi.getValorTotalItem(),
                    pi.getPedido().getIdPedido()
            });
        }
        em.close();
    }

    private void alterarItemPedido() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item do pedido para alterar.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);

        EntityManager em = JPAUtil.getEntityManager();
        PedidoItemDao dao = new PedidoItemDao(em);
        PedidoItem pedidoItem = dao.buscarPorID(id);

        pedidoItem.setPedido((Pedido) cbPedido.getSelectedItem());
        pedidoItem.setItem((Item) cbItem.getSelectedItem());
        pedidoItem.setQuantidadeItem(Integer.parseInt(txtQuantidade.getText().trim()));
        pedidoItem.setValorItem(Double.parseDouble(txtValor.getText().trim()));

        em.getTransaction().begin();
        dao.alterar(pedidoItem);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Item do pedido alterado com sucesso!");
        carregarItemPedidos();
    }

    private void removerItemPedido() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item do pedido para remover.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        EntityManager em = JPAUtil.getEntityManager();
        PedidoItem pedidoItem = new PedidoItemDao(em).buscarPorID(id);

        em.getTransaction().begin();
        em.remove(pedidoItem);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Item do pedido removido com sucesso!");
        carregarItemPedidos();
    }

    private void preencherCamposComSelecionado(ListSelectionEvent e) {
        int row = tabela.getSelectedRow();
        if (row != -1) {
            txtQuantidade.setText(tableModel.getValueAt(row, 2).toString());
            txtValor.setText(tableModel.getValueAt(row, 3).toString());
            txtValorTotal.setText(tableModel.getValueAt(row, 4).toString());

            String nomeItem = (String) tableModel.getValueAt(row, 1);
            int idPedido = (int) tableModel.getValueAt(row, 5);

            for (int i = 0; i < cbItem.getItemCount(); i++) {
                if (cbItem.getItemAt(i).toString().equals(nomeItem)) {
                    cbItem.setSelectedIndex(i);
                    break;
                }
            }

            for (int i = 0; i < cbPedido.getItemCount(); i++) {
                if (cbPedido.getItemAt(i).getIdPedido() == idPedido) {
                    cbPedido.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void limparCampos() {
        txtQuantidade.setText("");
        txtValor.setText("");
        txtValorTotal.setText("");
        cbPedido.setSelectedIndex(0);
        cbItem.setSelectedIndex(0);
    }
}

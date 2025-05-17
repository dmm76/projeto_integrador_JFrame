package view;

import dao.*;
import model.*;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendaForm extends JPanel {

    private JComboBox<Item> cbItem;
    private JTextField txtQuantidade;
    private JTextField txtValorUnitario;
    private JTextField txtTotalVenda;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnAdicionar, btnRemover, btnFinalizar;

    private JComboBox<Cliente> cbCliente;
    private JComboBox<FormaPagamento> cbFormaPagamento;
    private JLabel lblPedidoInfo;

    private Pedido pedido;
    private List<PedidoItem> carrinho;

    public VendaForm() {
        setLayout(new BorderLayout(10, 10));

        carrinho = new ArrayList<>();
        EntityManager em = JPAUtil.getEntityManager();
        pedido = new Pedido();
        pedido.setDataPedido(new Date());
        pedido.setStatusPedido("Aberto");
        pedido.setValorTotalPedido(0);
        pedido.setCliente(null);
        pedido.setFormaPagamento(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = gbc.gridy = 0;

        cbItem = new JComboBox<>();
        cbCliente = new JComboBox<>();
        cbFormaPagamento = new JComboBox<>();
        txtQuantidade = new JTextField(10);
        txtValorUnitario = new JTextField(10);
        txtValorUnitario.setEditable(false);
        txtTotalVenda = new JTextField(10);
        txtTotalVenda.setEditable(false);

        cbItem.addActionListener(e -> {
            Item item = (Item) cbItem.getSelectedItem();
            if (item != null) txtValorUnitario.setText(String.valueOf(item.getValorUnitarioProduto()));
        });

        carregarComboBox();

        lblPedidoInfo = new JLabel("Pedido em Aberto (ID será gerado ao finalizar)");
        gbc.gridwidth = 2;
        formPanel.add(lblPedidoInfo, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx++;
        formPanel.add(cbCliente, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Forma de Pagamento:"), gbc);
        gbc.gridx++;
        formPanel.add(cbFormaPagamento, gbc);
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
        formPanel.add(txtValorUnitario, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Total Venda:"), gbc);
        gbc.gridx++;
        formPanel.add(txtTotalVenda, gbc);
        gbc.gridx = 0; gbc.gridy++;

        btnAdicionar = new JButton("Adicionar à Venda");
        btnRemover = new JButton("Remover Item");
        btnFinalizar = new JButton("Finalizar Venda");

        gbc.gridwidth = 2;
        JPanel botoesPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        botoesPanel.add(btnAdicionar);
        botoesPanel.add(btnRemover);
        botoesPanel.add(btnFinalizar);
        formPanel.add(botoesPanel, gbc);

        tableModel = new DefaultTableModel(new Object[]{"Produto", "Qtd", "Valor Unitário", "Total"}, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAdicionar.addActionListener(e -> adicionarItem());
        btnRemover.addActionListener(e -> removerItem());
        btnFinalizar.addActionListener(e -> finalizarVenda());
    }

    private void carregarComboBox() {
        EntityManager em = JPAUtil.getEntityManager();
        cbItem.removeAllItems();
        cbCliente.removeAllItems();
        cbFormaPagamento.removeAllItems();

        new ItemDao(em).listarTodos().forEach(cbItem::addItem);
        new ClienteDao(em).buscarTodos().forEach(cbCliente::addItem);
        new FormaPagamentoDao(em).buscarTodos().forEach(cbFormaPagamento::addItem);
        em.close();
    }

    private void adicionarItem() {
        Item item = (Item) cbItem.getSelectedItem();
        int qtd = Integer.parseInt(txtQuantidade.getText().trim());
        double valor = item.getValorUnitarioProduto();
        PedidoItem pi = new PedidoItem(item, pedido, qtd, valor);
        carrinho.add(pi);

        tableModel.addRow(new Object[]{item.getNomeProduto(), qtd, valor, qtd * valor});
        atualizarTotal();
        txtQuantidade.setText("");
    }

    private void removerItem() {
        int row = tabela.getSelectedRow();
        if (row >= 0) {
            carrinho.remove(row);
            tableModel.removeRow(row);
            atualizarTotal();
        }
    }

    private void atualizarTotal() {
        double total = carrinho.stream()
                .mapToDouble(i -> i.getQuantidadeItem() * i.getValorItem())
                .sum();
        txtTotalVenda.setText(String.format("%.2f", total));
    }

    private void finalizarVenda() {
        pedido.setCliente((Cliente) cbCliente.getSelectedItem());
        pedido.setFormaPagamento((FormaPagamento) cbFormaPagamento.getSelectedItem());
        pedido.setStatusPedido("Finalizado");
        pedido.setValorTotalPedido(Double.parseDouble(txtTotalVenda.getText().replace(",", ".")));

        EntityManager em = JPAUtil.getEntityManager();
        PedidoDao pedidoDao = new PedidoDao(em);
        PedidoItemDao itemDao = new PedidoItemDao(em);

        em.getTransaction().begin();
        pedidoDao.cadastrar(pedido);
        for (PedidoItem pi : carrinho) {
            pi.setPedido(pedido);
            itemDao.cadastrar(pi);
        }
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Venda finalizada com sucesso!");
        removeAll();
        revalidate();
        repaint();
    }
}

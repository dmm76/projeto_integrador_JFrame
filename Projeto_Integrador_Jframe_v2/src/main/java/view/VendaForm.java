package view;

import dao.ItemDao;
import dao.PedidoDao;
import dao.PedidoItemDao;
import dao.ClienteDao;
import dao.FormaPagamentoDao;
import model.*;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

public class VendaForm extends JPanel {

    private JComboBox<Item> cbItem;
    private JTextField txtQuantidade;
    private JLabel lblTotal;
    private JButton btnAdicionar, btnFinalizar;
    private DefaultTableModel tableModel;
    private JTable tabelaCarrinho;

    private Pedido pedidoAtual;

    public VendaForm() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = gbc.gridy = 0;

        cbItem = new JComboBox<>();
        txtQuantidade = new JTextField(10);
        lblTotal = new JLabel("Total: R$ 0.00");
        btnAdicionar = new JButton("Adicionar à venda");
        btnFinalizar = new JButton("Confirmar Venda");

        carregarComboBoxItens();
        criarPedidoAtual();

        formPanel.add(new JLabel("Produto:"), gbc);
        gbc.gridx++;
        formPanel.add(cbItem, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx++;
        formPanel.add(txtQuantidade, gbc);
        gbc.gridx = 0; gbc.gridy++;

        gbc.gridwidth = 2;
        formPanel.add(btnAdicionar, gbc);
        gbc.gridy++;
        formPanel.add(lblTotal, gbc);
        gbc.gridy++;
        formPanel.add(btnFinalizar, gbc);

        tableModel = new DefaultTableModel(new Object[]{"Produto", "Qtd", "Unit", "Total"}, 0);
        tabelaCarrinho = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaCarrinho);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnAdicionar.addActionListener(this::adicionarItemAoPedido);
        btnFinalizar.addActionListener(this::finalizarVenda);
    }

    private void criarPedidoAtual() {
        EntityManager em = JPAUtil.getEntityManager();
        pedidoAtual = new Pedido(new Date(), "Em Aberto", 0.0,
                new ClienteDao(em).buscarTodos().get(0),
                new FormaPagamentoDao(em).buscarTodos().get(0));

        PedidoDao pedidoDao = new PedidoDao(em);
        em.getTransaction().begin();
        pedidoDao.cadastrar(pedidoAtual);
        em.getTransaction().commit();
        em.close();
    }

    private void carregarComboBoxItens() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Item> itens = new ItemDao(em).listarTodos();
        cbItem.removeAllItems();
        itens.forEach(cbItem::addItem);
        em.close();
    }

    private void adicionarItemAoPedido(ActionEvent e) {
        try {
            Item itemSelecionado = (Item) cbItem.getSelectedItem();
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
            double valorUnitario = itemSelecionado.getValorUnitarioProduto();
            double totalItem = quantidade * valorUnitario;

            PedidoItem pedidoItem = new PedidoItem(itemSelecionado, pedidoAtual, quantidade, valorUnitario);

            EntityManager em = JPAUtil.getEntityManager();
            PedidoItemDao dao = new PedidoItemDao(em);
            em.getTransaction().begin();
            dao.cadastrar(pedidoItem);
            em.getTransaction().commit();
            em.close();

            tableModel.addRow(new Object[]{
                    itemSelecionado.getNomeProduto(),
                    quantidade,
                    String.format("R$ %.2f", valorUnitario),
                    String.format("R$ %.2f", totalItem)
            });

            atualizarTotal();
            txtQuantidade.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar item: " + ex.getMessage());
        }
    }

    private void atualizarTotal() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String valor = tableModel.getValueAt(i, 3).toString().replace("R$", "").trim().replace(",", ".");
            total += Double.parseDouble(valor);
        }
        lblTotal.setText(String.format("Total: R$ %.2f", total));
    }

    private void finalizarVenda(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Venda finalizada com sucesso!\nID Pedido: " + pedidoAtual.getIdPedido());
        setVisible(false);
    }
}

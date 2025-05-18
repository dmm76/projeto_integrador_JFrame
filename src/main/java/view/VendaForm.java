package view;

import dao.*;
import model.*;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static util.EstiloSistema.*;

public class VendaForm extends JPanel {

    private final JComboBox<Item> cbItem = new JComboBox<>();
    private final JComboBox<Cliente> cbCliente = new JComboBox<>();
    private final JComboBox<FormaPagamento> cbFormaPagamento = new JComboBox<>();
    private final JTextField txtQuantidade = new JTextField(10);
    private final JTextField txtValorUnitario = new JTextField(10);
    private final JTextField txtTotalVenda = new JTextField(10);
    private final JTable tabela;
    private final DefaultTableModel tableModel;
    private final JLabel lblPedidoInfo = new JLabel("Pedido em Aberto (ID será gerado ao finalizar)");
    private final JButton btnAdicionar = new JButton("Adicionar à Venda");
    private final JButton btnRemover = new JButton("Remover Item");
    private final JButton btnFinalizar = new JButton("Finalizar Venda");
    private final JButton btnNovoCliente = new JButton("Novo Cliente");

    private final Pedido pedido;
    private final List<PedidoItem> carrinho = new ArrayList<>();

    public VendaForm() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COR_FUNDO);

        EntityManager em = JPAUtil.getEntityManager();
        pedido = new Pedido(new Date(), "Aberto", 0, null, null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int linha = 0;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = linha++;
        aplicarEstiloLabel(lblPedidoInfo);
        lblPedidoInfo.setForeground(COR_TEXTO);
        formPanel.add(lblPedidoInfo, gbc);

        gbc.gridwidth = 1;
        adicionarCampoComBotao(formPanel, gbc, linha++, "Cliente:", cbCliente, btnNovoCliente);
        adicionarCampo(formPanel, gbc, linha++, "Forma de Pagamento:", cbFormaPagamento);
        adicionarCampo(formPanel, gbc, linha++, "Produto:", cbItem);
        adicionarCampo(formPanel, gbc, linha++, "Quantidade:", txtQuantidade);
        adicionarCampo(formPanel, gbc, linha++, "Valor Unitário:", txtValorUnitario);
        adicionarCampo(formPanel, gbc, linha++, "Total Venda:", txtTotalVenda);

        aplicarEstiloCampo(txtQuantidade);
        aplicarEstiloCampo(txtValorUnitario);
        aplicarEstiloCampo(txtTotalVenda);
        txtValorUnitario.setEditable(false);
        txtTotalVenda.setEditable(false);

        carregarComboBox(em);
        em.close();

        cbItem.addActionListener(e -> {
            Item item = (Item) cbItem.getSelectedItem();
            if (item != null && item.getIdItem() != 0)
                txtValorUnitario.setText(String.valueOf(item.getValorUnitarioProduto()));
            else
                txtValorUnitario.setText("");
        });

        btnNovoCliente.addActionListener(e -> abrirCadastroClienteRapido());

        aplicarEstiloBotao(btnAdicionar);
        aplicarEstiloBotao(btnRemover);
        aplicarEstiloBotao(btnFinalizar);
        aplicarEstiloBotao(btnNovoCliente);

        JPanel botoesPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        botoesPanel.setBackground(COR_FUNDO);
        botoesPanel.add(btnAdicionar);
        botoesPanel.add(btnRemover);
        botoesPanel.add(btnFinalizar);

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 2;
        formPanel.add(botoesPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Produto", "Qtd", "Valor Unitário", "Total"}, 0);
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnAdicionar.addActionListener(e -> adicionarItem());
        btnRemover.addActionListener(e -> removerItem());
        btnFinalizar.addActionListener(e -> finalizarVenda());
    }

    private void adicionarCampo(JPanel panel, GridBagConstraints gbc, int linha, String rotulo, JComponent campo) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        JLabel label = new JLabel(rotulo);
        aplicarEstiloLabel(label);
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(campo, gbc);
    }

    private void adicionarCampoComBotao(JPanel panel, GridBagConstraints gbc, int linha, String rotulo, JComponent campo, JButton botao) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        JLabel label = new JLabel(rotulo);
        aplicarEstiloLabel(label);
        panel.add(label, gbc);

        JPanel comboComBotao = new JPanel(new BorderLayout());
        comboComBotao.add(campo, BorderLayout.CENTER);
        comboComBotao.add(botao, BorderLayout.EAST);

        gbc.gridx = 1;
        panel.add(comboComBotao, gbc);
    }

    private void carregarComboBox(EntityManager em) {
        cbItem.removeAllItems();
        cbCliente.removeAllItems();
        cbFormaPagamento.removeAllItems();

        Item vazio = new Item();
        vazio.setNomeProduto("-- Selecione --");
        cbItem.addItem(vazio);
        new ItemDao(em).listarTodos().forEach(cbItem::addItem);

        Cliente vazioC = new Cliente();
        vazioC.setNomeCliente("-- Selecione --");
        cbCliente.addItem(vazioC);
        new ClienteDao(em).buscarTodos().forEach(cbCliente::addItem);

        FormaPagamento vazioF = new FormaPagamento();
        vazioF.setDescricao("-- Selecione --");
        cbFormaPagamento.addItem(vazioF);
        new FormaPagamentoDao(em).buscarTodos().forEach(cbFormaPagamento::addItem);
    }

    private void adicionarItem() {
        try {
            if (cbItem.getSelectedIndex() <= 0 || txtQuantidade.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um produto e informe a quantidade.");
                return;
            }

            Item item = (Item) cbItem.getSelectedItem();
            int qtd = Integer.parseInt(txtQuantidade.getText().trim());
            double valor = item.getValorUnitarioProduto();
            PedidoItem pi = new PedidoItem(item, pedido, qtd, valor);
            carrinho.add(pi);

            tableModel.addRow(new Object[]{item.getNomeProduto(), qtd, valor, qtd * valor});
            atualizarTotal();
            txtQuantidade.setText("");
            cbItem.setSelectedIndex(0);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe uma quantidade válida.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar item: " + e.getMessage());
        }
    }

    private void removerItem() {
        int row = tabela.getSelectedRow();
        if (row >= 0) {
            carrinho.remove(row);
            tableModel.removeRow(row);
            atualizarTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void atualizarTotal() {
        double total = carrinho.stream()
                .mapToDouble(i -> i.getQuantidadeItem() * i.getValorItem())
                .sum();
        txtTotalVenda.setText(String.format("%.2f", total));
    }

    private void finalizarVenda() {
        try {
            if (cbCliente.getSelectedIndex() <= 0 || cbFormaPagamento.getSelectedIndex() <= 0 || carrinho.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios e adicione pelo menos um item.");
                return;
            }

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
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao finalizar venda: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        cbCliente.setSelectedIndex(0);
        cbFormaPagamento.setSelectedIndex(0);
        cbItem.setSelectedIndex(0);
        txtQuantidade.setText("");
        txtValorUnitario.setText("");
        txtTotalVenda.setText("");
        tableModel.setRowCount(0);
        carrinho.clear();
        lblPedidoInfo.setText("Pedido em Aberto (ID será gerado ao finalizar)");
    }

    private void abrirCadastroClienteRapido() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Novo Cliente", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(new ClienteForm((Void v) -> {
            EntityManager em = JPAUtil.getEntityManager();
            carregarComboBox(em); // recarrega os dados após salvar cliente
            em.close();
        }));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}

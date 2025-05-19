// Refatorado ItemPedidoForm com padrão de estilo e layout consistente
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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static util.EstiloSistema.*;

public class ItemPedidoForm extends JPanel {
    private final JComboBox<Pedido> cbPedido = new JComboBox<>();
    private final JComboBox<Item> cbItem = new JComboBox<>();
    private final JTextField txtQuantidade = new JTextField(10);
    private final JTextField txtValor = new JTextField(10);
    private final JTextField txtValorTotal = new JTextField(10);
    private final JTable tabela;
    private final DefaultTableModel tableModel;

    private final JButton btnCadastrar = new JButton("Cadastrar");
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnAlterar = new JButton("Alterar");
    private final JButton btnRemover = new JButton("Remover");
    private final JButton btnLimpar = new JButton("Limpar");

    private final Pedido pedidoFiltrado;

    public ItemPedidoForm() {
        this(null);
    }

    public ItemPedidoForm(Pedido pedidoFiltrado) {
        this.pedidoFiltrado = pedidoFiltrado;
        setLayout(new BorderLayout(10, 10));
        setBackground(COR_FUNDO);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int linha = 0;
        adicionarCampo(formPanel, gbc, linha++, "Pedido:", cbPedido);
        adicionarCampo(formPanel, gbc, linha++, "Produto:", cbItem);
        adicionarCampo(formPanel, gbc, linha++, "Quantidade:", txtQuantidade);
        adicionarCampo(formPanel, gbc, linha++, "Valor Unitário:", txtValor);
        adicionarCampo(formPanel, gbc, linha, "Valor Total (Banco):", txtValorTotal);

        aplicarEstiloCampo(txtQuantidade);
        aplicarEstiloCampo(txtValor);
        aplicarEstiloCampo(txtValorTotal);
        txtValorTotal.setEditable(false);

        aplicarEstiloBotao(btnCadastrar);
        aplicarEstiloBotao(btnBuscar);
        aplicarEstiloBotao(btnAlterar);
        aplicarEstiloBotao(btnRemover);
        aplicarEstiloBotao(btnLimpar);

        Dimension buttonSize = new Dimension(130, 30);
        btnCadastrar.setPreferredSize(buttonSize);
        btnBuscar.setPreferredSize(buttonSize);
        btnAlterar.setPreferredSize(buttonSize);
        btnRemover.setPreferredSize(buttonSize);
        btnLimpar.setPreferredSize(buttonSize);

        cbItem.addActionListener(e -> atualizarValorUnitario());

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
        botoesPanel.add(btnLimpar);
        formPanel.add(botoesPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID Item Pedido", "Produto", "Quantidade", "Valor Unitário", "Valor Total", "Pedido"}, 0);
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> salvarItemPedido());
        btnBuscar.addActionListener(e -> buscarItensPorPedido());
        btnAlterar.addActionListener(e -> alterarItemPedido());
        btnRemover.addActionListener(e -> removerItemPedido());
        btnLimpar.addActionListener(e -> {
            limparCampos();
            carregarItemPedidos(); // volta para todos os pedidos
            btnCadastrar.setEnabled(true);
        });


        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCamposComSelecionado();
                btnCadastrar.setEnabled(false);
            }
        });

        carregarComboBox();

        if (pedidoFiltrado != null) {
            cbPedido.setSelectedItem(pedidoFiltrado);
            carregarItensDoPedido(pedidoFiltrado);
        } else {
            carregarItemPedidos();
        }
    }

    private void adicionarCampo(JPanel panel, GridBagConstraints gbc, int linha, String rotulo, JComponent campo) {
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
        try {
            if (cbPedido.getSelectedIndex() <= 0 ||
                    cbItem.getSelectedIndex() <= 0 ||
                    txtQuantidade.getText().trim().isEmpty() ||
                    txtValor.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
                return;
            }

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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar item: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void buscarItensPorPedido() {
        String input = JOptionPane.showInputDialog(this, "Informe o ID do Pedido para buscar os itens:");
        if (input == null || input.isBlank()) return;

        try {
            int id = Integer.parseInt(input);
            EntityManager em = JPAUtil.getEntityManager();
            Pedido pedido = new PedidoDao(em).buscarPorID(id);
            if (pedido == null) {
                JOptionPane.showMessageDialog(this, "Pedido não encontrado.");
                return;
            }
            carregarItensDoPedido(pedido);
            em.close();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido. Digite um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarItensDoPedido(Pedido pedido) {
        EntityManager em = JPAUtil.getEntityManager();
        List<PedidoItem> itens = new PedidoItemDao(em).buscarPorPedido(pedido);
        tableModel.setRowCount(0);
        for (PedidoItem pi : itens) {
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

        try {
            if (cbPedido.getSelectedIndex() <= 0 ||
                    cbItem.getSelectedIndex() <= 0 ||
                    txtQuantidade.getText().trim().isEmpty() ||
                    txtValor.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void removerItemPedido() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item do pedido para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover este item?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

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

    private void preencherCamposComSelecionado() {
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
        tabela.clearSelection();
        btnCadastrar.setEnabled(true);
    }
}

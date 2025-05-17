package view;

import dao.ClienteDao;
import dao.FormaPagamentoDao;
import dao.PedidoDao;
import dao.PedidoItemDao;
import model.Cliente;
import model.FormaPagamento;
import model.Pedido;
import util.JPAUtil;
import model.PedidoItem;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PedidoForm extends JPanel {

    private JTextField txtData, txtStatus, txtValor;
    private JComboBox<Cliente> cbCliente;
    private JComboBox<FormaPagamento> cbFormaPagamento;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnCadastrar, btnBuscar, btnAlterar, btnRemover, btnRelatorio;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public PedidoForm() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = gbc.gridy = 0;

        txtData = new JTextField(20);
        txtStatus = new JTextField(20);
        txtValor = new JTextField(10);
        cbCliente = new JComboBox<>();
        cbFormaPagamento = new JComboBox<>();

        carregarComboBox();

        btnCadastrar = new JButton("Cadastrar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnRemover = new JButton("Remover");
        btnRelatorio = new JButton("Relatório");

        formPanel.add(new JLabel("Data (dd-MM-yyyy):"), gbc);
        gbc.gridx++;
        formPanel.add(txtData, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx++;
        formPanel.add(txtStatus, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Valor Total:"), gbc);
        gbc.gridx++;
        formPanel.add(txtValor, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx++;
        formPanel.add(cbCliente, gbc);
        gbc.gridx = 0; gbc.gridy++;

        formPanel.add(new JLabel("Forma de Pagamento:"), gbc);
        gbc.gridx++;
        formPanel.add(cbFormaPagamento, gbc);
        gbc.gridx = 0; gbc.gridy++;

        gbc.gridwidth = 2;
        JPanel botoesPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);
        botoesPanel.add(btnRelatorio);
        formPanel.add(botoesPanel, gbc);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Data", "Status", "Valor", "Cliente", "Pagamento"}, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> salvarPedido());
        btnBuscar.addActionListener(e -> carregarPedidos());

        btnAlterar.addActionListener(e -> alterarPedido());
        btnRemover.addActionListener(e -> removerPedido());
        btnRelatorio.addActionListener(e -> gerarRelatorioPedidos());

        tabela.getSelectionModel().addListSelectionListener(this::preencherCamposComSelecionado);

        carregarPedidos();
    }

    private void carregarComboBox() {
        EntityManager em = JPAUtil.getEntityManager();
        cbCliente.removeAllItems();
        cbFormaPagamento.removeAllItems();

        Cliente clienteVazio = new Cliente();
        clienteVazio.setNomeCliente("-- Selecione --");
        cbCliente.addItem(clienteVazio);

        FormaPagamento pagamentoVazio = new FormaPagamento();
        pagamentoVazio.setDescricao("-- Selecione --");
        cbFormaPagamento.addItem(pagamentoVazio);

        new ClienteDao(em).buscarTodos().forEach(cbCliente::addItem);
        new FormaPagamentoDao(em).buscarTodos().forEach(cbFormaPagamento::addItem);

        em.close();
    }

    private void salvarPedido() {
        try {
            // Verificações de campos obrigatórios
            if (txtData.getText().trim().isEmpty() ||
                    txtStatus.getText().trim().isEmpty() ||
                    txtValor.getText().trim().isEmpty() ||
                    cbCliente.getSelectedIndex() == -1 ||
                    cbFormaPagamento.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
                return;
            }

            Date data = sdf.parse(txtData.getText().trim());
            String status = txtStatus.getText().trim();
            double valor = Double.parseDouble(txtValor.getText().trim());

            Cliente cliente = (Cliente) cbCliente.getSelectedItem();
            FormaPagamento pagamento = (FormaPagamento) cbFormaPagamento.getSelectedItem();

            EntityManager em = JPAUtil.getEntityManager();
            PedidoDao dao = new PedidoDao(em);

            Pedido pedido = new Pedido(data, status, valor, cliente, pagamento);
            em.getTransaction().begin();
            dao.cadastrar(pedido);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Pedido cadastrado com sucesso!");
            limparCampos();
            carregarPedidos();

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd-MM-yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor total deve ser numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    private void carregarPedidos() {
        tableModel.setRowCount(0);
        EntityManager em = JPAUtil.getEntityManager();
        List<Pedido> pedidos = new PedidoDao(em).buscarTodos();
        for (Pedido p : pedidos) {
            tableModel.addRow(new Object[]{
                    p.getIdPedido(),
                    sdf.format(p.getDataPedido()),
                    p.getStatusPedido(),
                    p.getValorTotalPedido(),
                    p.getCliente().getNomeCliente(),
                    p.getFormaPagamento().getDescricao()
            });
        }
        em.close();
    }

    private void alterarPedido() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um pedido para alterar.");
            return;
        }
        if (txtData.getText().trim().isEmpty() ||
                txtStatus.getText().trim().isEmpty() ||
                txtValor.getText().trim().isEmpty() ||
                cbCliente.getSelectedIndex() == -1 ||
                cbFormaPagamento.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
            return;
        }


        try {
            int id = (int) tableModel.getValueAt(row, 0);

            EntityManager em = JPAUtil.getEntityManager();
            PedidoDao dao = new PedidoDao(em);
            Pedido pedido = dao.buscarPorID(id);

            pedido.setDataPedido(sdf.parse(txtData.getText().trim()));
            pedido.setStatusPedido(txtStatus.getText().trim());
            pedido.setValorTotalPedido(Double.parseDouble(txtValor.getText().trim()));
            pedido.setCliente((Cliente) cbCliente.getSelectedItem());
            pedido.setFormaPagamento((FormaPagamento) cbFormaPagamento.getSelectedItem());

            em.getTransaction().begin();
            dao.alterar(pedido);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Pedido atualizado com sucesso!");
            limparCampos();
            carregarPedidos();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd-MM-yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor total deve ser numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar o pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void removerPedido() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um pedido para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover este pedido?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) tableModel.getValueAt(row, 0);
        EntityManager em = JPAUtil.getEntityManager();
        PedidoDao pedidoDao = new PedidoDao(em);
        PedidoItemDao itemDao = new PedidoItemDao(em);

        try {
            Pedido pedido = pedidoDao.buscarPorID(id);

            em.getTransaction().begin();

            // Remove os itens vinculados a este pedido
            List<PedidoItem> itens = em.createQuery(
                            "SELECT i FROM PedidoItem i WHERE i.pedido = :pedido", PedidoItem.class)
                    .setParameter("pedido", pedido)
                    .getResultList();

            for (PedidoItem item : itens) {
                itemDao.remover(item);
            }

            // Agora remove o pedido
            pedidoDao.remover(pedido);

            em.getTransaction().commit();

            JOptionPane.showMessageDialog(this, "Pedido e itens vinculados removidos com sucesso!");
            limparCampos();
            carregarPedidos();
        } catch (Exception e) {
            em.getTransaction().rollback();
            JOptionPane.showMessageDialog(this, "Erro ao remover pedido: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private void preencherCamposComSelecionado(ListSelectionEvent e) {
        int row = tabela.getSelectedRow();
        if (row != -1) {
            txtData.setText((String) tableModel.getValueAt(row, 1));
            txtStatus.setText((String) tableModel.getValueAt(row, 2));
            txtValor.setText(tableModel.getValueAt(row, 3).toString());

            String nomeCliente = (String) tableModel.getValueAt(row, 4);
            String descPagamento = (String) tableModel.getValueAt(row, 5);

            for (int i = 0; i < cbCliente.getItemCount(); i++) {
                if (cbCliente.getItemAt(i).getNomeCliente().equals(nomeCliente)) {
                    cbCliente.setSelectedIndex(i);
                    break;
                }
            }

            for (int i = 0; i < cbFormaPagamento.getItemCount(); i++) {
                if (cbFormaPagamento.getItemAt(i).getDescricao().equals(descPagamento)) {
                    cbFormaPagamento.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void limparCampos() {
        txtData.setText("");
        txtStatus.setText("");
        txtValor.setText("");
        cbCliente.setSelectedIndex(0);
        cbFormaPagamento.setSelectedIndex(0);
    }
    private void gerarRelatorioPedidos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório de Pedidos");
        fileChooser.setSelectedFile(new java.io.File("relatorio_pedidos.txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) return;

        java.io.File file = fileChooser.getSelectedFile();

        try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
            writer.println("RELATÓRIO DE PEDIDOS");
            writer.println("---------------------");

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.println("ID: " + tableModel.getValueAt(i, 0));
                writer.println("Data: " + tableModel.getValueAt(i, 1));
                writer.println("Status: " + tableModel.getValueAt(i, 2));
                writer.println("Valor: R$ " + tableModel.getValueAt(i, 3));
                writer.println("Cliente: " + tableModel.getValueAt(i, 4));
                writer.println("Forma de Pagamento: " + tableModel.getValueAt(i, 5));
                writer.println("---------------------------");
            }

            JOptionPane.showMessageDialog(this, "Relatório gerado com sucesso em:\n" + file.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

}

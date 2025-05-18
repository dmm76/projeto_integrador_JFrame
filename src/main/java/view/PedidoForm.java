package view;

import dao.ClienteDao;
import dao.FormaPagamentoDao;
import dao.PedidoDao;
import dao.PedidoItemDao;
import model.Cliente;
import model.FormaPagamento;
import model.Pedido;
import model.PedidoItem;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static util.EstiloSistema.*;

public class PedidoForm extends JPanel {
    private final JTextField txtData = new JTextField(20);
    private final JTextField txtStatus = new JTextField(20);
    private final JTextField txtValor = new JTextField(10);
    private final JComboBox<Cliente> cbCliente = new JComboBox<>();
    private final JComboBox<FormaPagamento> cbFormaPagamento = new JComboBox<>();
    private final JTable tabela;
    private final DefaultTableModel tableModel;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public PedidoForm() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COR_FUNDO);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int linha = 0;

        adicionarCampo(formPanel, gbc, linha++, "Data (dd-MM-yyyy):", txtData);
        adicionarCampo(formPanel, gbc, linha++, "Status:", txtStatus);
        adicionarCampo(formPanel, gbc, linha++, "Valor Total:", txtValor);
        adicionarCampo(formPanel, gbc, linha++, "Cliente:", cbCliente);
        adicionarCampo(formPanel, gbc, linha++, "Forma de Pagamento:", cbFormaPagamento);

        aplicarEstiloCampo(txtData);
        aplicarEstiloCampo(txtStatus);
        aplicarEstiloCampo(txtValor);

        carregarComboBox();

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnAlterar = new JButton("Alterar");
        JButton btnRemover = new JButton("Remover");
        JButton btnRelatorio = new JButton("Relatório");

        aplicarEstiloBotao(btnCadastrar);
        aplicarEstiloBotao(btnBuscar);
        aplicarEstiloBotao(btnAlterar);
        aplicarEstiloBotao(btnRemover);
        aplicarEstiloBotao(btnRelatorio);

        JPanel botoesPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        botoesPanel.setBackground(COR_FUNDO);
        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);
        botoesPanel.add(btnRelatorio);

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 2;
        formPanel.add(botoesPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Data", "Status", "Valor", "Cliente", "Pagamento"}, 0);
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> salvarPedido());
        btnAlterar.addActionListener(e -> alterarPedido());
        btnRemover.addActionListener(e -> removerPedido());
        btnRelatorio.addActionListener(e -> gerarRelatorioPedidos());
        tabela.getSelectionModel().addListSelectionListener(e -> preencherCamposComSelecionado());
        btnBuscar.addActionListener(e -> {
            int selectedRow = tabela.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um pedido para visualizar os itens.");
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);
            EntityManager em = JPAUtil.getEntityManager();
            Pedido pedido = new PedidoDao(em).buscarPorID(id);
            em.close();

            // Obtém a janela pai (MainFrame)
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof MainFrame mainFrame) {
                mainFrame.abrirTela(new ItemPedidoForm(pedido));
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível abrir a tela dentro do sistema.");
            }
        });

        txtData.setText(sdf.format(new Date()));
        carregarPedidos();
    }

    private void adicionarCampo(JPanel panel, GridBagConstraints gbc, int linha, String rotulo, JComponent campo) {
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
            if (txtData.getText().trim().isEmpty() ||
                    txtStatus.getText().trim().isEmpty() ||
                    txtValor.getText().trim().isEmpty() ||
                    cbCliente.getSelectedIndex() <= 0 ||
                    cbFormaPagamento.getSelectedIndex() <= 0) {
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

            List<PedidoItem> itens = em.createQuery(
                            "SELECT i FROM PedidoItem i WHERE i.pedido = :pedido", PedidoItem.class)
                    .setParameter("pedido", pedido)
                    .getResultList();

            for (PedidoItem item : itens) {
                itemDao.remover(item);
            }

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

    private void gerarRelatorioPedidos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório de Pedidos");
        fileChooser.setSelectedFile(new File("relatorio_pedidos.txt"));

        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fileChooser.getSelectedFile();

        try (PrintWriter writer = new PrintWriter(file)) {
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

    private void preencherCamposComSelecionado() {
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
        txtData.setText(sdf.format(new Date()));
        txtStatus.setText("");
        txtValor.setText("");
        cbCliente.setSelectedIndex(0);
        cbFormaPagamento.setSelectedIndex(0);
    }
}

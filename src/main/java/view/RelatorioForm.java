package view;

import dao.PedidoDao;
import dao.PedidoItemDao;
import model.Pedido;
import model.PedidoItem;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RelatorioForm extends JPanel {

    private JComboBox<String> cbTipoRelatorio;
    private JButton btnGerar;

    public RelatorioForm() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        cbTipoRelatorio = new JComboBox<>(new String[]{
                "Selecione...",
                "Vendas por Período",
                "Produtos mais vendidos"
        });

        btnGerar = new JButton("Gerar Relatório");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Tipo de Relatório:"), gbc);

        gbc.gridx = 1;
        add(cbTipoRelatorio, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(btnGerar, gbc);

        btnGerar.addActionListener(this::gerarRelatorio);
    }

    private void gerarRelatorio(ActionEvent e) {
        String tipo = (String) cbTipoRelatorio.getSelectedItem();
        if (tipo == null || tipo.equals("Selecione...")) {
            JOptionPane.showMessageDialog(this, "Selecione um tipo de relatório.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Salvar Relatório");
        int escolha = chooser.showSaveDialog(this);
        if (escolha != JFileChooser.APPROVE_OPTION) return;

        File arquivo = chooser.getSelectedFile();

        // Garante que o arquivo tenha extensão .txt
        if (!arquivo.getName().toLowerCase().endsWith(".txt")) {
            arquivo = new File(arquivo.getAbsolutePath() + ".txt");
        }

        try (FileWriter writer = new FileWriter(arquivo)) {
            if (tipo.equals("Vendas por Período")) {
                gerarRelatorioVendas(writer);
            } else if (tipo.equals("Produtos mais vendidos")) {
                gerarRelatorioProdutos(writer);
            }
            JOptionPane.showMessageDialog(this, "Relatório gerado com sucesso!");

            // Abre o arquivo automaticamente no programa padrão
            Desktop.getDesktop().open(arquivo);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gerarRelatorioVendas(FileWriter writer) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        List<Pedido> pedidos = new PedidoDao(em).buscarTodos();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        writer.write(String.format("%-10s %-15s %-20s %-10s%n", "ID", "Data", "Cliente", "Total"));
        writer.write("=".repeat(55) + "\n");

        for (Pedido p : pedidos) {
            writer.write(String.format("%-10d %-15s %-20s R$ %-10.2f%n",
                    p.getIdPedido(),
                    sdf.format(p.getDataPedido()),
                    p.getCliente().getNomeCliente(),
                    p.getValorTotalPedido()));
        }

        em.close();
    }

    private void gerarRelatorioProdutos(FileWriter writer) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        List<Object[]> resultados = em.createQuery(
                "SELECT pi.item.nomeProduto, SUM(pi.quantidadeItem) " +
                        "FROM PedidoItem pi " +
                        "GROUP BY pi.item.nomeProduto " +
                        "ORDER BY SUM(pi.quantidadeItem) DESC"
        ).getResultList();

        writer.write(String.format("%-30s %10s%n", "Produto", "Quantidade"));
        writer.write("=".repeat(45) + "\n");

        for (Object[] linha : resultados) {
            writer.write(String.format("%-30s %10d%n", (String) linha[0], (Long) linha[1]));
        }

        em.close();
    }
}

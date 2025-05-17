package view;

import javax.swing.*;
import java.awt.*;
import view.MarcaForm;
import view.CategoriaForm;

public class MainFrame extends JFrame {

    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Sistema de Bar e Restaurante");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Painel central onde as telas serão encaixadas
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        // Cria a barra de menu
        JMenuBar menuBar = new JMenuBar();

        // Menus principais
        JMenu menuCadastro = new JMenu("Cadastro");
        JMenu menuPedido = new JMenu("Pedido");
        JMenu menuSair = new JMenu("Sistema");

        // Itens do menu Cadastro
        JMenuItem itemCliente = new JMenuItem("Cliente");
        JMenuItem itemFornecedor = new JMenuItem("Fornecedor");
        JMenuItem itemProduto = new JMenuItem("Produto");
        JMenuItem itemCategoria = new JMenuItem("Categoria");
        JMenuItem itemMarca = new JMenuItem("Marca");
        JMenuItem itemFormaPagamento = new JMenuItem("Forma de Pagamento");

        // Itens do menu Pedido
        JMenuItem itemNovoPedido = new JMenuItem("Novo Pedido");
        JMenuItem itemItensPedido = new JMenuItem("Itens do Pedido");

        // Item sair
        JMenuItem itemRelatorio = new JMenuItem("Relatório");
        JMenuItem itemSair = new JMenuItem("Sair");

        // Adiciona itens aos menus
        menuCadastro.add(itemCategoria);
        menuCadastro.add(itemCliente);
        menuCadastro.add(itemFormaPagamento);
        menuCadastro.add(itemFornecedor);
        menuCadastro.add(itemMarca);
        menuCadastro.add(itemProduto);

        menuPedido.add(itemNovoPedido);
        menuPedido.add(itemItensPedido);

        menuSair.add(itemRelatorio);
        menuSair.add(itemSair);

        // Adiciona menus à barra
        menuBar.add(menuCadastro);
        menuBar.add(menuPedido);
        menuBar.add(menuSair);

        // Define a barra no frame
        setJMenuBar(menuBar);

        // Ações de exemplo (você substituirá por formulários reais)
        //itemCliente.addActionListener(e -> showMessage("Abrir tela de Cliente"));
        itemRelatorio.addActionListener(e -> showMessage("Abrir tela de Relatórios"));
        //itemProduto.addActionListener(e -> showMessage("Abrir tela de Produto"));
        //itemFormaPagamento.addActionListener(e -> showMessage("Abrir tela de Forma de Pagamento"));
        itemNovoPedido.addActionListener(e -> showMessage("Abrir tela de Novo Pedido"));
        itemItensPedido.addActionListener(e -> showMessage("Abrir tela de Itens do Pedido"));
        itemSair.addActionListener(e -> System.exit(0));

        // Ações com formulários reais
        itemMarca.addActionListener(e -> abrirTela(new MarcaForm()));
        itemCategoria.addActionListener(e -> abrirTela(new CategoriaForm()));
        itemFormaPagamento.addActionListener(e -> abrirTela(new FormaPagamentoForm()));
        itemFornecedor.addActionListener(e -> abrirTela(new FornecedorForm()));
        itemCliente.addActionListener(e -> abrirTela(new ClienteForm()));
        itemProduto.addActionListener(e -> abrirTela(new ItemForm()));

        setVisible(true);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void abrirTela(JPanel tela) {
        contentPanel.removeAll();
        contentPanel.add(tela, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}

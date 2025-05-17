package view;

import javax.swing.*;
import java.awt.*;

import dao.PedidoItemDao;
import model.UsuarioSistema;
import view.MarcaForm;
import view.CategoriaForm;

public class MainFrame extends JFrame {
    private UsuarioSistema usuarioLogado;
    private JPanel contentPanel;

    public MainFrame(UsuarioSistema usuarioLogado) {

        this.usuarioLogado = usuarioLogado;

        setTitle("Sistema de Bar e Restaurante");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Painel central onde as telas serão encaixadas
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 240, 240)); // Cinza claro
        add(contentPanel, BorderLayout.CENTER);

        // Cria a barra de menu
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(30, 50, 80)); // Cinza escuro

        // Fonte personalizada
        Font fonteMenu = new Font("Arial", Font.BOLD, 14);

        // Menus principais
        JMenu menuCadastro = new JMenu("Cadastro");
        JMenu menuPedido = new JMenu("Pedido");
        JMenu menuSair = new JMenu("Sistema");

        menuCadastro.setForeground(Color.WHITE);
        menuPedido.setForeground(Color.WHITE);
        menuSair.setForeground(Color.WHITE);

        menuCadastro.setFont(fonteMenu);
        menuPedido.setFont(fonteMenu);
        menuSair.setFont(fonteMenu);

        // Ícones do menu
        JMenuItem itemCliente = new JMenuItem("Cliente", new ImageIcon("icons/cliente.png"));
        JMenuItem itemFornecedor = new JMenuItem("Fornecedor", new ImageIcon("icons/fornecedor.png"));
        JMenuItem itemProduto = new JMenuItem("Produto", new ImageIcon("icons/produto.png"));
        JMenuItem itemCategoria = new JMenuItem("Categoria", new ImageIcon("icons/categoria.png"));
        JMenuItem itemMarca = new JMenuItem("Marca", new ImageIcon("icons/marca.png"));
        JMenuItem itemFormaPagamento = new JMenuItem("Forma de Pagamento", new ImageIcon("icons/pagamento.png"));

        JMenuItem itemNovoPedido = new JMenuItem("Novo Pedido", new ImageIcon("icons/pedido.png"));
        JMenuItem itemItensPedido = new JMenuItem("Itens do Pedido", new ImageIcon("icons/itens.png"));
        JMenuItem itemVendas = new JMenuItem("PDV Vendas", new ImageIcon("icons/vendas.png"));

        JMenuItem itemRelatorio = new JMenuItem("Relatório", new ImageIcon("icons/relatorio.png"));
        JMenuItem itemCadastroUsuario = new JMenuItem("Gerenciar Usuários", new ImageIcon("icons/usuarios.png"));
        JMenuItem itemSair = new JMenuItem("Sair", new ImageIcon("icons/sair.png"));

        // Adiciona itens aos menus
        menuCadastro.add(itemCategoria);
        menuCadastro.add(itemCliente);
        menuCadastro.add(itemFormaPagamento);
        menuCadastro.add(itemFornecedor);
        menuCadastro.add(itemMarca);
        menuCadastro.add(itemProduto);

        menuPedido.add(itemNovoPedido);
        menuPedido.add(itemItensPedido);
        menuPedido.add(itemVendas);

        menuSair.add(itemRelatorio);
        menuSair.add(itemCadastroUsuario);
        menuSair.add(itemSair);

        // Adiciona menus à barra
        menuBar.add(menuCadastro);
        menuBar.add(menuPedido);
        menuBar.add(menuSair);

        // Define a barra no frame
        setJMenuBar(menuBar);

        // Ações
        itemRelatorio.addActionListener(e -> showMessage("Abrir tela de Relatórios"));
        itemSair.addActionListener(e -> System.exit(0));

        itemMarca.addActionListener(e -> abrirTela(new MarcaForm()));
        itemCategoria.addActionListener(e -> abrirTela(new CategoriaForm()));
        itemFormaPagamento.addActionListener(e -> abrirTela(new FormaPagamentoForm()));
        itemFornecedor.addActionListener(e -> abrirTela(new FornecedorForm()));
        itemCliente.addActionListener(e -> abrirTela(new ClienteForm()));
        itemProduto.addActionListener(e -> abrirTela(new ItemForm()));
        itemNovoPedido.addActionListener(e -> abrirTela(new PedidoForm()));
        itemItensPedido.addActionListener(e -> abrirTela(new ItemPedidoForm()));
        itemVendas.addActionListener(e -> abrirTela(new VendaForm()));
        itemCadastroUsuario.addActionListener(e -> abrirTela(new UsuarioSistemaForm()));

        if (!usuarioLogado.getPerfilUsuario().equalsIgnoreCase("admin")) {
            menuCadastro.setEnabled(false);
            itemRelatorio.setEnabled(false);
            itemFormaPagamento.setEnabled(false);
            itemMarca.setEnabled(false);
            itemFornecedor.setEnabled(false);
            itemProduto.setEnabled(false);
            itemItensPedido.setEnabled(false);
            itemCadastroUsuario.setEnabled(false); // << FALTAVA ISSO
        }

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
}
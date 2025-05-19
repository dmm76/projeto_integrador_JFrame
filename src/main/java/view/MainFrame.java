package view;

import static util.EstiloSistema.*;

import javax.swing.*;
import java.awt.*;
import model.UsuarioSistema;
import util.ImagePanelComOpacidade;

public class MainFrame extends JFrame {
    private UsuarioSistema usuarioLogado;
    private JPanel contentPanel;

    public MainFrame(UsuarioSistema usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        setTitle("Sistema de Bar e Restaurante");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Painel com imagem centralizada e opacidade
        contentPanel = new ImagePanelComOpacidade("src/main/java/util/images/BR_Sistema_LOGO2.png");
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(COR_FUNDO);
        add(contentPanel, BorderLayout.CENTER);

        // Barra de menu
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(COR_MENU);

        JMenu menuCadastro = new JMenu("Cadastro");
        JMenu menuPedido = new JMenu("Pedido");
        JMenu menuSair = new JMenu("Sistema");

        aplicarEstiloMenu(menuCadastro);
        aplicarEstiloMenu(menuPedido);
        aplicarEstiloMenu(menuSair);

        // Itens de menu
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
        JMenuItem itemLogoff = new JMenuItem("Trocar Usuário", new ImageIcon("icons/logoff.png"));
        JMenuItem itemSair = new JMenuItem("Sair", new ImageIcon("icons/sair.png"));

        // Padronizar todos os itens de menu
        aplicarEstiloMenuItem(itemCliente);
        aplicarEstiloMenuItem(itemFornecedor);
        aplicarEstiloMenuItem(itemProduto);
        aplicarEstiloMenuItem(itemCategoria);
        aplicarEstiloMenuItem(itemMarca);
        aplicarEstiloMenuItem(itemFormaPagamento);
        aplicarEstiloMenuItem(itemNovoPedido);
        aplicarEstiloMenuItem(itemItensPedido);
        aplicarEstiloMenuItem(itemVendas);
        aplicarEstiloMenuItem(itemRelatorio);
        aplicarEstiloMenuItem(itemCadastroUsuario);
        aplicarEstiloMenuItem(itemLogoff);
        aplicarEstiloMenuItem(itemSair);

        // Adicionar aos menus
        menuCadastro.add(itemCategoria);
        menuCadastro.add(itemCliente);
        menuCadastro.add(itemFormaPagamento);
        menuCadastro.add(itemFornecedor);
        menuCadastro.add(itemMarca);
        menuCadastro.add(itemProduto);

        menuPedido.add(itemNovoPedido);
        menuPedido.add(itemItensPedido);
        menuPedido.add(itemVendas);

        menuSair.add(itemCadastroUsuario);
        menuSair.add(itemRelatorio);
        menuSair.add(itemLogoff);
        menuSair.add(itemSair);

        // Adicionar menus à barra
        menuBar.add(menuCadastro);
        menuBar.add(menuPedido);
        menuBar.add(menuSair);
        setJMenuBar(menuBar);

        //Adiciono o nome do usuario logado ao menu
        JLabel usuarioLabel = new JLabel("Usuário: " + usuarioLogado.getNomeUsuario());
        aplicarEstiloLabel(usuarioLabel); // já deixa com fonte e cor padrão
        usuarioLabel.setForeground(Color.WHITE); // garante visibilidade no fundo escuro

        menuBar.add(Box.createHorizontalGlue()); // empurra para a direita
        menuBar.add(usuarioLabel);

        // Ações
        itemRelatorio.addActionListener(e -> abrirTela(new RelatorioForm()));
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
        itemLogoff.addActionListener(e -> {
            dispose(); // fecha a janela atual
            new LoginForm().setVisible(true); // abre nova tela de login
        });

        // Permissões
        if (!usuarioLogado.getPerfilUsuario().equalsIgnoreCase("admin")) {
            menuCadastro.setEnabled(false);
            itemRelatorio.setEnabled(false);
            itemFormaPagamento.setEnabled(false);
            itemMarca.setEnabled(false);
            itemFornecedor.setEnabled(false);
            itemProduto.setEnabled(false);
            itemItensPedido.setEnabled(false);
            itemCadastroUsuario.setEnabled(false);
        }

        setVisible(true);
    }

    public void abrirTela(JPanel tela) {
        contentPanel.removeAll();
        contentPanel.add(tela, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
//    }
}

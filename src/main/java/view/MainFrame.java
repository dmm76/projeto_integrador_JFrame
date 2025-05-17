package view;

import static util.EstiloSistema.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import model.UsuarioSistema;

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
        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage imgOriginal = ImageIO.read(new File("src/main/java/util/images/BR_Sistema_LOGO2.png"));
                    int larguraDesejada = 300;
                    int alturaDesejada = 300;
                    Image imagemReduzida = imgOriginal.getScaledInstance(larguraDesejada, alturaDesejada, Image.SCALE_SMOOTH);

                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

                    int x = (getWidth() - larguraDesejada) / 2;
                    int y = (getHeight() - alturaDesejada) / 2;
                    g2d.drawImage(imagemReduzida, x, y, this);
                    g2d.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
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

        menuSair.add(itemRelatorio);
        menuSair.add(itemCadastroUsuario);
        menuSair.add(itemSair);

        // Adicionar menus à barra
        menuBar.add(menuCadastro);
        menuBar.add(menuPedido);
        menuBar.add(menuSair);
        setJMenuBar(menuBar);

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

    private void abrirTela(JPanel tela) {
        contentPanel.removeAll();
        contentPanel.add(tela, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}

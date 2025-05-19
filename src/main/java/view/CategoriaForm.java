package view;

import dao.CategoriaDao;
import model.Categoria;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static util.EstiloSistema.*;

public class CategoriaForm extends JPanel {

    private final JTextField txtDescricao = new JTextField(15);
    private final JTable tabela;
    private final DefaultTableModel tableModel;
    private final JButton btnCadastrar = new JButton("Cadastrar");
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnAlterar = new JButton("Alterar");
    private final JButton btnRemover = new JButton("Remover");
    private final JButton btnLimpar = new JButton("Limpar");

    public CategoriaForm() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COR_FUNDO);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        aplicarEstiloCampo(txtDescricao);
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

        JLabel lblDescricao = new JLabel("Descrição");
        aplicarEstiloLabel(lblDescricao);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblDescricao, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtDescricao, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        formPanel.add(btnCadastrar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        JPanel botoesPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        botoesPanel.setBackground(COR_FUNDO);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);
        botoesPanel.add(btnLimpar);
        formPanel.add(botoesPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Descrição"}, 0);
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> salvarCategoria());
        btnBuscar.addActionListener(e -> carregarCategorias());
        btnAlterar.addActionListener(e -> alterarCategoria());
        btnRemover.addActionListener(e -> removerCategoria());
        btnLimpar.addActionListener(e -> {
            limparCampos();
            btnCadastrar.setEnabled(true);
        });

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCamposComSelecionado();
                btnCadastrar.setEnabled(false);
            }
        });

        carregarCategorias();
    }

    private void salvarCategoria() {
        String descricao = txtDescricao.getText().trim();
        if (descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Descrição não pode estar vazia.");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        CategoriaDao dao = new CategoriaDao(em);

        Categoria categoria = new Categoria(descricao);
        em.getTransaction().begin();
        dao.cadastrar(categoria);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Categoria cadastrada com sucesso!");
        limparCampos();
        carregarCategorias();
    }

    private void carregarCategorias() {
        tableModel.setRowCount(0);
        EntityManager em = JPAUtil.getEntityManager();
        CategoriaDao dao = new CategoriaDao(em);
        List<Categoria> categorias = dao.buscarTodos();
        for (Categoria c : categorias) {
            tableModel.addRow(new Object[]{c.getIdCategoria(), c.getDescricao()});
        }
        em.close();
    }

    private void alterarCategoria() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para alterar.");
            return;
        }

        String novaDescricao = txtDescricao.getText().trim();
        if (novaDescricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A nova descrição não pode estar vazia.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        EntityManager em = JPAUtil.getEntityManager();
        CategoriaDao dao = new CategoriaDao(em);
        Categoria categoria = dao.buscarPorID(id);

        em.getTransaction().begin();
        categoria.setDescricao(novaDescricao);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso!");
        limparCampos();
        carregarCategorias();
    }

    private void removerCategoria() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover esta categoria?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        EntityManager em = JPAUtil.getEntityManager();
        CategoriaDao dao = new CategoriaDao(em);

        Categoria categoria = dao.buscarPorID(id);
        em.getTransaction().begin();
        dao.remover(categoria);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Categoria removida com sucesso!");
        limparCampos();
        carregarCategorias();
    }

    private void preencherCamposComSelecionado() {
        int row = tabela.getSelectedRow();
        if (row != -1) {
            txtDescricao.setText(tableModel.getValueAt(row, 1).toString());
        }
    }

    private void limparCampos() {
        txtDescricao.setText("");
        tabela.clearSelection();
        btnCadastrar.setEnabled(true);
    }
}

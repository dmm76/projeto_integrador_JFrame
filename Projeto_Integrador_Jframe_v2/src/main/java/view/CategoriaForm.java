package view;

import dao.CategoriaDao;
import model.Categoria;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaForm extends JPanel {

    private JTextField txtDescricao;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnCadastrar, btnBuscar, btnAlterar, btnRemover;

    public CategoriaForm() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new FlowLayout());
        txtDescricao = new JTextField(20);
        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(txtDescricao);

        btnCadastrar = new JButton("Cadastrar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnRemover = new JButton("Remover");

        formPanel.add(btnCadastrar);
        formPanel.add(btnBuscar);
        formPanel.add(btnAlterar);
        formPanel.add(btnRemover);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Descrição"}, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Ações
        btnCadastrar.addActionListener(e -> salvarCategoria());
        btnBuscar.addActionListener(e -> carregarCategorias());
        btnAlterar.addActionListener(e -> alterarCategoria());
        btnRemover.addActionListener(e -> removerCategoria());

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
        txtDescricao.setText("");
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

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String novaDescricao = JOptionPane.showInputDialog(this, "Nova descrição:", tableModel.getValueAt(selectedRow, 1));

        if (novaDescricao != null && !novaDescricao.trim().isEmpty()) {
            EntityManager em = JPAUtil.getEntityManager();
            CategoriaDao dao = new CategoriaDao(em);

            Categoria categoria = dao.buscarPorID(id);
            em.getTransaction().begin();
            categoria.setDescricao(novaDescricao);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso!");
            carregarCategorias();
        }
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
        carregarCategorias();
    }
}

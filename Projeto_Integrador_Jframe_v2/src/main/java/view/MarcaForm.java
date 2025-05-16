package view;

import dao.MarcaDao;
import model.Marca;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MarcaForm extends JPanel {

    private JTextField txtDescricao;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnCadastrar, btnBuscar, btnAlterar, btnRemover;

    public MarcaForm() {
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
        btnCadastrar.addActionListener(e -> salvarMarca());
        btnBuscar.addActionListener(e -> carregarMarcas());
        btnAlterar.addActionListener(e -> alterarMarca());
        btnRemover.addActionListener(e -> removerMarca());

        carregarMarcas();
    }

    private void salvarMarca() {
        String descricao = txtDescricao.getText().trim();
        if (descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Descrição não pode estar vazia.");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        MarcaDao dao = new MarcaDao(em);

        Marca marca = new Marca(descricao);
        em.getTransaction().begin();
        dao.cadastrar(marca);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Marca cadastrada com sucesso!");
        txtDescricao.setText("");
        carregarMarcas();
    }

    private void carregarMarcas() {
        tableModel.setRowCount(0);
        EntityManager em = JPAUtil.getEntityManager();
        MarcaDao dao = new MarcaDao(em);
        List<Marca> marcas = dao.buscarTodos();
        for (Marca m : marcas) {
            tableModel.addRow(new Object[]{m.getIdMarca(), m.getDescricao()});
        }
        em.close();
    }

    private void alterarMarca() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma marca para alterar.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String novaDescricao = JOptionPane.showInputDialog(this, "Nova descrição:", tableModel.getValueAt(selectedRow, 1));

        if (novaDescricao != null && !novaDescricao.trim().isEmpty()) {
            EntityManager em = JPAUtil.getEntityManager();
            MarcaDao dao = new MarcaDao(em);

            Marca marca = dao.buscarPorID(id);
            em.getTransaction().begin();
            marca.setDescricao(novaDescricao);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Marca atualizada com sucesso!");
            carregarMarcas();
        }
    }

    private void removerMarca() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma marca para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover esta marca?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        EntityManager em = JPAUtil.getEntityManager();
        MarcaDao dao = new MarcaDao(em);

        Marca marca = dao.buscarPorID(id);
        em.getTransaction().begin();
        dao.remover(marca);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Marca removida com sucesso!");
        carregarMarcas();
    }
}

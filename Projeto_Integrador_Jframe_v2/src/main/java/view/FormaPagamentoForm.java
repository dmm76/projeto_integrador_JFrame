package view;

import dao.FormaPagamentoDao;
import model.FormaPagamento;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormaPagamentoForm extends JPanel {

    private JTextField txtDescricao;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnCadastrar, btnBuscar, btnAlterar, btnRemover;

    public FormaPagamentoForm() {
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
        btnCadastrar.addActionListener(e -> salvarFormaPagamento());
        btnBuscar.addActionListener(e -> carregarFormasPagamento());
        btnAlterar.addActionListener(e -> alterarFormaPagamento());
        btnRemover.addActionListener(e -> removerFormaPagamento());

        carregarFormasPagamento();
    }

    private void salvarFormaPagamento() {
        String descricao = txtDescricao.getText().trim();
        if (descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Descrição não pode estar vazia.");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        FormaPagamentoDao dao = new FormaPagamentoDao(em);

        FormaPagamento forma = new FormaPagamento(descricao);
        em.getTransaction().begin();
        dao.cadastrar(forma);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Forma de pagamento cadastrada com sucesso!");
        txtDescricao.setText("");
        carregarFormasPagamento();
    }

    private void carregarFormasPagamento() {
        tableModel.setRowCount(0);
        EntityManager em = JPAUtil.getEntityManager();
        FormaPagamentoDao dao = new FormaPagamentoDao(em);
        List<FormaPagamento> formas = dao.buscarTodos();
        for (FormaPagamento f : formas) {
            tableModel.addRow(new Object[]{f.getIdFormaPagamento(), f.getDescricao()});
        }
        em.close();
    }

    private void alterarFormaPagamento() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma forma de pagamento para alterar.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String novaDescricao = JOptionPane.showInputDialog(this, "Nova descrição:", tableModel.getValueAt(selectedRow, 1));

        if (novaDescricao != null && !novaDescricao.trim().isEmpty()) {
            EntityManager em = JPAUtil.getEntityManager();
            FormaPagamentoDao dao = new FormaPagamentoDao(em);

            FormaPagamento forma = dao.buscarPorID(id);
            em.getTransaction().begin();
            forma.setDescricao(novaDescricao);
            em.getTransaction().commit();
            em.close();

            JOptionPane.showMessageDialog(this, "Forma de pagamento atualizada com sucesso!");
            carregarFormasPagamento();
        }
    }

    private void removerFormaPagamento() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma forma de pagamento para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover esta forma de pagamento?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        EntityManager em = JPAUtil.getEntityManager();
        FormaPagamentoDao dao = new FormaPagamentoDao(em);

        FormaPagamento forma = dao.buscarPorID(id);
        em.getTransaction().begin();
        dao.remover(forma);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Forma de pagamento removida com sucesso!");
        carregarFormasPagamento();
    }
}

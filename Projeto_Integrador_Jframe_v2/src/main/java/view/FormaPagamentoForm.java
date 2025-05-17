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

        // Campos
        txtDescricao = new JTextField(20);
        btnCadastrar = new JButton("Cadastrar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnRemover = new JButton("Remover");

        // Painel de formulário com GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtDescricao, gbc);

        // Painel de botões
        JPanel botoesPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        botoesPanel.add(btnCadastrar);
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnAlterar);
        botoesPanel.add(btnRemover);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(botoesPanel, gbc);

        // Tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Descrição"}, 0);
        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        btnCadastrar.addActionListener(e -> salvarFormaPagamento());
        btnBuscar.addActionListener(e -> carregarFormasPagamento());
        btnAlterar.addActionListener(e -> alterarFormaPagamento());
        btnRemover.addActionListener(e -> removerFormaPagamento());
        tabela.getSelectionModel().addListSelectionListener(e -> atualizarCampoTexto());

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
        String novaDescricao = txtDescricao.getText().trim();
        if (novaDescricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite uma nova descrição.");
            return;
        }

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

    private void removerFormaPagamento() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma forma de pagamento para remover.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente remover?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        EntityManager em = JPAUtil.getEntityManager();
        FormaPagamentoDao dao = new FormaPagamentoDao(em);
        FormaPagamento forma = dao.buscarPorID(id);

        em.getTransaction().begin();
        dao.remover(forma);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Removido com sucesso!");
        carregarFormasPagamento();
    }

    private void atualizarCampoTexto() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow != -1) {
            String descricao = (String) tableModel.getValueAt(selectedRow, 1);
            txtDescricao.setText(descricao);
        }
    }
}

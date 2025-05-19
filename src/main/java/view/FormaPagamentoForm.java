package view;

import dao.FormaPagamentoDao;
import model.FormaPagamento;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static util.EstiloSistema.*;

public class FormaPagamentoForm extends JPanel {

    private JTextField txtDescricao;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnCadastrar, btnBuscar, btnAlterar, btnRemover, btnLimpar;

    public FormaPagamentoForm() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COR_FUNDO);

        txtDescricao = new JTextField(20);
        aplicarEstiloCampo(txtDescricao);

        btnCadastrar = new JButton("Cadastrar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnRemover = new JButton("Remover");
        btnLimpar = new JButton("Limpar");

        Dimension buttonSize = new Dimension(130, 30);
        btnCadastrar.setPreferredSize(buttonSize);
        btnBuscar.setPreferredSize(buttonSize);
        btnAlterar.setPreferredSize(buttonSize);
        btnRemover.setPreferredSize(buttonSize);
        btnLimpar.setPreferredSize(buttonSize);

        aplicarEstiloBotao(btnCadastrar);
        aplicarEstiloBotao(btnBuscar);
        aplicarEstiloBotao(btnAlterar);
        aplicarEstiloBotao(btnRemover);
        aplicarEstiloBotao(btnLimpar);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COR_FUNDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblDescricao = new JLabel("Descrição:");
        aplicarEstiloLabel(lblDescricao);
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

        btnCadastrar.addActionListener(e -> salvarFormaPagamento());
        btnBuscar.addActionListener(e -> carregarFormasPagamento());
        btnAlterar.addActionListener(e -> alterarFormaPagamento());
        btnRemover.addActionListener(e -> removerFormaPagamento());
        btnLimpar.addActionListener(e -> limparCampos());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                atualizarCampoTexto();
                btnCadastrar.setEnabled(false);
            }
        });

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
        limparCampos();
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
        btnCadastrar.setEnabled(true);
    }

    private void alterarFormaPagamento() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma forma de pagamento para alterar.");
            return;
        }

        String novaDescricao = txtDescricao.getText().trim();
        if (novaDescricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite uma nova descrição.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        EntityManager em = JPAUtil.getEntityManager();
        FormaPagamentoDao dao = new FormaPagamentoDao(em);

        FormaPagamento forma = dao.buscarPorID(id);
        em.getTransaction().begin();
        forma.setDescricao(novaDescricao);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Forma de pagamento atualizada com sucesso!");
        limparCampos();
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
        limparCampos();
        carregarFormasPagamento();
    }

    private void atualizarCampoTexto() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow != -1) {
            String descricao = (String) tableModel.getValueAt(selectedRow, 1);
            txtDescricao.setText(descricao);
        }
    }

    private void limparCampos() {
        txtDescricao.setText("");
        tabela.clearSelection();
        btnCadastrar.setEnabled(true);
    }
}

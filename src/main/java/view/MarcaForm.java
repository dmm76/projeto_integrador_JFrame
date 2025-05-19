package view;

import dao.MarcaDao;
import model.Marca;
import util.JPAUtil;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static util.EstiloSistema.*;

public class MarcaForm extends JPanel {

    private final JTextField txtDescricao = new JTextField(20);
    private final JTable tabela;
    private final DefaultTableModel tableModel;
    private final JButton btnCadastrar = new JButton("Cadastrar");
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnAlterar = new JButton("Alterar");
    private final JButton btnRemover = new JButton("Remover");

    public MarcaForm() {
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

        Dimension buttonSize = new Dimension(130, 30);
        btnCadastrar.setPreferredSize(buttonSize);
        btnBuscar.setPreferredSize(buttonSize);
        btnAlterar.setPreferredSize(buttonSize);
        btnRemover.setPreferredSize(buttonSize);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lbl = new JLabel("Descrição:");
        aplicarEstiloLabel(lbl);
        formPanel.add(lbl, gbc);

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

        JButton btnLimpar = new JButton("Limpar");
        aplicarEstiloBotao(btnLimpar);
        btnLimpar.setPreferredSize(buttonSize);
        btnLimpar.addActionListener(e -> {
            limparCampos();
            btnCadastrar.setEnabled(true);
        });
        botoesPanel.add(btnLimpar);
        formPanel.add(botoesPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Descrição"}, 0);
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnCadastrar.addActionListener(e -> salvarMarca());
        btnBuscar.addActionListener(e -> carregarMarcas());
        btnAlterar.addActionListener(e -> alterarMarca());
        btnRemover.addActionListener(e -> removerMarca());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabela.getSelectedRow() != -1) {
                int selectedRow = tabela.getSelectedRow();
                txtDescricao.setText((String) tableModel.getValueAt(selectedRow, 1));
                btnCadastrar.setEnabled(false);
            }
        });

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
        limparCampos();
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
        String novaDescricao = txtDescricao.getText().trim();
        if (novaDescricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A nova descrição não pode estar vazia.");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        MarcaDao dao = new MarcaDao(em);

        Marca marca = dao.buscarPorID(id);
        em.getTransaction().begin();
        marca.setDescricao(novaDescricao);
        em.getTransaction().commit();
        em.close();

        JOptionPane.showMessageDialog(this, "Marca atualizada com sucesso!");
        limparCampos();
        carregarMarcas();
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
        limparCampos();
        carregarMarcas();
    }

    private void limparCampos() {
        txtDescricao.setText("");
        tabela.clearSelection();
        btnCadastrar.setEnabled(true);
    }
}

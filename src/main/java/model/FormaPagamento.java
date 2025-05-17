package model;

import javax.persistence.*;

@Entity
@Table(name = "formapagamento")
public class FormaPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFormaPagamento;
    private String descricao;

    public FormaPagamento() {
    }

    public FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public int getIdFormaPagamento() {
        return idFormaPagamento;
    }

    public void setIdFormaPagamento(int idFormaPagamento) {
        this.idFormaPagamento = idFormaPagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FormaPagamento  formaPagamento = (FormaPagamento) obj;
        return idFormaPagamento == formaPagamento.idFormaPagamento;
    }
}

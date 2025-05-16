package model;

import javax.persistence.*;

@Entity
@Table(name= "marca")
public class Marca {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int idMarca;
    private String descricao;

    public Marca() {
    }

    public Marca(String descricao) {
        this.descricao = descricao;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

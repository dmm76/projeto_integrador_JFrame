package model;

import javax.persistence.*;

@Entity
@Table(name = "usuariosistema")
public class UsuarioSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuarioSistema;

    private String nomeUsuario;
    private String loginUsuario;
    private String senhaUsuario;
    private String perfilUsuario; // admin ou operador

    public UsuarioSistema() {
    }

    public UsuarioSistema(String nomeUsuario, String loginUsuario, String senhaUsuario, String perfilUsuario) {
        this.nomeUsuario = nomeUsuario;
        this.loginUsuario = loginUsuario;
        this.senhaUsuario = senhaUsuario;
        this.perfilUsuario = perfilUsuario;
    }

    public int getIdUsuarioSistema() {
        return idUsuarioSistema;
    }

    public void setIdUsuarioSistema(int idUsuarioSistema) {
        this.idUsuarioSistema = idUsuarioSistema;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public String getPerfilUsuario() {
        return perfilUsuario;
    }

    public void setPerfilUsuario(String perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
    }

    @Override
    public String toString() {
        return nomeUsuario + " (" + perfilUsuario + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UsuarioSistema)) return false;
        UsuarioSistema other = (UsuarioSistema) obj;
        return idUsuarioSistema == other.idUsuarioSistema;
    }
}

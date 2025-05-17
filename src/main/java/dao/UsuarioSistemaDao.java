package dao;

import model.UsuarioSistema;

import javax.persistence.EntityManager;
import java.util.List;

public class UsuarioSistemaDao {
    private EntityManager em;

    public UsuarioSistemaDao(EntityManager em) {
        this.em = em;
    }

    // Autenticar login
    public UsuarioSistema autenticar(String login, String senha) {
        String jpql = "SELECT u FROM UsuarioSistema u WHERE u.loginUsuario = :login AND u.senhaUsuario = :senha";
        List<UsuarioSistema> usuarios = em.createQuery(jpql, UsuarioSistema.class)
                .setParameter("login", login)
                .setParameter("senha", senha)
                .getResultList();
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    // Cadastrar novo usuário
    public void cadastrar(UsuarioSistema usuario) {
        em.persist(usuario);
    }

    // Buscar todos os usuários
    public List<UsuarioSistema> buscarTodos() {
        String jpql = "SELECT u FROM UsuarioSistema u";
        return em.createQuery(jpql, UsuarioSistema.class).getResultList();
    }

    // Buscar usuário por ID
    public UsuarioSistema buscarPorID(int id) {
        return em.find(UsuarioSistema.class, id);
    }

    // Atualizar usuário
    public void alterar(UsuarioSistema usuario) {
        em.merge(usuario);
    }

    // Remover usuário
    public void remover(UsuarioSistema usuario) {
        if (!em.contains(usuario)) {
            usuario = em.merge(usuario);
        }
        em.remove(usuario);
    }
}

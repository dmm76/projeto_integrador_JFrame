package dao;

import model.Categoria;

import javax.persistence.EntityManager;
import java.util.List;

public class CategoriaDao {
    private EntityManager em;

    //construtor do FormaPagamentoDao
    public CategoriaDao(EntityManager em){
        this.em = em;
    }

    //cadastrar
    public void cadastrar(Categoria categoria){
        em.persist(categoria);
    }
    //listar
    public List<Categoria> buscarTodos(){
        String jpql = "Select c FROM Categoria c";
        return em.createQuery(jpql, Categoria.class).getResultList();
    }
    //buscar por Id
    public Categoria buscarPorID(int id){
        return em.find(Categoria.class, id);
    }
    //remover
    public void remover(Categoria categoria){
        em.remove(categoria);
    }
    //alterar
    public void alterar(Categoria categoria){
        em.merge(categoria);
    }
}

package dao;

import model.FormaPagamento;
import model.Marca;

import javax.persistence.EntityManager;
import java.util.List;

public class MarcaDao {
    private EntityManager em;

    //construtor do FormaPagamentoDao
    public MarcaDao(EntityManager em){
        this.em = em;
    }

    //cadastrar
    public void cadastrar(Marca marca){
        em.persist(marca);
    }
    //listar
    public List<Marca> buscarTodos(){
        String jpql = "Select m FROM Marca m";
        return em.createQuery(jpql, Marca.class).getResultList();
    }

    //buscar por Id
    public Marca buscarPorID(int id){
        return em.find(Marca.class, id);
    }
    //remover
    public void remover(Marca marca){
        em.remove(marca);
    }
    //alterar
    public void alterar(Marca marca){
        em.merge(marca);
    }
}

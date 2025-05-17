package dao;

import model.Pedido;

import javax.persistence.EntityManager;
import java.util.List;

public class PedidoDao {
    private EntityManager em;

    //construtor do PedidoDao
    public PedidoDao(EntityManager em){
        this.em = em;
    }

    //cadastrar
    public void cadastrar(Pedido pedido){
        em.persist(pedido);
    }
    //listar
    public List<Pedido> buscarTodos(){
        String jpql = "Select p FROM Pedido p";
        return em.createQuery(jpql, Pedido.class).getResultList();
    }
    //buscar por Id
    public Pedido buscarPorID(int id){
        return em.find(Pedido.class, id);
    }
    //remover
    public void remover(Pedido pedido){
        if (!em.contains(pedido)) {
            pedido = em.merge(pedido); // anexa ao contexto de persistência
        }
        em.remove(pedido);
    }
    //alterar
    public void alterar(Pedido pedido){
        em.merge(pedido);
    }

}

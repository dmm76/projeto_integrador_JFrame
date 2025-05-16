package dao;

import model.Fornecedor;
import model.Marca;

import javax.persistence.EntityManager;
import java.util.List;

public class FornecedorDao {
    private EntityManager em;

    //construtor do FormaPagamentoDao
    public FornecedorDao(EntityManager em){
        this.em = em;
    }

    //cadastrar
    public void cadastrar(Fornecedor fornecedor){
        em.persist(fornecedor);
    }
    //listar
    public List<Fornecedor> buscarTodos(){
        String jpql = "Select f FROM Fornecedor f";
        return em.createQuery(jpql, Fornecedor.class).getResultList();
    }
    //buscar por Id
    public Fornecedor buscarPorID(int id){
        return em.find(Fornecedor.class, id);
    }
    //remover
    public void remover(Fornecedor fornecedor){
        em.remove(fornecedor);
    }
    //alterar
    public void alterar(Fornecedor fornecedor){
        em.merge(fornecedor);
    }
}

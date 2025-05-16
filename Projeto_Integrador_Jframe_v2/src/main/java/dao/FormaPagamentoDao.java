package dao;

import model.FormaPagamento;

import javax.persistence.EntityManager;
import java.util.List;

public class FormaPagamentoDao {
    //conexao com o banco
    private EntityManager em;

    //construtor do FormaPagamentoDao
    public FormaPagamentoDao(EntityManager em){
        this.em = em;
    }

    //cadastrar
    public void cadastrar(FormaPagamento formaPagamento){
        em.persist(formaPagamento);
    }
    //listar
    public List<FormaPagamento> buscarTodos(){
        String jpql = "Select f FROM FormaPagamento f";
        return em.createQuery(jpql, FormaPagamento.class).getResultList();
    }
    //buscar por Id
    public FormaPagamento buscarPorID(int id){
        return em.find(FormaPagamento.class, id);
    }
    //remover
    public void remover(FormaPagamento formaPagamento){
        em.remove(formaPagamento);
    }
    //alterar
    public void alterar(FormaPagamento formaPagamento){
        em.merge(formaPagamento);
    }
}

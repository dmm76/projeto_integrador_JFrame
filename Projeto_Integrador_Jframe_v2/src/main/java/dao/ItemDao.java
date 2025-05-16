package dao;


import model.Item;

import javax.persistence.EntityManager;
import java.util.List;

public class ItemDao {
    private EntityManager em;

    public ItemDao(EntityManager em) {
        this.em = em;
    }

    // Cadastrar novo item
    public void cadastrar(Item item) {
        em.persist(item);
    }

    // Buscar item por ID
    public Item buscarPorId(int id) {
        return em.find(Item.class, id);
    }

    // Listar todos os itens
    public List<Item> listarTodos() {
        String jpql = "SELECT i FROM Item i";
        return em.createQuery(jpql, Item.class).getResultList();
    }

    // Atualizar item
    public void atualizar(Item item) {
        em.merge(item);
    }

    // Remover item (com verificação de estado gerenciado)
    public void remover(Item item) {
        if (!em.contains(item)) {
            item = em.merge(item);
        }
        em.remove(item);
    }
}

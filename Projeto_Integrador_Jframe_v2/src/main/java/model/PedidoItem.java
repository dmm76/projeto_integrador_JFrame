package model;

import javax.persistence.*;

@Entity
@Table(name = "pedidoitem")
public class PedidoItem {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int idPedidoItem;

    @ManyToOne
    @JoinColumn(name="idItem")
    private Item item;

    @ManyToOne
    @JoinColumn(name="idPedido")
    private Pedido pedido;

    private int quantidadeItem;
    private double valorItem;
    @Column(insertable = false, updatable = false)
    private double valorTotalItem;

    public PedidoItem() {
    }

    public PedidoItem(Item item, Pedido pedido, int quantidadeItem, double valorItem) {
        this.item = item;
        this.pedido = pedido;
        this.quantidadeItem = quantidadeItem;
        this.valorItem = valorItem;
        //this.valorTotalItem = valorTotalItem;
    }

    public int getIdPedidoItem() {
        return idPedidoItem;
    }

    public void setIdPedidoItem(int idItemPedido) {
        this.idPedidoItem = idItemPedido;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public int getQuantidadeItem() {
        return quantidadeItem;
    }

    public void setQuantidadeItem(int quantidadeItem) {
        this.quantidadeItem = quantidadeItem;
    }

    public double getValorItem() {
        return valorItem;
    }

    public void setValorItem(double valorItem) {
        this.valorItem = valorItem;
    }

    public double getValorTotalItem() {
        return valorTotalItem;
    }

    public void setValorTotalItem(double valorTotalItem) {
        this.valorTotalItem = valorTotalItem;
    }

    @Override
    public String toString() {
        return item.getNomeProduto() + " x " + quantidadeItem;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PedidoItem pedidoItem = (PedidoItem) obj;
        return idPedidoItem == pedidoItem.idPedidoItem;
    }
}

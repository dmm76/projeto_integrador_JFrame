package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int idPedido;

    @Temporal(TemporalType.DATE)
    private Date dataPedido;

    private String statusPedido;
    private double valorTotalPedido;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idFormaPagamento")
    private FormaPagamento formaPagamento;

    public Pedido() {
    }

    public Pedido(Date dataPedido, String statusPedido, double valorTotalPedido, Cliente cliente, FormaPagamento formaPagamento) {
        this.dataPedido = dataPedido;
        this.statusPedido = statusPedido;
        this.valorTotalPedido = valorTotalPedido;
        this.cliente = cliente;
        this.formaPagamento = formaPagamento;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }

    public double getValorTotalPedido() {
        return valorTotalPedido;
    }

    public void setValorTotalPedido(double valorTotalPedido) {
        this.valorTotalPedido = valorTotalPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    @Override
    public String toString() {
        if (idPedido == 0) {
            return "-- Selecione --";
        }
        return "Pedido #" + idPedido;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pedido pedido = (Pedido) obj;
        return idPedido == pedido.idPedido;
    }
}

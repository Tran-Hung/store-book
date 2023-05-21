package com.store.book.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ORDERS")
public class Orders implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD")
    private Long record;

    @Column(name = "ID_ORDER")
    private String idOrder;

    @Column(name = "DATE_BUY")
    private Date dateBuy;

    @Column(name = "TOTAL")
    private Long total;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name="CLIENT_ID", nullable=false, referencedColumnName = "ID_CLIENT")
    private Client client;

    @OneToMany(mappedBy = "orders", orphanRemoval=true, cascade = CascadeType.ALL)
    private List<DetailOrder> detailOrder = new ArrayList<DetailOrder>();

    public Long getRecord() {
        return record;
    }

    public void setRecord(Long record) {
        this.record = record;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public Date getDateBuy() {
        return dateBuy;
    }

    public void setDateBuy(Date dateBuy) {
        this.dateBuy = dateBuy;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<DetailOrder> getDetailOrder() {
        return detailOrder;
    }

    public void setDetailOrder(List<DetailOrder> detailOrder) {
        this.detailOrder = detailOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

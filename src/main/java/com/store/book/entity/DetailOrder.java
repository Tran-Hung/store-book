package com.store.book.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "DETAIL_ORDER")
public class DetailOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD")
    private Long record;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID_ORDER", nullable=false)
    private Orders orders;

    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "AMOUNT")
    private Long amount;

    public Long getRecord() {
        return record;
    }

    public void setRecord(Long record) {
        this.record = record;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}

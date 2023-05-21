package com.store.book.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TOY")
public class Toy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD")
    private Long record;

    @Column(name = "ID_TOY")
    private String idToy;

    @Column(name = "NAME_TOY")
    private String nameToy;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "PUBLISHER")
    private String publisher;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATE_PUBLISH")
    private Date datePublish;

    @ManyToOne
    @JoinColumn(name="CATEGORY_ID", nullable=false)
    private CategoryToy categoryToy;

    public Long getRecord() {
        return record;
    }

    public void setRecord(Long record) {
        this.record = record;
    }

    public String getIdToy() {
        return idToy;
    }

    public void setIdToy(String idToy) {
        this.idToy = idToy;
    }

    public String getNameToy() {
        return nameToy;
    }

    public void setNameToy(String nameToy) {
        this.nameToy = nameToy;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getDatePublish() {
        return datePublish;
    }

    public void setDatePublish(Date datePublish) {
        this.datePublish = datePublish;
    }

    public CategoryToy getCategoryToy() {
        return categoryToy;
    }

    public void setCategoryToy(CategoryToy categoryToy) {
        this.categoryToy = categoryToy;
    }
}

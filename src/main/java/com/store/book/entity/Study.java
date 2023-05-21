package com.store.book.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "STUDY")
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD")
    private Long record;

    @Column(name = "ID_STUDY")
    private String idStudy;

    @Column(name = "NAME_STUDY")
    private String nameStudy;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "PUBLISHER")
    private String publisher;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATE_PUBLISH")
    private Date datePublish;

    @ManyToOne
    @JoinColumn(name="CATEGORY_ID", nullable=false)
    private CategoryStudy categoryStudy;

    public Long getRecord() {
        return record;
    }

    public void setRecord(Long record) {
        this.record = record;
    }

    public String getIdStudy() {
        return idStudy;
    }

    public void setIdStudy(String idStudy) {
        this.idStudy = idStudy;
    }

    public String getNameStudy() {
        return nameStudy;
    }

    public void setNameStudy(String nameStudy) {
        this.nameStudy = nameStudy;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public CategoryStudy getCategoryStudy() {
        return categoryStudy;
    }

    public void setCategoryStudy(CategoryStudy categoryStudy) {
        this.categoryStudy = categoryStudy;
    }
}

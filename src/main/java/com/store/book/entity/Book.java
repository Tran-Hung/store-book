package com.store.book.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BOOK")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD")
    private Long record;

    @Column(name = "ID_BOOK")
    private String idBook;

    @Column(name = "NAME_BOOK")
    private String nameBook;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "PUBLISHER")
    private String publisher;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATE_PUBLISH")
    private Date datePublish;

    @ManyToOne
    @JoinColumn(name="CATEGORY_ID", nullable=false)
    private CategoryBook categoryBook;

    public Long getRecord() {
        return record;
    }

    public void setRecord(Long record) {
        this.record = record;
    }

    public String getIdBook() {
        return idBook;
    }

    public void setIdBook(String idBook) {
        this.idBook = idBook;
    }

    public String getNameBook() {
        return nameBook;
    }

    public void setNameBook(String nameBook) {
        this.nameBook = nameBook;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public CategoryBook getCategoryBook() {
        return categoryBook;
    }

    public void setCategoryBook(CategoryBook categoryBook) {
        this.categoryBook = categoryBook;
    }
}

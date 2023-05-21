package com.store.book.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CLIENT")
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD")
    private Long record;

    @Column(name = "ID_CLIENT")
    private String idClient;

    @Column(name = "NAME_CLIENT")
    private String nameClient;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "BIRTHDAY")
    private Date birthday;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ID_APP_USER")
    private Long idAppUser;

    @Column(nullable = true, length = 64)
    private String photos;

    @ManyToOne
    @JoinColumn(name="TYPE_ID", nullable=false, referencedColumnName = "ID")
    private TypeClient typeClient;

    @OneToMany(mappedBy = "client", orphanRemoval=true)
    private List<Orders> order = new ArrayList<Orders>();

    public Client(String nameClient, String email, Date birthday, String phone) {
        this.nameClient = nameClient;
        this.email = email;
        this.birthday = birthday;
        this.phone = phone;
    }

    public Client() {

    }

    public Long getRecord() {
        return record;
    }

    public void setRecord(Long record) {
        this.record = record;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public TypeClient getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(TypeClient typeClient) {
        this.typeClient = typeClient;
    }

    public List<Orders> getOrder() {
        return order;
    }

    public void setOrder(List<Orders> order) {
        this.order = order;
    }

    public Long getIdAppUser() {
        return idAppUser;
    }

    public void setIdAppUser(Long idAppUser) {
        this.idAppUser = idAppUser;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    @Transient
    public String getPhotosImagePath() {
        if (photos == null || idClient == null) return null;

        return "/user-photos/" + idClient + "/" + photos;
    }
}

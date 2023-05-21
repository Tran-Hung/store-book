package com.store.book.entity.bean;

import java.util.List;

public class CartBean {
    private String idOrder;
    private List<ProductBean> productBean;
    private Long total;

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public List<ProductBean> getProductBean() {
        return productBean;
    }

    public void setProductBean(List<ProductBean> productBean) {
        this.productBean = productBean;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}

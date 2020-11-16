package ims.supporting;

import ims.entities.Product;

import javax.persistence.Transient;

public class TableProduct {
    private String brand;
    private String invNum;
    private String givenBy;
    private String givenOn;
    @Transient
    private Product product;

    public TableProduct(String brand, String invNum, String givenBy, String givenOn, Product product) {
        this.brand = brand;
        this.invNum = invNum;
        this.givenBy = givenBy;
        this.givenOn = givenOn;
        this.product = product;
    }

    public String getBrand() {
        return brand;
    }

    public String getInvNum() {
        return invNum;
    }

    public String getGivenBy() {
        return givenBy;
    }

    public String getGivenOn() {
        return givenOn;
    }

    public Product getProduct() {
        return product;
    }


}

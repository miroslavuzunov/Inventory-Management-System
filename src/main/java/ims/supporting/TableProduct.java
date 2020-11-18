package ims.supporting;

import ims.entities.Product;
import javafx.scene.control.Button;

import javax.persistence.Transient;

public class TableProduct {
    private String brand;
    private String invNum;
    private String givenBy;
    private String givenOn;
    private String status;
    private Button button;

    @Transient
    private Product product;

    public TableProduct(String brand, String invNum, String givenBy, String givenOn, Product product, String status) {
        this.brand = brand;
        this.invNum = invNum;
        this.givenBy = givenBy;
        this.givenOn = givenOn;
        this.product = product;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public Product getProduct() {
        return product;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button){
        this.button = button;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

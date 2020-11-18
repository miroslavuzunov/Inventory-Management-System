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
    private String productType;
    private String totalQuantity;
    private String availableQuantity;

    @Transient
    private Product product;

    public TableProduct(String brand, String invNum, String givenBy, String givenOn, String status, Product product) {
        this.brand = brand;
        this.invNum = invNum;
        this.givenBy = givenBy;
        this.givenOn = givenOn;
        this.status = status;
        this.product = product;
    }

    public TableProduct(String brand, String productType, String totalQuantity, String availableQuantity) {
        this.brand = brand;
        this.productType = productType;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
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

    public String getProductType() {
        return productType;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public void setButton(Button button){
        this.button = button;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

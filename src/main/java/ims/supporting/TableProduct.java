package ims.supporting;

import ims.entities.Product;
import javafx.scene.control.Button;

import javax.persistence.Transient;

public class TableProduct{
    private String brand;
    private String invNum;
    private String givenBy;
    private String givenOn;
    private String registeredOn;
    private String status;
    private String initialPrice;
    private String currentPrice;
    private Button button;
    private String productType;
    private String totalQuantity;
    private String availableQuantity;

    @Transient
    private Product product;

    public TableProduct() {
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

    public String getInitialPrice() {
        return initialPrice;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setInvNum(String invNum) {
        this.invNum = invNum;
    }

    public void setInitialPrice(String initialPrice) {
        this.initialPrice = initialPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setGivenBy(String givenBy) {
        this.givenBy = givenBy;
    }

    public void setGivenOn(String givenOn) {
        this.givenOn = givenOn;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }
}

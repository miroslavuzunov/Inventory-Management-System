package ims.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "product")
public class Product extends BaseEntity{
    private String inventoryNumber;
    private LocalDate registeredOn;
    private boolean available;
    private ProductDetails productDetails;

    public Product() {
    }

    @Column(name = "inv_num")
    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    @Column(name = "received_date")
    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    @Column(name = "available")
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @ManyToOne(targetEntity = ProductDetails.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_details_id", referencedColumnName = "id")
    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }
}

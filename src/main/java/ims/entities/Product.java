package ims.entities;

import ims.enums.RecordStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "product")
public class Product extends BaseEntity{
    private String inventoryNumber;
    private LocalDate registeredOn;
    private boolean available;
    private boolean existing;
    private ProductDetails productDetails;
    private RecordStatus status;

    public Product() {
    }

    @Column(name = "inv_num")
    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    @Column(name = "created_on")
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

    @Column(name = "existing")
    public boolean isExisting() {
        return existing;
    }

    public void setExisting(boolean existing) {
        this.existing = existing;
    }

    @ManyToOne(targetEntity = ProductDetails.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_details_id", referencedColumnName = "id")
    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public RecordStatus getStatus() {
        return status;
    }

    public void setStatus(RecordStatus status) {
        this.status = status;
    }
}

package ims.entities;

import ims.enums.PriceCurrency;
import ims.enums.ProductType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "product_details")
public class ProductDetails extends BaseEntity{
    private String brandModel;
    private BigDecimal initialPrice;
    private BigDecimal currentPrice;
    private PriceCurrency priceCurrency;
    private Integer quantity;
    private String description;
    private ProductType productType;
    private DepreciationDegree depreciationDegree;
    private Set<Product> products;

    public ProductDetails() {
    }

    @Column(name = "brand_model")
    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    @Column(name = "initial_price")
    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(BigDecimal price) {
        this.initialPrice = price;
    }

    @Column(name = "current_price")
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Column(name = "price_currency")
    @Enumerated(EnumType.STRING)
    public PriceCurrency getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(PriceCurrency priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    @Column(name = "quantity")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    @ManyToOne(targetEntity = DepreciationDegree.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "depreciation_dg_id", referencedColumnName = "id")
    public DepreciationDegree getDepreciationDegree() {
        return depreciationDegree;
    }

    public void setDepreciationDegree(DepreciationDegree depreciationDegree) {
        this.depreciationDegree = depreciationDegree;
    }

    @OneToMany(mappedBy = "productDetails", targetEntity = Product.class, fetch = FetchType.LAZY)
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}

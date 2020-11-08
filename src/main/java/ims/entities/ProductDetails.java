package ims.entities;

import ims.enums.Currency;
import ims.enums.ProductType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_details")
public class ProductDetails extends BaseEntity{
    private String brandAndModel;
    private BigDecimal price;
    private Currency priceCurrency;
    private String description;
    private ProductType productType;
    private ScrappingCriteria scrappingCriteria;
    private DepreciationDegree depreciationDegree;

    public ProductDetails() {
    }

    @Column(name = "brand_model")
    public String getBrandAndModel() {
        return brandAndModel;
    }

    public void setBrandAndModel(String brandAndModel) {
        this.brandAndModel = brandAndModel;
    }

    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "price_currency")
    @Enumerated(EnumType.STRING)
    public Currency getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(Currency priceCurrency) {
        this.priceCurrency = priceCurrency;
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

    @ManyToOne(targetEntity = ScrappingCriteria.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "scrapping_criteria_id", referencedColumnName = "id")
    public ScrappingCriteria getScrappingCriteria() {
        return scrappingCriteria;
    }

    public void setScrappingCriteria(ScrappingCriteria scrappingCriteria) {
        this.scrappingCriteria = scrappingCriteria;
    }

    @ManyToOne(targetEntity = DepreciationDegree.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "depreciation_dg_id", referencedColumnName = "id")
    public DepreciationDegree getDepreciationDegree() {
        return depreciationDegree;
    }

    public void setDepreciationDegree(DepreciationDegree depreciationDegree) {
        this.depreciationDegree = depreciationDegree;
    }
}

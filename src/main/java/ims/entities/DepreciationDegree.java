package ims.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "depreciation_dg")
public class DepreciationDegree extends BaseEntity{
    private String category;
    private BigDecimal percentage;
    private String productGroup;

    public DepreciationDegree() {
    }

    @Column(name = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "percentage")
    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Column(name = "product_group")
    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String group) {
        this.productGroup = group;
    }
}

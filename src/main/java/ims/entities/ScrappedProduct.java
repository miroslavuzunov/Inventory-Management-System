package ims.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "scrapped_products")
public class ScrappedProduct extends BaseEntity{
    private Product product;
    private LocalDate scrapDate;

    @OneToOne(targetEntity = Product.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product scrappedProduct) {
        this.product = scrappedProduct;
    }

    @Column(name = "scrap_date")
    public LocalDate getScrapDate() {
        return scrapDate;
    }

    public void setScrapDate(LocalDate scrapDate) {
        this.scrapDate = scrapDate;
    }
}

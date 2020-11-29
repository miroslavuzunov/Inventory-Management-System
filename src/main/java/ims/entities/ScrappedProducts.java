package ims.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "scrapped_products")
public class ScrappedProducts extends BaseEntity{
    private Product scrappedProduct;
    private LocalDate scrapDate;

    @OneToOne(targetEntity = Product.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getScrappedProduct() {
        return scrappedProduct;
    }

    public void setScrappedProduct(Product scrappedProduct) {
        this.scrappedProduct = scrappedProduct;
    }

    @Column(name = "scrap_date")
    public LocalDate getScrapDate() {
        return scrapDate;
    }

    public void setScrapDate(LocalDate scrapDate) {
        this.scrapDate = scrapDate;
    }
}

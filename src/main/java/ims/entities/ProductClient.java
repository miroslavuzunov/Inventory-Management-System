package ims.entities;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "product_client")
public class ProductClient extends BaseEntity {
    private Product product;
    private User client;
    private User mrt;
    private LocalDate givenOn;

    public ProductClient() {
    }

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "mrt_id", referencedColumnName = "id")
    public User getMrt() {
        return mrt;
    }

    public void setMrt(User mrt) {
        this.mrt = mrt;
    }

    @Column(name = "created_on")
    public LocalDate getGivenOn() {
        return givenOn;
    }

    public void setGivenOn(LocalDate givenOn) {
        this.givenOn = givenOn;
    }
}

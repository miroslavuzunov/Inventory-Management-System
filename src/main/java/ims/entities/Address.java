package ims.entities;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address extends BaseEntity{
    private City city;
    private String street;
    private String details;

    public Address() {
    }

    @ManyToOne(targetEntity = City.class)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Column(name = "street")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Column(name = "details")
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

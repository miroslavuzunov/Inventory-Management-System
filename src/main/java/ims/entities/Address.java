package ims.entities;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address extends BaseEntity{
    private City city;
    private Country country;
    private String street;
    private String details;

    public Address() {
    }

    public Address(City city, Country country, String street, String details) {
        setCity(city);
        setCountry(country);
        setStreet(street);
        setDetails(details);
    }

    @ManyToOne(targetEntity = City.class)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @ManyToOne(targetEntity = Country.class)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

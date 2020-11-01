package ims.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "country")
public class Country extends BaseEntity{
    private String name;
    private Set<City> cities;

    public Country() {
        cities = new HashSet<>();
    }

    public Country(String name) {
        setName(name);
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "country", targetEntity = City.class)
    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }
}

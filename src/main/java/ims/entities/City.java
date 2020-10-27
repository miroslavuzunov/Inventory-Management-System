package ims.entities;

import javax.persistence.*;

@Entity
@Table(name = "city")
public class City extends BaseEntity{

    private String name;
    private String region;

    public City(){}

    public City(String name, String region) {
        setName(name);
        setRegion(region);
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "region")
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}

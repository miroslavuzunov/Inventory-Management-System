import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import static constants.ExceptionMessagesConstants.*;

@Entity
@Table(name = "address")
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "details", nullable = false)
    private String details;

    @OneToMany(mappedBy = "address")
    private List<Address> addressList; // Somewhere to store them maybe?

    @OneToOne(mappedBy = "stockroom")
    private List<Address> stockroomList;

//    public Address() {
//        street = "";
//        details = "";
//    }

    public Address(City city, Country country, String street, String details) {
        setStreet(street);
        setDetails(details);
    }

    public int getId() {
        return this.id;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        if (street == null || street.trim().isEmpty()) {
            street = "Address details";
            throw new IllegalArgumentException(String.format(NULL_OR_EMPTY_EXCEPTION_MESSAGE, street));
        }
        if (street.length() > 100) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_100_EXCEPTION_MESSAGE);
        }
        if (street.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.street = street;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        if (details == null || details.trim().isEmpty()) {
            details = "Address details";
            throw new IllegalArgumentException(String.format(NULL_OR_EMPTY_EXCEPTION_MESSAGE, details));
        }
        if (details.length() > 200) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_200_EXCEPTION_MESSAGE);
        }
        if (details.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.details = details;
    }

//    public Country getCountry() {
//        return this.country;
//    }
//
//    public void setCountry(Country country) {
//        this.country = country;
//    }
//
//    public City getCity() {
//        return this.city;
//    }
//
//    public void setCity(City city) {
//        this.city = city;
//    }
}
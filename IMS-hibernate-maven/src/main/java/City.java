import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import static constants.ExceptionMessagesConstants.*;

@Entity
@Table(name = "city")
public class City implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "region", nullable = false)
    private String region;

    @OneToMany(mappedBy = "city")
    private List<Country> cityList; // Somewhere to store them maybe?


    public City(int id, String name, String region) {
        setId(id);
        setName(name);
        setRegion(region);
    }

    public City() {
        name = "";
        region = "";
    }

    public int getId() {
        return this.id;
    }

    // For the identity type when we want to add the id explicitly for test.
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            name = "City name";
            throw new IllegalArgumentException(String.format(name, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_50_EXCEPTION_MESSAGE);
        }
        if (name.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.name = name;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        if (region == null || region.trim().isEmpty()) {
            region = "City region";
            throw new IllegalArgumentException(String.format(region, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (region.length() > 50) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_50_EXCEPTION_MESSAGE);
        }
        if (region.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.region = region;
    }
}

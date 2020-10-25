import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import static constants.ExceptionMessagesConstants.*;

@Entity
@Table(name = "country")
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "country")
    private List<Country> countryList; // Somewhere to store them maybe?

    public Country() {
        name = "";
    }

    public Country(String name) {
        setName(name);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            name = "Country name";
            throw new IllegalArgumentException(String.format(name, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_100_EXCEPTION_MESSAGE);
        }
        if (name.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.name = name;
    }
}

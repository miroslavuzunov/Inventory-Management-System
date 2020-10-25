import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import static constants.ExceptionMessagesConstants.*;

@Entity
@Table(name = "product_group")
public class ProductGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "group", nullable = false, unique = true)
    private String group;

    @OneToMany(mappedBy = "product_details")
    private List<ProductGroup> productGroupList; // Somewhere to store them maybe?


    public ProductGroup() {
        group = "";
    }

    public ProductGroup(String group) {
        setGroup(group);
    }

    public int getId() {
        return this.id;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        if (group == null || group.trim().isEmpty()) {
            group = "Product group group";
            throw new IllegalArgumentException(String.format(group, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (group.length() > 100) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_100_EXCEPTION_MESSAGE);
        }
        if (group.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.group = group;
    }
}

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "stockroom")
public class Stockroom implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @OneToOne(mappedBy = "user_id")
    private List<Stockroom> userList; // Somewhere to store them maybe?

    @OneToOne(mappedBy = "address_id")
    private List<Stockroom> addressList; // Somewhere to store them maybe?

    @OneToMany(mappedBy = "product")
    private List<Stockroom> stockroomList; // Somewhere to store them maybe?

    @OneToMany(mappedBy = "scrapped_products")
    private List<Stockroom> scrappedProductList; // Somewhere to store them maybe?

    public Stockroom() {}

    public int getId() {
        return this.id;
    }

}

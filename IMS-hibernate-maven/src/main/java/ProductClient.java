import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import static constants.ExceptionMessagesConstants.INVALID_DATE_PATTERN_MESSAGE_EXCEPTION;
import static constants.ExceptionMessagesConstants.NULL_OR_EMPTY_EXCEPTION_MESSAGE;

@Entity
@Table(name = "product_client")
public class ProductClient implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductClient productClientProduct;

    @ManyToOne
    @JoinColumn(name = "mrt_id", nullable = false)
    private ProductClient productClientMrt;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ProductClient getProductClientClient;

    @Column(name = "created_on", nullable = false)
    private String createdOn;

    public ProductClient() {
        createdOn = "";
    }

    public ProductClient(String createdOn) {
        setCreatedOn(createdOn);
    }

    public int getId() {
        return this.id;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(String createdOn) {
        if (createdOn == null || createdOn.trim().isEmpty()) {
            createdOn = "User created on";
            throw new IllegalArgumentException(String.format(createdOn, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        /*
            Regex explanation:
            Year:
            2[0-1]              # for the first two numbers we have 100% 2 and after that 0 OR 1
            [0-9]{2}            # the last two numbers from the year can be both from 0-9
             Month:
            (0[0-9]             # 100% the number 0 for the month and one number from 0-9
            |1[0-2])            # OR 100 % the number 1 for the month and one number from 0 to 2
            Day:
            ([0-2]{1}[0-9]{1}   # One number from 0-2 and one number from 0-9
            |3[0-1])            # OR one number 100% 3 and one number from 0-1

         */
        String pattern = "/2[0-1][0-9]{2}-(0[0-9]|1[0-2])-([0-2]{1}[0-9]{1}|3[0-1])/";
        String createdOnFormatted = createdOn.trim(); // remove the whitespaces
        if (!(pattern.matches(createdOnFormatted))) {
            throw new IllegalArgumentException(String.format(createdOn, INVALID_DATE_PATTERN_MESSAGE_EXCEPTION));
        }
        this.createdOn = createdOn;
    }
}
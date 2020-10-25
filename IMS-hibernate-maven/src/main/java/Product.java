import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import static constants.ExceptionMessagesConstants.*;

@Entity
@Table(name = "product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "inv_num", nullable = false, unique = true)
    private String inventoryNumber;

    @Column(name = "received_date", nullable = false)
    private String receivedDate;

    @Column(name = "available", nullable = false)
    private Integer available;

    @ManyToOne
    @JoinColumn(name = "product_details_id", nullable = false)
    private Product productDetails;

    @ManyToOne
    @JoinColumn(name = "stockroom_id", nullable = false)
    private Product productStockroom;

    @OneToOne(mappedBy = "scrapped_products")
    private List<Product> scrappedProductList; // Somewhere to store them maybe?

    @OneToMany(mappedBy = "product_client")
    private List<Product> productProductClientList; // Somewhere to store them maybe?

//    public Product() {
//        inventoryNumber = "";
//        receivedDate = "";
//        available = 0;
//    }

    public Product(String inventoryNumber, String receivedDate, Integer available) {
        setInventoryNumber(inventoryNumber);
        setReceivedDate(receivedDate);
        setAvailable(available);
    }

    public int getId() {
        return this.id;
    }

    public String getInventoryNumber() {
        return this.inventoryNumber;
    }

    /*
        Idea: We can get the brand model from product details here and concat its ID from the DB behind it.
        That way we will always have meaningful inventory number and it will always be unique because of the
        numbers concated behind the brand model text.
     */
    public void setInventoryNumber(String inventoryNumber) {
        if (inventoryNumber == null || inventoryNumber.trim().isEmpty()) {
            inventoryNumber = "Product inventory number";
            throw new IllegalArgumentException(String.format(NULL_OR_EMPTY_EXCEPTION_MESSAGE, inventoryNumber));
        }
        if (inventoryNumber.length() > 30) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_30_EXCEPTION_MESSAGE);
        }
        if (inventoryNumber.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.inventoryNumber = inventoryNumber;
    }

    public String getReceivedDate() {
        return this.receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        if (receivedDate == null || receivedDate.trim().isEmpty()) {
            receivedDate = "Product received date";
            throw new IllegalArgumentException(String.format(NULL_OR_EMPTY_EXCEPTION_MESSAGE, receivedDate));
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
        String createdOnFormatted = receivedDate.trim(); // remove the whitespaces
        if (!(pattern.matches(createdOnFormatted))) {
            throw new IllegalArgumentException(String.format(INVALID_DATE_PATTERN_MESSAGE_EXCEPTION, receivedDate));
        }
        this.receivedDate = receivedDate;
    }

    public Integer getAvailable() {
        return this.available;
    }

    public void setAvailable(Integer available) {
        String availableToString = available.toString();
        if (availableToString == null || availableToString.trim().isEmpty()) {
            availableToString = "Product availablility";
            throw new IllegalArgumentException(String.format(NULL_OR_EMPTY_EXCEPTION_MESSAGE, availableToString));
        }
        if (!(available.equals(0) || available.equals(1))) {
            throw new IllegalArgumentException(String.format(INVALID_AVAILABLE_EXCEPTION_MESSAGE, available));
        }
        this.available = available;
    }
}

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


import static constants.ExceptionMessagesConstants.*;

@Entity
@Table(name = "scrapped_products")
public class ScrappedProducts implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "stockroom_id", nullable = false)
    private ScrappedProducts scrappedProductsStockroom;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ScrappedProducts scrappedProductsProduct;

    @Column(name = "scrapped_date", nullable = false)
    private String scrappedDate;

    public ScrappedProducts() {
        scrappedDate = "";
    }

    public ScrappedProducts(String scrappedDate) {
        setScrappedDate(scrappedDate);
    }

    public int getId() {
        return this.id;
    }

    public String getScrappedDate() {
        return this.scrappedDate;
    }

    public void setScrappedDate(String scrappedDate) {
        if (scrappedDate == null || scrappedDate.trim().isEmpty()) {
            scrappedDate = "User created on";
            throw new IllegalArgumentException(String.format(scrappedDate, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
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
        String createdOnFormatted = scrappedDate.trim(); // remove the whitespaces
        if (!(pattern.matches(createdOnFormatted))) {
            throw new IllegalArgumentException(String.format(scrappedDate, INVALID_DATE_PATTERN_MESSAGE_EXCEPTION));
        }
        this.scrappedDate = scrappedDate;
    }
}
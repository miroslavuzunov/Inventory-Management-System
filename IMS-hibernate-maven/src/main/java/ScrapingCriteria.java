import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import static constants.ExceptionMessagesConstants.*;

@Entity
@Table(name = "scraping_criteria")
public class ScrapingCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "criteria", nullable = false)
    private String criteria;

    @OneToMany(mappedBy = "product_details")
    private List<ScrapingCriteria> scrapingCriteriaList; // Somewhere to store them maybe?

//    public ScrapingCriteria() {
//        criteria = "";
//    }

    public ScrapingCriteria(String criteria) {
        setCriteria(criteria);
    }

    public int getId() {
        return this.id;
    }

    public String getCriteria() {
        return this.criteria;
    }

    public void setCriteria(String criteria) {
        if (criteria == null || criteria.trim().isEmpty()) {
            String criteriaString = "Scrapping criteria criteria ";
            throw new IllegalArgumentException(String.format(NULL_OR_EMPTY_EXCEPTION_MESSAGE, criteriaString));
        }
        if (criteria.length() > 45) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_45_EXCEPTION_MESSAGE);
        }
        if (criteria.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.criteria = criteria;
    }

}

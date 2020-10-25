import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import static constants.ExceptionMessagesConstants.*;

@Entity
@Table(name = "depreciation_dg")
public class DepreciationDegree implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "category", nullable = false, unique = true)
    private char category;

    @Column(name = "percentage", nullable = false, unique = true)
    private Float percentage;

    @OneToMany(mappedBy = "product_details")
    private List<DepreciationDegree> depreciationDegreeList; // Somewhere to store them maybe?


    public DepreciationDegree() {
        category = ' ';
        percentage = 0.0f;
    }

    public DepreciationDegree(char category, Float percentage) {
        setCategory(category);
        setPercentage(percentage);
    }

    public int getId() {
        return this.id;
    }

    public char getCategory() {
        return this.category;
    }

    public void setCategory(char category) {

        String categoryToString = String.valueOf(category);

        if (categoryToString == null || categoryToString.trim().isEmpty()) {
            String categoryString = "Depreciation degree category ";
            throw new IllegalArgumentException(String.format(categoryString, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (categoryToString.length() != 1) {
            throw new IllegalArgumentException(INVALID_DIFFERENT_THAN_1_EXCEPTION_MESSAGE);
        }

        this.category = category;
    }

    public Float getPercentage() {
        return this.percentage;
    }

    public void setPercentage(Float percentage) {

        String percentageToString = percentage.toString();

        if (percentageToString == null || percentageToString.trim().isEmpty()) {
            String percentageString = "Depreciation degree percentage ";
            throw new IllegalArgumentException(String.format(percentageString, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (percentageToString.length() > 3) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_3_EXCEPTION_MESSAGE);
        }
        if (percentageToString.length() < 1) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_1_EXCEPTION_MESSAGE);
        }
        this.percentage = percentage;
    }
}



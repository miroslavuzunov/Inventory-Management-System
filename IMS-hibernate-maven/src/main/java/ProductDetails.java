import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import static constants.ExceptionMessagesConstants.*;

@Entity
@Table(name = "product_details")
public class ProductDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "brand_model", nullable = false, unique = true)
    private String brandModel;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "product_type", nullable = false)
    private String productType;

    @ManyToOne
    @JoinColumn(name = "product_group_id", nullable = false)
    private ProductGroup productGroup;

    @ManyToOne
    @JoinColumn(name = "depreciation_dg_id", nullable = false)
    private DepreciationDegree depreciationDegree;

    @ManyToOne
    @JoinColumn(name = "scraping_criteria_id", nullable = false)
    private ScrapingCriteria scrapingCriteria;

    @OneToMany(mappedBy = "product")
    private List<ProductDetails> productDetailsList; // Somewhere to store them maybe?

    public ProductDetails() {
        brandModel = "";
        price = 0.0f;
        description = "";
        productType = "";
    }

    public ProductDetails(String brandModel, Float price, String description, String productType) {
        setBrandModel(brandModel);
        setPrice(price);
        setDescription(description);
        setProductType(productType);
    }

    public int getId() {
        return this.id;
    }

    public String getBrandModel() {
        return this.brandModel;
    }

    public void setBrandModel(String brandModel) {
        if (brandModel == null || brandModel.trim().isEmpty()) {
            brandModel = "Product details brand model";
            throw new IllegalArgumentException(String.format(NULL_OR_EMPTY_EXCEPTION_MESSAGE, brandModel));
        }
        if (brandModel.length() > 200) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_200_EXCEPTION_MESSAGE);
        }
        if (brandModel.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.brandModel = brandModel;
    }

    public Float getPrice() {
        return this.price;
    }

    public void setPrice(Float price) {
        String priceToString = price.toString();

        if (priceToString == null || priceToString.trim().isEmpty()) {
            String percentageString = "Product details price";
            throw new IllegalArgumentException(String.format(NULL_OR_EMPTY_EXCEPTION_MESSAGE, percentageString));
        }
        if (priceToString.length() > 3) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_3_EXCEPTION_MESSAGE);
        }
        if (priceToString.length() < 1) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_1_EXCEPTION_MESSAGE);
        }

        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            description = "Product details description";
            throw new IllegalArgumentException(String.format(NULL_OR_EMPTY_EXCEPTION_MESSAGE, description));
        }
        if (description.length() > 200) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_200_EXCEPTION_MESSAGE);
        }
        if (description.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.description = description;
    }

    public String getProductType() {
        return this.productType;
    }

    public void setProductType(String productType) {
        if (productType == null || productType.trim().isEmpty()) {
            productType = "Product details product type";
            throw new IllegalArgumentException(String.format(NULL_OR_EMPTY_EXCEPTION_MESSAGE,productType));
        }
        if (!(productType.equals("LTTA") || productType.equals("TA"))) {
            throw new IllegalArgumentException(String.format(INVALID_ROLE_EXCEPTION_MESSAGE, productType));
        }
        this.productType = productType;
    }
}
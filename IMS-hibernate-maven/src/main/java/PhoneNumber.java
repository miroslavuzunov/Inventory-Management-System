import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import static constants.ExceptionMessagesConstants.*;

@Entity
@Table(name = "phone_number")
public class PhoneNumber implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "phone_type", nullable = false)
    private String phoneType;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PhoneNumber() {
        phoneType = "";
        number = "";
    }

    public PhoneNumber(String phoneType, String number) {
       setPhoneType(phoneType);
       setNumber(number);
    }

    public int getId() {
        return this.id;
    }

    public String getPhoneType() {

        return this.phoneType;
    }

    public void setPhoneType(String phoneType) {
        if (phoneType == null || phoneType.trim().isEmpty()) {
            phoneType = "Phone number phone type";
            throw new IllegalArgumentException(String.format(phoneType, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (phoneType.length() > 8) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_8_EXCEPTION_MESSAGE);
        }
        if (phoneType.length() < 6) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_6_EXCEPTION_MESSAGE);
        }
        if (!(phoneType.equals("PERSONAL") || phoneType.equals("OFFICE"))) {
            throw new IllegalArgumentException(String.format(phoneType, INVALID_ROLE_EXCEPTION_MESSAGE));
        }
        this.phoneType = phoneType;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
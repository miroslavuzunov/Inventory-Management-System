import sun.jvm.hotspot.debugger.AddressException;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.mail.internet;

import static constants.ExceptionMessagesConstants.*;

@Entity
@Table(name = "person_info")
public class PersonInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String fName;

    @Column(name = "last_name", nullable = false)
    private String lName;

    @Column(name = "egn", nullable = false, unique = true)
    private String EGN;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "person_info")
    private List<PersonInfo> personInfoList; // Somewhere to store them maybe?

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
        } catch (AddressException e) {
            result = false;
        }
        return result;
    }

    public PersonInfo() {
        fName = "";
        lName = "";
        EGN = "";
        email = "";
    }

    public PersonInfo(String first_name, String last_name, String egn, String email) {
        setfName(first_name);
        setlName(last_name);
        setEGN(egn);
        setEmail(email);
    }

    public int getId() {
        return this.id;
    }

    public String getfName() {
        return this.fName;
    }

    public void setfName(String fName) {
        if (fName == null || fName.trim().isEmpty()) {
            fName = "Person info first name";
            throw new IllegalArgumentException(String.format(fName, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (fName.length() > 100) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_100_EXCEPTION_MESSAGE);
        }
        if (fName.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.fName = fName;
    }

    public String getlName() {
        return this.lName;
    }

    public void setlName(String lName) {
        if (lName == null || lName.trim().isEmpty()) {
            lName = "Person info last name";
            throw new IllegalArgumentException(String.format(lName, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (lName.length() > 100) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_100_EXCEPTION_MESSAGE);
        }
        if (lName.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.lName = lName;
    }

    public String getEGN() {
        return this.EGN;
    }

    public void setEGN(String EGN) {
        if (EGN == null || EGN.trim().isEmpty()) {
            EGN = "Person info EGN";
            throw new IllegalArgumentException(String.format(EGN, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (EGN.length() != 10) {
            throw new IllegalArgumentException(INVALID_LENGTH_10_EXCEPTION_MESSAGE);
        }
        this.EGN = EGN;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            email = "Person info Email";
            throw new IllegalArgumentException(String.format(email, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        boolean checkValid = isValidEmailAddress(email);
        if (!checkValid) {
            throw new IllegalArgumentException(String.format(email, INVALID_EMAIL_EXCEPTION_MESSAGE));
        }
        this.email = email;
    }

//    public Address getAddress() {
//        return this.address;
//    }
//
//    public void setAddress(Address address) {
//        this.address = address;
//    }
}

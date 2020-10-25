import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import static constants.ExceptionMessagesConstants.*;


@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // This is going to auto create our ID`s
    //@GeneratedValue(strategy = GenerationType.IDENTITY) // If we want to add explicitly the ID
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_on", nullable = false)
    private String createdOn;

    @Column(name = "role", nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "person_info_id", nullable = false)
    private PersonInfo personInfo;

    @OneToMany(mappedBy = "phone_number")
    private List<User> UserList; // Somewhere to store them maybe?

    @OneToOne(mappedBy = "stockroom")
    private List<User> stockroomList;

    @OneToMany(mappedBy = "product_client")
    private List<User> userProductClientMrtList; // Somewhere to store them maybe?

    @OneToMany(mappedBy = "product_client")
    private List<User> userProductClientClientList; // Somewhere to store them maybe?

    public User() {
        nickname = "";
        password = "";
        createdOn = "";
        role = "";
    }

    public User(String nickname, String password, String createdOn, String role) {
        setNickname(nickname);
        setPassword(password);
        setCreatedOn(createdOn);
        setRole(role);
    }

    public int getId() {
        return this.id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = "User nickname";
            throw new IllegalArgumentException(String.format(nickname, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (nickname.length() > 100) {
            throw new IllegalArgumentException(INVALID_LENGTH_GREATER_THAN_100_EXCEPTION_MESSAGE);
        }
        if (nickname.length() < 2) {
            throw new IllegalArgumentException(INVALID_LENGTH_LOWER_THAN_2_EXCEPTION_MESSAGE);
        }
        this.nickname = nickname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            password = "User password";
            throw new IllegalArgumentException(String.format(password, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }
        if (password.length() > 32) {
            throw new IllegalArgumentException(INVALID_LENGTH_32_EXCEPTION_MESSAGE);
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException(INVALID_LENGTH_8_EXCEPTION_MESSAGE);
        }
        /*
            Regex explanation:
            ^                   # start of string
            (?=.*[0-9])         # a digit must occur at least once
            (?=.*[a-z])         # a lower case letter must occur at least once
            (?=.*[A-Z])         # a upper case letter must occur at least once
            (?=.*[!@#$%^&+=])   # a special character must occur at least once
            (?=\S+$)            # no whitespace allowed in the entire string
            .{8,}               # anything, at least eight places though
            $                   # end of string
         */
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$";
        if (!(password.matches(pattern))) {
            throw new IllegalArgumentException(String.format(password, INVALID_PASSWORD_REQUIREMENTS_EXCEPTION_MESSAGE));
        }
        this.password = password;
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

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        //TODO: Validate that it is one of the 3 roles
        if (role == null || role.trim().isEmpty()) {
            role = "User role";
            throw new IllegalArgumentException(String.format(role, NULL_OR_EMPTY_EXCEPTION_MESSAGE));
        }

        if (!(role.equals("ADMIN") || role.equals("MRT") || role.equals("CLIENT"))) {
            throw new IllegalArgumentException(String.format(role, INVALID_ROLE_EXCEPTION_MESSAGE));
        }
            this.role = role;
    }
}
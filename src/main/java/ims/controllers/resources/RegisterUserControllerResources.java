package ims.controllers.resources;

import ims.controllers.primary.SceneController;
import ims.controllers.contracts.ControllerConfig;
import ims.controllers.secondary.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public abstract class RegisterUserControllerResources extends AbstractController implements ControllerConfig {
    public static final String BUSY_USERNAME_MSG = "Username is already used!";
    public static final String PASSWORDS_DONT_MATCH_MSG = "Passwords don't match!";
    public static final String BUSY_EMAIL_MSG = "Email is already used!";
    public static final String INVALID_EGN_MSG = "Invalid EGN!";
    public static final String BUSY_EGN_MSG = "EGN is already used!";
    public static final String USERNAME_FIELD_NAME = "Username";
    public static final String PASSWORD_FIELD_NAME = "Password";
    public static final String REPEAT_PASSWORD_FIELD_NAME = "Repeat password";
    public static final String EMAIL_FIELD_NAME = "E-mail";
    public static final String FIRST_NAME_FIELD_NAME = "First name";
    public static final String LAST_NAME_FIELD_NAME = "Last name";
    public static final String EGN_FIELD_NAME = "EGN";
    public static final String COUNTRY_FIELD_NAME = "Country";
    public static final String CITY_FIELD_NAME = "City";
    public static final String STREET_FIELD_NAME = "Street";
    public static final String ADDRESS_DETAILS_FIELD_NAME = "Address details";
    public static final String PHONE_NUMBER_FIELD_NAME = "Phone number";
    public static final String PHONE_TYPE_FIELD_NAME = "Phone type";

    @FXML
    protected AnchorPane regPane;

    @FXML
    protected TextField usernameField;

    @FXML
    protected Label usernameMsg;

    @FXML
    protected PasswordField passwordField;

    @FXML
    protected Label passwordMsg;

    @FXML
    protected PasswordField repeatPasswordField;

    @FXML
    protected Label repeatPasswordMsg;

    @FXML
    protected TextField emailField;

    @FXML
    protected Label emailMsg;

    @FXML
    protected TextField firstNameField;

    @FXML
    protected Label firstNameMsg;

    @FXML
    protected TextField lastNameField;

    @FXML
    protected Label lastNameMsg;

    @FXML
    protected TextField egnField;

    @FXML
    protected Label regEgnMsg;

    @FXML
    protected ComboBox<String> countryComboBox;

    @FXML
    protected ComboBox<String> cityComboBox;

    @FXML
    protected Label countryMsg;

    @FXML
    protected Label cityMsg;

    @FXML
    protected TextField streetField;

    @FXML
    protected Label streetMsg;

    @FXML
    protected TextField addressDetailsField;

    @FXML
    protected Label addressDetailsMsg;

    @FXML
    protected RadioButton personalPhoneRadioBtn;

    @FXML
    protected RadioButton officePhoneRadioBtn;

    @FXML
    protected TextField phoneNumberField;

    @FXML
    protected Label phoneNumberMsg;

    @FXML
    protected Button signUpBtn;

    @FXML
    protected VBox cleanLeftPane;

    @FXML
    protected Button backBtn;

    @FXML
    protected Label header;

    @Override
    public void initializeScenes() {
        SceneController.loadScenes(new HashMap<>() {{
            put(backBtn, "Admin");
        }});
    }

}

package ims.controllers.resources;

import ims.controllers.SceneController;
import ims.controllers.contracts.ControllerConfig;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public abstract class RegisterMrtControllerResources implements ControllerConfig {
    public static final String BUSY_USERNAME_MSG = "Username is already used!";
    protected static final String PASSWORDS_DONT_MATCH_MSG = "Passwords don't match!";
    public static final String BUSY_EMAIL_MSG = "Email is already used!";
    public static final String INVALID_EGN_MSG = "Invalid EGN!";
    public static final String BUSY_EGN_MSG = "EGN is already used!";
    protected static final String EMPTY_FIELD_MSG = "Empty field forbidden!";
    protected static final String CLEAN_MSG = "";
    public static final String USERNAME_FIELD_NAME = "Username";
    protected static final String PASSWORD_FIELD_NAME = "Password";
    protected static final String REPEAT_PASSWORD_FIELD_NAME = "Repeat password";
    public static final String EMAIL_FIELD_NAME = "E-mail";
    protected static final String FIRST_NAME_FIELD_NAME = "First name";
    protected static final String LAST_NAME_FIELD_NAME = "Last name";
    public static final String EGN_FIELD_NAME = "EGN";
    protected static final String COUNTRY_FIELD_NAME = "Country";
    protected static final String CITY_FIELD_NAME = "City";
    protected static final String STREET_FIELD_NAME = "Street";
    protected static final String ADDRESS_DETAILS_FIELD_NAME = "Address details";
    protected static final String PHONE_NUMBER_FIELD_NAME = "Phone number";

    @FXML
    protected AnchorPane regPane;

    @FXML
    protected TextField mrtRegUsernameField;

    @FXML
    protected Label mrtRegUsernameMsg;

    @FXML
    protected PasswordField mrtRegPasswordField;

    @FXML
    protected Label mrtRegPasswordMsg;

    @FXML
    protected Label mrtRegRepeatPasswordMsg;

    @FXML
    protected PasswordField mrtRegRepeatPasswordField;

    @FXML
    protected TextField mrtRegEmailField;

    @FXML
    protected Label mrtRegEmailMsg;

    @FXML
    protected TextField mrtRegFirstNameField;

    @FXML
    protected Label mrtRegFirstNameMsg;

    @FXML
    protected TextField mrtRegLastNameField;

    @FXML
    protected Label mrtRegLastNameMsg;

    @FXML
    protected TextField mrtRegEgnField;

    @FXML
    protected Label mrtRegEgnMsg;

    @FXML
    protected ComboBox<String> countryComboBox;

    @FXML
    protected ComboBox<String> cityComboBox;

    @FXML
    protected Label mrtRegCountryMsg;

    @FXML
    protected Label mrtRegCityMsg;

    @FXML
    protected TextField mrtRegStreetField;

    @FXML
    protected Label mrtRegStreetMsg;

    @FXML
    protected TextField mrtRegDetailsField;

    @FXML
    protected Label mrtRegAddressMsg;

    @FXML
    protected RadioButton mrtRegPersonalPhoneRadioBtn;

    @FXML
    protected RadioButton mrtRegOfficePhoneRadioBtn;

    @FXML
    protected TextField mrtRegPhoneNumberField;

    @FXML
    protected Label mrtRegPhoneNumberMsg;

    @FXML
    protected Button signUpBtn;

    @FXML
    protected VBox cleanLeftPane;

    @FXML
    protected Button backBtn;

    @Override
    public void initializeScenes() {
        SceneController.loadScenes(new HashMap<>() {{
            put(backBtn, "Admin");
        }});
    }

}

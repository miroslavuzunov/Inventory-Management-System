package ims.controllers.resources;

import ims.controllers.SceneController;
import ims.controllers.contracts.ControllerConfig;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public abstract class RegisterMrtControllerResources implements ControllerConfig {
    protected static final String BUSY_USERNAME_MSG = "Username is busy!";
    protected static final String PASSWORDS_DONT_MATCH_MSG = "Passwords don't match!";
    protected static final String BUSY_EMAIL_MSG = "Email is already used!";
    protected static final String INVALID_EGN_MSG = "Invalid EGN!";
    protected static final String EMPTY_FIELD_MSG = "Empty field!";

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

    @FXML
    protected ComboBox<String> test;

    @Override
    public void initializeScenes(){
        SceneController.loadScenes(new HashMap<>(){{
            put(backBtn, "Admin");
        }});
    }
}

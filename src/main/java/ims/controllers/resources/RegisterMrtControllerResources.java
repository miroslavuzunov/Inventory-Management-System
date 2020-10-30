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

    @FXML
    protected AnchorPane regPane;

    @FXML
    protected TextField mrtRegUsernameField;

    @FXML
    protected Label mrtRegUsernameMsg;

    @FXML
    protected PasswordField mrtRegPasswordField;

    @FXML
    protected PasswordField mrtRegRepeatPasswordField;

    @FXML
    protected Label mrtRegPasswordMsg;

    @FXML
    protected TextField mrtRegEmailField;

    @FXML
    protected Label mrtRegEmailMsg;

    @FXML
    protected TextField mrtRegFirstNameField;

    @FXML
    protected TextField mrtRegLastNameField;

    @FXML
    protected TextField mrtRegEgnField;

    @FXML
    protected Label mrtRegEgnMsg;

    @FXML
    protected ChoiceBox<String> countryChoiceBox;

    @FXML
    protected TextField mrtRegCityField;

    @FXML
    protected TextField mrtRegRegionField;

    @FXML
    protected TextField mrtRegStreetField;

    @FXML
    protected TextField mrtRegDetailsField;

    @FXML
    protected VBox cleanLeftPane;

    @FXML
    protected Button signUpBtn;

    @FXML
    protected Button backBtn;

    @Override
    public void initializeScenes(){
        SceneController.loadScenes(new HashMap<>(){{
            put(backBtn, "Admin");
        }});
    }
}

package ims.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class AdminControllerResources {
    protected static final String BUSY_USERNAME_MSG = "Username is busy!";
    protected static final String PASSWORDS_DONT_MATCH_MSG = "Passwords don't match!";
    protected static final String BUSY_EMAIL_MSG = "Email is already used!";
    protected static final String INVALID_EGN_MSG = "Invalid EGN!";


    @FXML
    protected Pane fadeMain;

    @FXML
    protected AnchorPane mainPane;

    @FXML
    protected Label nameLabel;

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
    protected VBox clientManipPane;

    @FXML
    protected Button regCardBtn;

    @FXML
    protected Button addProductBtn;

    @FXML
    protected Button removeProductBtn;

    @FXML
    protected Button checkClientProductsBtn;

    @FXML
    protected VBox genRefPane;

    @FXML
    protected Button allProductsBtn;

    @FXML
    protected Button checkStatusBtn;

    @FXML
    protected Button productsByCategoryBtn;

    @FXML
    protected Button scrappedProductsBtn;

    @FXML
    protected VBox leftPane;

    @FXML
    protected VBox cleanLeftPane;

    @FXML
    protected Button regMrtBtn;

    @FXML
    protected Button notificationBtn;

    @FXML
    protected Button genRefBtn;

    @FXML
    protected Button stockRefBtn;

    @FXML
    protected Button regProductBtn;

    @FXML
    protected Button clientManipBtn;

    @FXML
    protected Label todaysDate;

    @FXML
    protected Label userStatus;
}

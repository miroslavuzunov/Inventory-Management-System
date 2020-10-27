package ims.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import ims.App;
import ims.entities.PersonInfo;
import ims.entities.User;
import ims.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;


public class AdminController implements Initializable {

    @FXML
    private Pane fadeMain;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private VBox clientManipPane;

    @FXML
    private Button regCardBtn;

    @FXML
    private Button addProductBtn;

    @FXML
    private Button removeProductBtn;

    @FXML
    private Button checkClientProductsBtn;

    @FXML
    private VBox genRefPane;

    @FXML
    private Button allProductsBtn;

    @FXML
    private Button checkStatusBtn;

    @FXML
    private Button productsByCategoryBtn;

    @FXML
    private Button scrappedProductsBtn;

    @FXML
    private VBox leftPane;

    @FXML
    private Button regMrtBtn;

    @FXML
    private Button genRefBtn;

    @FXML
    private Button stockRefBtn;

    @FXML
    private Button regProductBtn;

    @FXML
    private Button clientManipBtn;

    @FXML
    private Button notificationBtn;

    @FXML
    private Label todaysDate;

    @FXML
    private Label userStatus;

    @FXML
    private Label nameLabel;

    @FXML
    private AnchorPane regPane;

    @FXML
    private ChoiceBox<String> countryChoiceBox;

    private static String adminFirstName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userStatus.setText("Status: ADMIN");
        todaysDate.setText("Today's date: " + LocalDate.now().toString());
        nameLabel.setText("Hello, "+ adminFirstName + "!");
    }

    @FXML
    private void exit() throws IOException {
        App.setRoot("/view/LoginPanel");
    }

    @FXML
    private void handleClicks(ActionEvent event) {
        if (event.getSource() == genRefBtn) {
            mainPane.setDisable(true);
            mainPane.toFront();
            fadeMain.toFront();
            genRefPane.toFront();
        }
        if (event.getSource() == clientManipBtn) {
            mainPane.setDisable(true);
            mainPane.toFront();
            fadeMain.toFront();
            clientManipPane.toFront();
        }
        if(event.getSource() == regMrtBtn){
            mainPane.setDisable(true);
            countryChoiceBox.getItems().add("Bulgaria"); //TODO Loading from DB
            regPane.toFront();
        }
    }

    public void backToLeftHome(ActionEvent event) {
        mainPane.setDisable(false);
        mainPane.toFront();
        leftPane.toFront();
    }

    public static void passUserFirstName(User user){
        adminFirstName = user.getPersonInfo().getFirstName();
    }

//    public static void disableAdminButton(){
//        leftPane.getChildren().remove(regMrtBtn);
//    }

}

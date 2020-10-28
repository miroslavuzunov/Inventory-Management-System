package ims.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import ims.App;
import ims.entities.PersonInfo;
import ims.entities.User;
import ims.services.UserRegistrationService;
import ims.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;


public class AdminController extends AdminControllerResources implements Initializable {
    private static User loggedUser;
    private UserRegistrationService userRegistrationService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userStatus.setText("Status: ADMIN");
        todaysDate.setText("Today's date: " + LocalDate.now().toString());
        nameLabel.setText("Hello, "+ loggedUser.getPersonInfo().getFirstName() + "!");
    }

    @FXML
    private void exit() throws IOException {
        App.setRoot("/view/Login");
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
            userRegistrationService = new UserRegistrationService();
            userRegistrationService.initializeCountries(countryChoiceBox);
            mainPane.setDisable(true);
            regPane.toFront();
        }
    }

    @FXML
    public void backToLeftHome(ActionEvent event) {
        mainPane.setDisable(false);
        mainPane.toFront();
        leftPane.toFront();
    }

    public void signUp(ActionEvent event) {
        //TODO confirmation prompt

        if(userRegistrationService.checkIfUsernameExists(mrtRegUsernameField.getText()))
            mrtRegUsernameMsg.setText("Username is busy!");
        else
            mrtRegUsernameMsg.setText("");
        if(!userRegistrationService.checkIfPasswordsMatch(mrtRegPasswordField.getText(), mrtRegRepeatPasswordField.getText()))
            mrtRegPasswordMsg.setText("Passwords don't match!");
        else
            mrtRegPasswordMsg.setText("");
        if(userRegistrationService.checkIfEmailIsUsed(mrtRegEmailField.getText()))
            mrtRegEmailMsg.setText("Email is already used!");
        else
            mrtRegEmailMsg.setText("");
        if(!userRegistrationService.checkIfEgnIsValid(mrtRegEgnField.getText()))
            mrtRegEgnMsg.setText("Invalid EGN!");
        else
            mrtRegEgnMsg.setText("");
    }

    public static void passUser(User passedUser){
        loggedUser = passedUser;
    }



}

package ims.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ims.App;
import ims.dialogs.ConfirmationDialog;
import ims.entities.User;
import ims.services.UserRegistrationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;


public class AdminController extends AdminControllerResources implements Initializable {
    private static User loggedUser;
    private UserRegistrationService userRegistrationService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userStatus.setText("Status: ADMIN");
        todaysDate.setText("Today's date: " + LocalDate.now().toString());
        nameLabel.setText("Hello, " + loggedUser.getPersonInfo().getFirstName() + "!");

        userRegistrationService = new UserRegistrationService();
        userRegistrationService.initializeCountries(countryChoiceBox);
    }

    @FXML
    private void handleClicks(ActionEvent event) {
        switchScene(event.getSource() == genRefBtn,
                Stream.of(
                        mainPane,
                        fadeMain,
                        genRefPane
                ));

        switchScene(event.getSource() == clientManipBtn,
                Stream.of(
                        mainPane,
                        fadeMain,
                        clientManipPane
                ));

        switchScene(event.getSource() == regMrtBtn,
                Stream.of(
                        mainPane,
                        cleanLeftPane,
                        regPane
                ));
    }

    @FXML
    public void backToSideHome(ActionEvent event) {
        switchScene(true,
                Stream.of(
                        mainPane,
                        leftPane
                ));
    }

    @FXML
    public void signUp(ActionEvent event) throws InvocationTargetException, IllegalAccessException {
        List<String> errorMessages = new ArrayList<>();

        validateFields(errorMessages);

        if (errorMessages.isEmpty())
            ConfirmationDialog.confirm("Are you sure you want to sign up?");
    }

    @FXML
    private void exit() throws IOException {
        App.setRoot("/view/Login");
    }

    private void validateFields(List<String> errorMessages) {
        checkError(errorMessages,
                userRegistrationService.checkIfUsernameExists(mrtRegUsernameField.getText()),
                mrtRegUsernameMsg,
                BUSY_USERNAME_MSG
        );
        checkError(errorMessages,
                !userRegistrationService.checkIfPasswordsMatch(mrtRegPasswordField.getText(), mrtRegRepeatPasswordField.getText()),
                mrtRegPasswordMsg,
                PASSWORDS_DONT_MATCH_MSG
        );
        checkError(errorMessages,
                userRegistrationService.checkIfEmailIsUsed(mrtRegEmailField.getText()),
                mrtRegEmailMsg,
                BUSY_EMAIL_MSG
        );
        checkError(errorMessages,
                !userRegistrationService.checkIfEgnIsValid(mrtRegEgnField.getText()),
                mrtRegEgnMsg,
                INVALID_EGN_MSG
        );
    }

    private void switchScene(boolean statement, Stream<Parent> parentWindows) {
        List<Parent> parentWindowsList = parentWindows.collect(Collectors.toList());

        if (statement)
            parentWindowsList.forEach(window -> {
                window.toFront();
            });
    }

    private void checkError(List<String> errorMessages, boolean statement, Label messageLabel, String message) {
        if (statement) {
            messageLabel.setText(message);
            errorMessages.add(message);
        } else {
            messageLabel.setText("");
            errorMessages.removeIf(str -> str.equals(message));
        }
    }

    public static void passUser(User passedUser) {
        loggedUser = passedUser;
    }

}

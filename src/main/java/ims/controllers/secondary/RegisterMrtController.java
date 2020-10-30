package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.controllers.resources.RegisterMrtControllerResources;
import ims.dialogs.ConfirmationDialog;
import ims.services.UserRegistrationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterMrtController extends RegisterMrtControllerResources implements Initializable {
    private UserRegistrationService userRegistrationService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userRegistrationService = new UserRegistrationService();
        userRegistrationService.initializeCountries(countryChoiceBox);

        initializeScenes();
    }

    @FXML
    public void handleClicks(ActionEvent event) throws IOException{
        if(event.getSource() == signUpBtn)
            signUp();

        SceneController.switchSceneByButton((Button)event.getSource());
    }

    public void signUp(){
        List<String> errorMessages = new ArrayList<>();

        validateFields(errorMessages);

        if (errorMessages.isEmpty())
            ConfirmationDialog.confirm("Are you sure you want to sign up?");
    }

    private void validateFields(List<String> errorMessages) {
        validateUsernameField(errorMessages);
        validatePasswordFields(errorMessages);
        validateEmailField(errorMessages);
        validateEgnField(errorMessages);
    }

    private void validateUsernameField(List<String> errorMessages){
        checkForError(errorMessages,
                userRegistrationService.checkIfUsernameExists(mrtRegUsernameField.getText()),
                mrtRegUsernameMsg,
                BUSY_USERNAME_MSG
        );
    }

    private void validatePasswordFields(List<String> errorMessages){
        checkForError(errorMessages,
                !userRegistrationService.checkIfPasswordsMatch(mrtRegPasswordField.getText(), mrtRegRepeatPasswordField.getText()),
                mrtRegPasswordMsg,
                PASSWORDS_DONT_MATCH_MSG
        );
    }

    private void validateEmailField(List<String> errorMessages) {
        checkForError(errorMessages,
                userRegistrationService.checkIfEmailIsUsed(mrtRegEmailField.getText()),
                mrtRegEmailMsg,
                BUSY_EMAIL_MSG
        );
    }

    private void validateEgnField(List<String> errorMessages) {
        checkForError(errorMessages,
                !userRegistrationService.checkIfEgnIsValid(mrtRegEgnField.getText()),
                mrtRegEgnMsg,
                INVALID_EGN_MSG
        );
    }

    private void checkForError(List<String> errorMessages, boolean statement, Label messageLabel, String message) {
        if (statement) {
            messageLabel.setText(message);
            errorMessages.add(message);
        } else {
            messageLabel.setText("");
            errorMessages.removeIf(str -> str.equals(message));
        }
    }

}

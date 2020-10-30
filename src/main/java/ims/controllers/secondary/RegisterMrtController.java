package ims.controllers.secondary;

import ims.controllers.SceneController;
import ims.controllers.resources.RegisterMrtControllerResources;
import ims.entities.*;
import ims.enums.Role;
import ims.services.UserRegistrationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
    public void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == signUpBtn)
            signUp();

        SceneController.switchSceneByButton((Button) event.getSource());
    }

    public void signUp() {
        List<String> errorMessages = new ArrayList<>();

        validateFields(errorMessages);

//        if (errorMessages.isEmpty())
//            ConfirmationDialog.confirm("Are you sure you want to sign up?");

        createUser();
    }

    private void createUser() {
        Country country = new Country();
        City city = new City();
        Address address = new Address();
        PersonInfo personInfo = new PersonInfo();
        User user = new User();


        //TODO COMPLETE METHOD

        country.setName(countryChoiceBox.getSelectionModel().getSelectedItem());

        city.setName(mrtRegCityField.getText());
        city.setRegion(mrtRegRegionField.getText());

        //address.setCountry(country);
        //address.setCity(city);
        address.setStreet(mrtRegStreetField.getText());
        address.setDetails(mrtRegDetailsField.getText());


        userRegistrationService.beginTransaction();

        if (userRegistrationService.getCityId(city) == null)
            address.setCity(city);
        else
            userRegistrationService.setUserAddressCity(address, userRegistrationService.getCityId(city));


        userRegistrationService.setUserAddressCountry(address,
                userRegistrationService.getCountryId(country));

        userRegistrationService.commitTransaction();


        //personInfo.setAddress(address);
        personInfo.setFirstName(mrtRegFirstNameField.getText());
        personInfo.setLastName(mrtRegLastNameField.getText());
        personInfo.setEgn(mrtRegEgnField.getText());


        user.setPersonInfo(personInfo);
        user.setNickname(mrtRegUsernameField.getText());
        user.setPassword(mrtRegPasswordField.getText());
        user.setEmail(mrtRegEmailField.getText());
        user.setRole(Role.MRT);
        user.setCreatedOn(LocalDate.now());

        //userRegistrationService.saveUser(user);
    }

    private void validateFields(List<String> errorMessages) {
        validateUsernameField(errorMessages);
        validatePasswordFields(errorMessages);
        validateEmailField(errorMessages);
        validateEgnField(errorMessages);
    }

    private void validateUsernameField(List<String> errorMessages) {
        checkForError(errorMessages,
                userRegistrationService.checkIfUsernameExists(mrtRegUsernameField.getText()),
                mrtRegUsernameMsg,
                BUSY_USERNAME_MSG
        );
    }

    private void validatePasswordFields(List<String> errorMessages) {
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

package ims.controllers.secondary;

import ims.controllers.SceneController;
import ims.controllers.resources.RegisterMrtControllerResources;
import ims.dialogs.ConfirmationDialog;
import ims.entities.*;
import ims.enums.PhoneType;
import ims.enums.Role;
import ims.services.UserRegistrationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterMrtController extends RegisterMrtControllerResources implements Initializable {
    private UserRegistrationService userRegistrationService;
    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userRegistrationService = new UserRegistrationService();

        userRegistrationService.initializeCountries(countryComboBox);
        userRegistrationService.initializeCities(countryComboBox, cityComboBox);

        makeToggleGroup(List.of(mrtRegPersonalPhoneRadioBtn, mrtRegOfficePhoneRadioBtn));
        initializeScenes();
    }

    @FXML
    public void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == signUpBtn)
            signUp();

        SceneController.switchSceneByButton((Button) event.getSource());
    }

    public void signUp() {
        List<Parent> invalidFields = new ArrayList<>();

        validateFields(invalidFields);

//        if (invalidFields.isEmpty())
//            ConfirmationDialog.confirm("Are you sure you want to sign up?");

        createUser();
    }

    private void createUser() {
//        Country country = new Country();
//        City city = new City();
//        Address address = new Address();
//        PersonInfo personInfo = new PersonInfo();
//        User user = new User();
//
//
//        //TODO COMPLETE METHOD
//
//        country.setName(countryComboBox.getSelectionModel().getSelectedItem());
//
//        //city.setName();
//        //city.setRegion();
//
//        //address.setCountry(country);
//        //address.setCity(city);
//        address.setStreet(mrtRegStreetField.getText());
//        address.setDetails(mrtRegDetailsField.getText());
//
//
//        userRegistrationService.beginTransaction();
//
//        if (userRegistrationService.getCityId(city) == null)
//            address.setCity(city);
//        else
//            userRegistrationService.setUserAddressCity(address, userRegistrationService.getCityId(city));
//
//
//        userRegistrationService.commitTransaction();
//
//
//        //personInfo.setAddress(address);
//        personInfo.setFirstName(mrtRegFirstNameField.getText());
//        personInfo.setLastName(mrtRegLastNameField.getText());
//        personInfo.setEgn(mrtRegEgnField.getText());
//
//
//        user.setPersonInfo(personInfo);
//        user.setNickname(mrtRegUsernameField.getText());
//        user.setPassword(mrtRegPasswordField.getText());
//        user.setEmail(mrtRegEmailField.getText());
//        user.setRole(Role.MRT);
//        user.setCreatedOn(LocalDate.now());
//
//        //userRegistrationService.saveUser(user);


    }

    private void validateFields(List<Parent> invalidFields) {
        validateUsernameField(invalidFields);
        validatePasswordField(invalidFields);
        validateRepeatPasswordField(invalidFields);
        validateEmailField(invalidFields);
        validateFirstNameField(invalidFields);
        validateLastNameField(invalidFields);
        validateEgnField(invalidFields);
        validateCountryComboBox(invalidFields);
        validateCityComboBox(invalidFields);
        validateStreetField(invalidFields);
        validateAddressDetailsField(invalidFields);
        validatePhoneNumberField(invalidFields);
    }

    private void validateUsernameField(List<Parent> invalidFields) {
        if (!checkIfFieldIsEmpty(mrtRegUsernameField))
            handleError(invalidFields,
                    mrtRegUsernameField,
                    userRegistrationService.checkIfUsernameExists(mrtRegUsernameField.getText()),
                    mrtRegUsernameMsg,
                    BUSY_USERNAME_MSG
            );
        else
            handleEmptyFieldError(invalidFields, mrtRegUsernameField, mrtRegUsernameMsg);
    }

    private void validatePasswordField(List<Parent> invalidFields) {
        handleEmptyFieldError(invalidFields, mrtRegPasswordField, mrtRegPasswordMsg);
    }

    private void validateRepeatPasswordField(List<Parent> invalidFields) {
        if (!checkIfFieldIsEmpty(mrtRegRepeatPasswordField))
            handleError(invalidFields,
                    mrtRegRepeatPasswordField,
                    !userRegistrationService.checkIfPasswordsMatch(mrtRegPasswordField.getText(), mrtRegRepeatPasswordField.getText()),
                    mrtRegRepeatPasswordMsg,
                    PASSWORDS_DONT_MATCH_MSG
            );
        else
            handleEmptyFieldError(invalidFields, mrtRegRepeatPasswordField, mrtRegRepeatPasswordMsg);

    }

    private void validateEmailField(List<Parent> invalidFields) {
        if (!checkIfFieldIsEmpty(mrtRegEmailField))
            handleError(invalidFields,
                    mrtRegEmailField,
                    userRegistrationService.checkIfEmailIsUsed(mrtRegEmailField.getText()),
                    mrtRegEmailMsg,
                    BUSY_EMAIL_MSG
            );
        else
            handleEmptyFieldError(invalidFields, mrtRegEmailField, mrtRegEmailMsg);
    }

    private void validateFirstNameField(List<Parent> invalidFields){
        handleEmptyFieldError(invalidFields, mrtRegFirstNameField, mrtRegFirstNameMsg);
    }

    private void validateLastNameField(List<Parent> invalidFields){
        handleEmptyFieldError(invalidFields, mrtRegLastNameField, mrtRegLastNameMsg);
    }

    private void validateEgnField(List<Parent> invalidFields) {
        if(!checkIfFieldIsEmpty(mrtRegEgnField))
        handleError(invalidFields,
                mrtRegEgnField,
                !userRegistrationService.checkIfEgnIsValid(mrtRegEgnField.getText()),
                mrtRegEgnMsg,
                INVALID_EGN_MSG
        );
        else
            handleEmptyFieldError(invalidFields, mrtRegEgnField, mrtRegEgnMsg);
    }

    private void validateCountryComboBox(List<Parent> invalidFields){
            handleNotSelectedComboBoxError(invalidFields, countryComboBox,mrtRegCountryMsg);
    }

    private void validateCityComboBox(List<Parent> invalidFields) {
        handleNotSelectedComboBoxError(invalidFields, cityComboBox,mrtRegCityMsg);
    }

    private void validateStreetField(List<Parent> invalidFields) {
        handleEmptyFieldError(invalidFields, mrtRegStreetField, mrtRegStreetMsg);
    }

    private void validateAddressDetailsField(List<Parent> invalidFields) {
        handleEmptyFieldError(invalidFields, mrtRegDetailsField, mrtRegAddressMsg);
    }

    private void validatePhoneNumberField(List<Parent> invalidFields) {
        handleEmptyFieldError(invalidFields, mrtRegPhoneNumberField, mrtRegPhoneNumberMsg);
    }


    private void handleError(List<Parent> invalidFields, Parent field, boolean statement, Label messageLabel, String message) {
        if (statement) {
            messageLabel.setText(message);
            invalidFields.add(field);
            field.setStyle("-fx-border-width: 1px;" + "-fx-border-color: red;" + "-fx-border-radius: 3px");
        } else {
            messageLabel.setText("");
            invalidFields.removeIf(str -> str.equals(field));
            field.setStyle("-fx-border-width: 1px;" + "-fx-border-color: lightgrey;" + "-fx-border-radius: 3px");
        }
    }

    private void handleEmptyFieldError(List<Parent> invalidFields, Parent field, Label message) {
        handleError(invalidFields,
                field,
                checkIfFieldIsEmpty((TextField) field),
                message,
                EMPTY_FIELD_MSG
        );
    }

    private void handleNotSelectedComboBoxError(List<Parent> invalidFields, ComboBox<String> box, Label message) {
        handleError(invalidFields,
                box,
                checkIfChoiceBoxIsNotSelected(box),
                message,
                EMPTY_FIELD_MSG
        );
    }


    private void makeToggleGroup(List<RadioButton> phoneTypes) {
        toggleGroup = new ToggleGroup();

        phoneTypes.forEach(radioButton -> {
            radioButton.setToggleGroup(toggleGroup);
        });

        toggleGroup.selectToggle(mrtRegPersonalPhoneRadioBtn);
    }

    private boolean checkIfFieldIsEmpty(TextField field) {
        return field.getText().length() == 0;
    }

    private boolean checkIfChoiceBoxIsNotSelected(ComboBox<String> choiceBox) {
        return choiceBox.getSelectionModel().isEmpty();
    }


}

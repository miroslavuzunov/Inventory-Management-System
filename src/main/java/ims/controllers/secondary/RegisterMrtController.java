package ims.controllers.secondary;

import ims.App;
import ims.controllers.primary.SceneController;
import ims.controllers.resources.RegisterMrtControllerResources;
import ims.dialogs.ConfirmationDialog;
import ims.dialogs.SuccessDialog;
import ims.enums.State;
import ims.services.UserRegistrationService;
import ims.supporting.CustomField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RegisterMrtController extends RegisterMrtControllerResources implements Initializable {
    private UserRegistrationService userRegistrationService;
    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userRegistrationService = new UserRegistrationService();

        initializeScenes();
        makeToggleGroup(List.of(personalPhoneRadioBtn, officePhoneRadioBtn));
        userRegistrationService.initializeCountries(countryComboBox);
        userRegistrationService.generateCities(countryComboBox, cityComboBox);
    }

    @FXML
    public void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == signUpBtn)
            signUp();
        if (event.getSource() == backBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Input data will be lost. Are you sure you want to get back?");

            if (result == ButtonType.YES)
                SceneController.switchSceneByButton((Button) event.getSource());
        }
        else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    public void signUp() throws IOException {
        boolean noEmptyFields = true;
        boolean passwordsMatch = true;
        boolean noTakenData = true;
        Map<String, CustomField> fieldsByName = initializeCustomFields();

        noEmptyFields = handleEmptyFields(fieldsByName);
        passwordsMatch = handlePasswordsMatching(fieldsByName);
        //handleForbiddenChars(inputFields);
        //handleEgnValidation(inputFields);

        if (noEmptyFields && passwordsMatch)
            noTakenData = handleTakenData(fieldsByName);

        if (noEmptyFields && passwordsMatch && noTakenData) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want to sign up new MRT?");

            if (result == ButtonType.YES) {
                userRegistrationService.createUser(fieldsByName);
                SuccessDialog.success("Done! A new MRT has been signed up!");
                App.setScene("/view/RegisterMrt"); //reloading same scene to clean the fields
            }

        }
    }

    private void makeToggleGroup(List<RadioButton> phoneTypes) {
        toggleGroup = new ToggleGroup();

        phoneTypes.forEach(radioButton -> {
            radioButton.setToggleGroup(toggleGroup);
        });

        toggleGroup.selectToggle(personalPhoneRadioBtn);
    }

    private Map<String, CustomField> initializeCustomFields() {
        Map<String, CustomField> fieldsByName = new HashMap<>();

        fieldsByName.put(USERNAME_FIELD_NAME, new CustomField(usernameField.getText()));
        fieldsByName.put(PASSWORD_FIELD_NAME, new CustomField(passwordField.getText()));
        fieldsByName.put(REPEAT_PASSWORD_FIELD_NAME, new CustomField(repeatPasswordField.getText()));
        fieldsByName.put(EMAIL_FIELD_NAME, new CustomField(emailField.getText()));
        fieldsByName.put(FIRST_NAME_FIELD_NAME, new CustomField(firstNameField.getText()));
        fieldsByName.put(LAST_NAME_FIELD_NAME, new CustomField(lastNameField.getText()));
        fieldsByName.put(EGN_FIELD_NAME, new CustomField(egnField.getText()));
        fieldsByName.put(COUNTRY_FIELD_NAME, new CustomField(countryComboBox.getSelectionModel().getSelectedItem()));
        fieldsByName.put(CITY_FIELD_NAME, new CustomField(cityComboBox.getSelectionModel().getSelectedItem()));
        fieldsByName.put(STREET_FIELD_NAME, new CustomField(streetField.getText()));
        fieldsByName.put(ADDRESS_DETAILS_FIELD_NAME, new CustomField(addressDetailsField.getText()));
        fieldsByName.put(PHONE_NUMBER_FIELD_NAME, new CustomField(phoneNumberField.getText()));

        if (toggleGroup.getSelectedToggle().equals(personalPhoneRadioBtn))
            fieldsByName.put(PHONE_TYPE_FIELD_NAME, new CustomField("PERSONAL"));
        else
            fieldsByName.put(PHONE_TYPE_FIELD_NAME, new CustomField("OFFICE"));

        return fieldsByName;
    }

    private void handleField(CustomField inputField, State state, String message) {
        if (state.equals(State.VALID)) {
            inputField.setStyle("-fx-border-width: 1px;" + "-fx-border-color: lightgrey;" + "-fx-border-radius: 3px");
        } else {
            inputField.setStyle("-fx-border-width: 1px;" + "-fx-border-color: red;" + "-fx-border-radius: 3px");
        }
        inputField.setState(state);
        inputField.setMessage(message);
    }

    private boolean handleEmptyFields(Map<String, CustomField> fieldsByName) {
        boolean handlingResult = true;

        for (CustomField field : fieldsByName.values()) {
            if (field.getFieldValue() == null || field.getFieldValue().isEmpty()) {
                handleField(field, State.INVALID, EMPTY_FIELD_MSG);
                handlingResult = false;
            } else {
                handleField(field, State.VALID, CLEAN_MSG);
            }
        }
        displayMessages(fieldsByName);

        return handlingResult;
    }

    private boolean handlePasswordsMatching(Map<String, CustomField> fieldsByName) {
        boolean handlingResult = true;
        CustomField passwordField;
        CustomField repeatPasswordField;

        if (!this.passwordField.getText().equals(this.repeatPasswordField.getText())) {
            passwordField = fieldsByName.get(PASSWORD_FIELD_NAME);
            repeatPasswordField = fieldsByName.get(REPEAT_PASSWORD_FIELD_NAME);

            if (!passwordField.getFieldValue().isEmpty())
                handleField(passwordField, State.INVALID, CLEAN_MSG);
            handleField(repeatPasswordField, State.INVALID, PASSWORDS_DONT_MATCH_MSG);

            handlingResult = false;
        }
        displayMessages(fieldsByName);

        return handlingResult;
    }

    private boolean handleTakenData(Map<String, CustomField> fieldsByName) {
        boolean handlingResult = true;
        handlingResult = userRegistrationService.validateData(fieldsByName);

        for (CustomField field : fieldsByName.values())
            handleField(field, field.getState(), field.getMessage());

        displayMessages(fieldsByName);

        return handlingResult;
    }

    private void displayMessages(Map<String, CustomField> fieldsByName) {
        usernameMsg.setText(fieldsByName.get(USERNAME_FIELD_NAME).getMessage());
        usernameField.setStyle(fieldsByName.get(USERNAME_FIELD_NAME).getStyle());
        passwordMsg.setText(fieldsByName.get(PASSWORD_FIELD_NAME).getMessage());
        passwordField.setStyle(fieldsByName.get(PASSWORD_FIELD_NAME).getStyle());
        repeatPasswordMsg.setText(fieldsByName.get(REPEAT_PASSWORD_FIELD_NAME).getMessage());
        repeatPasswordField.setStyle(fieldsByName.get(REPEAT_PASSWORD_FIELD_NAME).getStyle());
        emailMsg.setText(fieldsByName.get(EMAIL_FIELD_NAME).getMessage());
        emailField.setStyle(fieldsByName.get(EMAIL_FIELD_NAME).getStyle());
        firstNameMsg.setText(fieldsByName.get(FIRST_NAME_FIELD_NAME).getMessage());
        firstNameField.setStyle(fieldsByName.get(FIRST_NAME_FIELD_NAME).getStyle());
        lastNameMsg.setText(fieldsByName.get(LAST_NAME_FIELD_NAME).getMessage());
        lastNameField.setStyle(fieldsByName.get(LAST_NAME_FIELD_NAME).getStyle());
        regEgnMsg.setText(fieldsByName.get(EGN_FIELD_NAME).getMessage());
        egnField.setStyle(fieldsByName.get(EGN_FIELD_NAME).getStyle());
        countryMsg.setText(fieldsByName.get(COUNTRY_FIELD_NAME).getMessage());
        countryComboBox.setStyle(fieldsByName.get(COUNTRY_FIELD_NAME).getStyle());
        cityMsg.setText(fieldsByName.get(CITY_FIELD_NAME).getMessage());
        cityComboBox.setStyle(fieldsByName.get(CITY_FIELD_NAME).getStyle());
        streetMsg.setText(fieldsByName.get(STREET_FIELD_NAME).getMessage());
        streetField.setStyle(fieldsByName.get(STREET_FIELD_NAME).getStyle());
        addressDetailsMsg.setText(fieldsByName.get(ADDRESS_DETAILS_FIELD_NAME).getMessage());
        addressDetailsField.setStyle(fieldsByName.get(ADDRESS_DETAILS_FIELD_NAME).getStyle());
        phoneNumberMsg.setText(fieldsByName.get(PHONE_NUMBER_FIELD_NAME).getMessage());
        phoneNumberField.setStyle(fieldsByName.get(PHONE_NUMBER_FIELD_NAME).getStyle());
    }

}

package ims.controllers.secondary;

import ims.controllers.contracts.EventBasedController;
import ims.controllers.primary.SceneController;
import ims.controllers.resources.RegisterUserControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.dialogs.InformationDialog;
import ims.enums.Role;
import ims.services.UserRegistrationService;
import ims.supporting.CustomField;
import ims.supporting.GroupToggler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RegisterUserController extends RegisterUserControllerResources implements Initializable, EventBasedController {
    private UserRegistrationService userRegistrationService;
    private ToggleGroup toggleGroup;
    private static Role role;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();

        userRegistrationService = new UserRegistrationService();
        setInterfaceElements();
        initializeCustomFieldMap();
        initializeCountries();
    }

    @FXML
    public void handleClicks(ActionEvent event) throws IOException{
        if (event.getSource() == backBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Input data will be lost. Are you sure you want to get back?");

            if (result == ButtonType.YES) {
                SceneController.getBack();
                AbstractDao.closeEntityManager();
            }
        } else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    private void setInterfaceElements() {
        header.setText(role.toString() + " REGISTRATION");

        toggleGroup = GroupToggler.makeToggleGroup(List.of(personalPhoneRadioBtn, officePhoneRadioBtn));
    }

    private void initializeCountries() {
        List<CustomField> countriesFromDb = userRegistrationService.getCountries();

        countriesFromDb.forEach(field -> {
            countryComboBox.getItems().add(field.getFieldValue());
        });
    }

    @FXML
    private void initializeCities() throws NoSuchFieldException {
        cityComboBox.getItems().clear(); //Prevents data duplicating

        String chosenCountry = countryComboBox.getSelectionModel().getSelectedItem();
        List<CustomField> citiesFromDb = userRegistrationService.getCitiesOfCurrentCountry(chosenCountry);

        citiesFromDb.forEach(field -> {
            cityComboBox.getItems().add(field.getFieldValue());
        });
    }

    @FXML
    public void signUp() throws NoSuchFieldException {
        if (isInputDataValid()) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want to sign up new " + role.toString() + "?");

            if (result == ButtonType.YES) {
                userRegistrationService.createUser(customFieldsByName, role);
                InformationDialog.displayInformation("Done! A new " + role.toString() + " has been signed up!");
            }
        }
    }

    private boolean isInputDataValid() throws NoSuchFieldException {
        boolean noEmptyFields = false;
        boolean passwordsMatch = false;
        boolean noForbiddenChars = true;
        boolean validEgn = true;
        boolean isDataBusy = true;

        noEmptyFields = handleEmptyFields();
        passwordsMatch = handlePasswordsMatching(customFieldsByName);
        //noForbiddenChars = handleForbiddenChars(inputFields);
        //validEgn = handleEgnValidation(inputFields);
        boolean isInitialCheckValid = noEmptyFields && passwordsMatch && noForbiddenChars && validEgn;

        if (isInitialCheckValid)
            isDataBusy = isDataBusy(customFieldsByName);

        return !isDataBusy && noEmptyFields;
    }

    private boolean handlePasswordsMatching(Map<String, CustomField> fieldsByName) {
        boolean passwordsMatch = userRegistrationService.checkIfPasswordsMatch(fieldsByName, passwordField.getText(), repeatPasswordField.getText());

        editField(
                fieldsByName.get(PASSWORD_FIELD_NAME),
                fieldsByName.get(PASSWORD_FIELD_NAME).getState(),
                fieldsByName.get(PASSWORD_FIELD_NAME).getMessage()
        );
        editField(
                fieldsByName.get(REPEAT_PASSWORD_FIELD_NAME),
                fieldsByName.get(REPEAT_PASSWORD_FIELD_NAME).getState(),
                fieldsByName.get(REPEAT_PASSWORD_FIELD_NAME).getMessage()
        );

        displayMessages(fieldsByName);

        return passwordsMatch;
    }

    private boolean isDataBusy(Map<String, CustomField> fieldsByName) throws NoSuchFieldException {
        boolean isDataBusy = userRegistrationService.isDataFound(fieldsByName);

        for (CustomField field : fieldsByName.values()) {
            editField(field, field.getState(), field.getMessage());
        }

        displayMessages(fieldsByName);

        return isDataBusy;
    }

    @Override
    protected void initializeCustomFieldMap() {
        customFieldsByName.put(USERNAME_FIELD_NAME, new CustomField(usernameField.getText()));
        customFieldsByName.put(PASSWORD_FIELD_NAME, new CustomField(passwordField.getText()));
        customFieldsByName.put(REPEAT_PASSWORD_FIELD_NAME, new CustomField(repeatPasswordField.getText()));
        customFieldsByName.put(EMAIL_FIELD_NAME, new CustomField(emailField.getText()));
        customFieldsByName.put(FIRST_NAME_FIELD_NAME, new CustomField(firstNameField.getText()));
        customFieldsByName.put(LAST_NAME_FIELD_NAME, new CustomField(lastNameField.getText()));
        customFieldsByName.put(EGN_FIELD_NAME, new CustomField(egnField.getText()));
        customFieldsByName.put(COUNTRY_FIELD_NAME, new CustomField(countryComboBox.getSelectionModel().getSelectedItem()));
        customFieldsByName.put(CITY_FIELD_NAME, new CustomField(cityComboBox.getSelectionModel().getSelectedItem()));
        customFieldsByName.put(STREET_FIELD_NAME, new CustomField(streetField.getText()));
        customFieldsByName.put(ADDRESS_DETAILS_FIELD_NAME, new CustomField(addressDetailsField.getText()));
        customFieldsByName.put(PHONE_NUMBER_FIELD_NAME, new CustomField(phoneNumberField.getText()));

        if (toggleGroup.getSelectedToggle().equals(personalPhoneRadioBtn))
            customFieldsByName.put(PHONE_TYPE_FIELD_NAME, new CustomField("PERSONAL"));
        else
            customFieldsByName.put(PHONE_TYPE_FIELD_NAME, new CustomField("OFFICE"));
    }

    @Override
    protected void displayMessages(Map<String, CustomField> fieldsByName) {
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

    public static void passUserRegistrationRole(Role passedRole) {
        role = passedRole;
    }

}

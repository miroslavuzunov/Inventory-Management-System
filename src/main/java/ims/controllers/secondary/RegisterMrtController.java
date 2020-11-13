package ims.controllers.secondary;

import ims.App;
import ims.controllers.primary.SceneController;
import ims.controllers.resources.RegisterMrtControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.dialogs.SuccessDialog;
import ims.enums.State;
import ims.services.UserRegistrationService;
import ims.supporting.Cache;
import ims.supporting.CustomField;
import ims.supporting.GroupToggler;
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
        AbstractDao.newEntityManager();
        userRegistrationService = new UserRegistrationService();

        toggleGroup = GroupToggler.makeToggleGroup(List.of(personalPhoneRadioBtn, officePhoneRadioBtn));
        initializeScenes();
        initializeCountries();
    }

    @FXML
    private void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == signUpBtn)
            signUp();
        if (event.getSource() == backBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Input data will be lost. Are you sure you want to get back?");

            if (result == ButtonType.YES) {
                SceneController.switchSceneByButton((Button) event.getSource());
                AbstractDao.closeEntityManager();
            }
        } else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    @Override
    protected void initializeCustomFields() {
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

    private void initializeCountries() {
        List<CustomField> countriesFromDb = Cache.getCachedFields("Countries");

        if (countriesFromDb == null) {
            countriesFromDb = userRegistrationService.initializeCountries();
            Cache.cacheList("Countries", countriesFromDb);
        }

        countriesFromDb.forEach(field -> {
            countryComboBox.getItems().add(field.getFieldValue());
        });
    }

    @FXML
    private void initializeCities() {
        cityComboBox.getItems().clear(); //Prevents data duplicating

        String chosenCountry = countryComboBox.getSelectionModel().getSelectedItem();
        List<CustomField> citiesFromDb = Cache.getCachedFields(chosenCountry);

        if(citiesFromDb == null){
            citiesFromDb = userRegistrationService.initializeCitiesAccordingCountry(chosenCountry);
            Cache.cacheList(chosenCountry, citiesFromDb);
        }

        citiesFromDb.forEach(field -> {
            cityComboBox.getItems().add(field.getFieldValue());
        });
    }

    public void signUp() throws IOException {
        boolean noEmptyFields = true;
        boolean passwordsMatch = true;
        boolean noBusyData = true;
        boolean noForbiddenChars = true;
        boolean validEgn = true;

        noEmptyFields = handleEmptyFields();
        passwordsMatch = handlePasswordsMatching(customFieldsByName);
        //noForbiddenChars = handleForbiddenChars(inputFields);
        //validEgn = handleEgnValidation(inputFields);

        if (noEmptyFields && passwordsMatch && noForbiddenChars && validEgn)
            noBusyData = handleBusyData(customFieldsByName);

        if (noEmptyFields && passwordsMatch && noBusyData) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want to sign up new MRT?");

            if (result == ButtonType.YES) {
                userRegistrationService.createUser(customFieldsByName);
                SuccessDialog.success("Done! A new MRT has been signed up!");
                App.setScene("/view/RegisterMrt"); //reloading same scene to clean the fields
            }
        }
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

    private boolean handleBusyData(Map<String, CustomField> fieldsByName) {
        boolean handlingResult = true;
        handlingResult = userRegistrationService.validateData(fieldsByName);

        for (CustomField field : fieldsByName.values())
            handleField(field, field.getState(), field.getMessage());

        displayMessages(fieldsByName);

        return handlingResult;
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

}

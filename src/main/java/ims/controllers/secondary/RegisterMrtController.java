package ims.controllers.secondary;

import ims.controllers.SceneController;
import ims.controllers.resources.RegisterMrtControllerResources;
import ims.entities.*;
import ims.enums.PhoneType;
import ims.enums.Role;
import ims.enums.State;
import ims.services.UserRegistrationService;
import ims.supporting.CustomField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class RegisterMrtController extends RegisterMrtControllerResources implements Initializable {
    private UserRegistrationService userRegistrationService;
    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userRegistrationService = new UserRegistrationService();

        makeToggleGroup(List.of(mrtRegPersonalPhoneRadioBtn, mrtRegOfficePhoneRadioBtn));
        userRegistrationService.initializeCountries(countryComboBox);
        userRegistrationService.generateCities(countryComboBox, cityComboBox);
        initializeScenes();
    }

    @FXML
    public void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == signUpBtn)
            signUp();

        SceneController.switchSceneByButton((Button) event.getSource());
    }

    public void signUp() {
        Map<String, CustomField> fieldsByName = initializeCustomFields();

        handleEmptyFields(fieldsByName);
        handlePasswordsMatching(fieldsByName);
        //handleForbiddenChars(inputFields);
        //handleEgnValidation(inputFields);
        if(!emptyFieldsFound(fieldsByName))
            handleTakenData(fieldsByName);

//        if (invalidFields.isEmpty())
//            ConfirmationDialog.confirm("Are you sure you want to sign up?");

        //createUser();
    }

    private boolean emptyFieldsFound(Map<String, CustomField> fieldsByName) {
        for(CustomField field : fieldsByName.values()){
           if(field.getState().equals(State.INVALID) && field.getMessage().equals(EMPTY_FIELD_MSG))
               return true;
        }
        return false;
    }


    private void makeToggleGroup(List<RadioButton> phoneTypes) {
        toggleGroup = new ToggleGroup();

        phoneTypes.forEach(radioButton -> {
            radioButton.setToggleGroup(toggleGroup);
        });

        toggleGroup.selectToggle(mrtRegPersonalPhoneRadioBtn);
    }

    private Map<String, CustomField> initializeCustomFields() {
        Map<String, CustomField> fieldsByName = new HashMap<>();

        fieldsByName.put(USERNAME_FIELD_NAME, new CustomField(mrtRegUsernameField.getText()));
        fieldsByName.put(PASSWORD_FIELD_NAME,new CustomField(mrtRegPasswordField.getText()));
        fieldsByName.put(REPEAT_PASSWORD_FIELD_NAME,new CustomField(mrtRegRepeatPasswordField.getText()));
        fieldsByName.put(EMAIL_FIELD_NAME,new CustomField(mrtRegEmailField.getText()));
        fieldsByName.put(FIRST_NAME_FIELD_NAME,new CustomField(mrtRegFirstNameField.getText()));
        fieldsByName.put(LAST_NAME_FIELD_NAME,new CustomField(mrtRegLastNameField.getText()));
        fieldsByName.put(EGN_FIELD_NAME,new CustomField(mrtRegEgnField.getText()));
        fieldsByName.put(COUNTRY_FIELD_NAME,new CustomField(countryComboBox.getSelectionModel().getSelectedItem()));
        fieldsByName.put(CITY_FIELD_NAME,new CustomField(cityComboBox.getSelectionModel().getSelectedItem()));
        fieldsByName.put(STREET_FIELD_NAME,new CustomField(mrtRegStreetField.getText()));
        fieldsByName.put(ADDRESS_DETAILS_FIELD_NAME,new CustomField(mrtRegDetailsField.getText()));
        fieldsByName.put(PHONE_NUMBER_FIELD_NAME,new CustomField(mrtRegPhoneNumberField.getText()));


        return fieldsByName;
    }

    private void handleEmptyFields(Map<String, CustomField> fieldsByName) {
        for (CustomField field : fieldsByName.values()) {
            if (field.getFieldValue() == null || field.getFieldValue().isEmpty()) {
                handleField(field, State.INVALID, EMPTY_FIELD_MSG);
            } else {
                handleField(field, State.VALID, CLEAN_MSG);
            }
        }
        displayMessages(fieldsByName);
    }

    private void handlePasswordsMatching(Map<String, CustomField> fieldsByName) {
        CustomField passwordField;
        CustomField repeatPasswordField;

        if (!mrtRegPasswordField.getText().equals(mrtRegRepeatPasswordField.getText())) {
            passwordField = fieldsByName.get(PASSWORD_FIELD_NAME);
            repeatPasswordField = fieldsByName.get(REPEAT_PASSWORD_FIELD_NAME);

            if(!passwordField.getFieldValue().isEmpty())
                handleField(passwordField, State.INVALID, CLEAN_MSG);
            handleField(repeatPasswordField, State.INVALID, PASSWORDS_DONT_MATCH_MSG);
        }
        displayMessages(fieldsByName);
    }

    private void handleTakenData(Map<String, CustomField> fieldsByName) {
        userRegistrationService.validateData(fieldsByName);

        for (CustomField field : fieldsByName.values())
            handleField(field, field.getState(), field.getMessage());

        displayMessages(fieldsByName);
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

    private void displayMessages(Map<String, CustomField> fieldsByName) {
        mrtRegUsernameMsg.setText(fieldsByName.get(USERNAME_FIELD_NAME).getMessage());
        mrtRegUsernameField.setStyle(fieldsByName.get(USERNAME_FIELD_NAME).getStyle());
        mrtRegPasswordMsg.setText(fieldsByName.get(PASSWORD_FIELD_NAME).getMessage());
        mrtRegPasswordField.setStyle(fieldsByName.get(PASSWORD_FIELD_NAME).getStyle());
        mrtRegRepeatPasswordMsg.setText(fieldsByName.get(REPEAT_PASSWORD_FIELD_NAME).getMessage());
        mrtRegRepeatPasswordField.setStyle(fieldsByName.get(REPEAT_PASSWORD_FIELD_NAME).getStyle());
        mrtRegEmailMsg.setText(fieldsByName.get(EMAIL_FIELD_NAME).getMessage());
        mrtRegEmailField.setStyle(fieldsByName.get(EMAIL_FIELD_NAME).getStyle());
        mrtRegFirstNameMsg.setText(fieldsByName.get(FIRST_NAME_FIELD_NAME).getMessage());
        mrtRegFirstNameField.setStyle(fieldsByName.get(FIRST_NAME_FIELD_NAME).getStyle());
        mrtRegLastNameMsg.setText(fieldsByName.get(LAST_NAME_FIELD_NAME).getMessage());
        mrtRegLastNameField.setStyle(fieldsByName.get(LAST_NAME_FIELD_NAME).getStyle());
        mrtRegEgnMsg.setText(fieldsByName.get(EGN_FIELD_NAME).getMessage());
        mrtRegEgnField.setStyle(fieldsByName.get(EGN_FIELD_NAME).getStyle());
        mrtRegCountryMsg.setText(fieldsByName.get(COUNTRY_FIELD_NAME).getMessage());
        countryComboBox.setStyle(fieldsByName.get(COUNTRY_FIELD_NAME).getStyle());
        mrtRegCityMsg.setText(fieldsByName.get(CITY_FIELD_NAME).getMessage());
        cityComboBox.setStyle(fieldsByName.get(CITY_FIELD_NAME).getStyle());
        mrtRegStreetMsg.setText(fieldsByName.get(STREET_FIELD_NAME).getMessage());
        mrtRegStreetField.setStyle(fieldsByName.get(STREET_FIELD_NAME).getStyle());
        mrtRegAddressMsg.setText(fieldsByName.get(ADDRESS_DETAILS_FIELD_NAME).getMessage());
        mrtRegDetailsField.setStyle(fieldsByName.get(ADDRESS_DETAILS_FIELD_NAME).getStyle());
        mrtRegPhoneNumberMsg.setText(fieldsByName.get(PHONE_NUMBER_FIELD_NAME).getMessage());
        mrtRegPhoneNumberField.setStyle(fieldsByName.get(PHONE_NUMBER_FIELD_NAME).getStyle());
    }

    private void createUser() {  //TODO ORGANIZE CREATING USER!
        City city = new City();
        Address address = new Address();
        PersonInfo personInfo = new PersonInfo();
        User user = new User();
        PhoneNumber phoneNumber = new PhoneNumber();

        //TODO COMPLETE METHOD

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA");
        EntityManager manager = factory.createEntityManager();

        city.setName("Yambol");
        city.setRegion("Yambol");

        address.setStreet(mrtRegStreetField.getText());
        address.setDetails(mrtRegDetailsField.getText());

        address.setCity(userRegistrationService.getUserAddressCity(address, userRegistrationService.getCityId(city)));

        personInfo.setFirstName(mrtRegFirstNameField.getText());
        personInfo.setLastName(mrtRegLastNameField.getText());
        personInfo.setEgn(mrtRegEgnField.getText());
        personInfo.setAddress(address);

        user.setPersonInfo(personInfo);
        user.setNickname(mrtRegUsernameField.getText());
        user.setPassword(mrtRegPasswordField.getText());
        user.setEmail(mrtRegEmailField.getText());
        user.setRole(Role.MRT);
        user.setCreatedOn(LocalDate.now());

        phoneNumber.setOwner(user);
        if (toggleGroup.getSelectedToggle().equals(mrtRegPersonalPhoneRadioBtn))
            phoneNumber.setPhoneType(PhoneType.PERSONAL);
        else
            phoneNumber.setPhoneType(PhoneType.OFFICE);

        phoneNumber.setNumber(mrtRegPhoneNumberField.getText());
        user.setPhoneNumbers(Set.of(phoneNumber));

        userRegistrationService.saveUser(user);
        userRegistrationService.savePhone(phoneNumber);
    }

}

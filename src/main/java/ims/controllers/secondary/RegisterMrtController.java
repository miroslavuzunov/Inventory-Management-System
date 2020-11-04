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
import java.util.Set;

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
        List<CustomField> inputFields = initializeCustomFields();

        checkForEmptyFields(inputFields);

//        checkForPasswordsMatch(inputFields);
//        checkForForbiddenChars(inputFields);
//        checkForReservedData(inputFields);


//        if (invalidFields.isEmpty())
//            ConfirmationDialog.confirm("Are you sure you want to sign up?");

        //createUser();
    }


    private void checkForEmptyFields(List<CustomField> inputFields) {
        for (CustomField field : inputFields) {
            if (field.getFieldValue() == null || field.getFieldValue().isEmpty()) {
                field.setState(State.INVALID);
                field.setMessage(EMPTY_FIELD_MSG);
                field.setStyle("-fx-border-width: 1px;" + "-fx-border-color: red;" + "-fx-border-radius: 3px");
            } else {
                field.setState(State.VALID);
                field.setMessage(CLEAN_MSG);
                field.setStyle("-fx-border-width: 1px;" + "-fx-border-color: lightgrey;" + "-fx-border-radius: 3px");
            }
        }
        displayMessages(inputFields);
    }

    private void displayMessages(List<CustomField> inputFields) {
        for (CustomField field : inputFields) {
            switch (field.getFieldName()) {
                case USERNAME_FIELD_NAME:
                    mrtRegUsernameMsg.setText(field.getMessage());
                    mrtRegUsernameField.setStyle(field.getStyle());
                case PASSWORD_FIELD_NAME:
                    mrtRegPasswordMsg.setText(field.getMessage());
                    mrtRegPasswordField.setStyle(field.getStyle());
                case REPEAT_PASSWORD_FIELD_NAME:
                    mrtRegRepeatPasswordMsg.setText(field.getMessage());
                    mrtRegRepeatPasswordField.setStyle(field.getStyle());
                case EMAIL_FIELD_NAME:
                    mrtRegEmailMsg.setText(field.getMessage());
                    mrtRegEmailField.setStyle(field.getStyle());
                case FIRST_NAME_FIELD_NAME:
                    mrtRegFirstNameMsg.setText(field.getMessage());
                    mrtRegFirstNameField.setStyle(field.getStyle());
                case LAST_NAME_FIELD_NAME:
                    mrtRegLastNameMsg.setText(field.getMessage());
                    mrtRegLastNameField.setStyle(field.getStyle());
                case EGN_FIELD_NAME:
                    mrtRegEgnMsg.setText(field.getMessage());
                    mrtRegEgnField.setStyle(field.getStyle());
                case COUNTRY_FIELD_NAME:
                    mrtRegCountryMsg.setText(field.getMessage());
                    countryComboBox.setStyle(field.getStyle());
                case CITY_FIELD_NAME:
                    mrtRegCityMsg.setText(field.getMessage());
                    cityComboBox.setStyle(field.getStyle());
                case STREET_FIELD_NAME:
                    mrtRegStreetMsg.setText(field.getMessage());
                    mrtRegStreetField.setStyle(field.getStyle());
                case ADDRESS_DETAILS_FIELD_NAME:
                    mrtRegAddressMsg.setText(field.getMessage());
                    mrtRegDetailsField.setStyle(field.getStyle());
                case PHONE_NUMBER_FIELD_NAME:
                    mrtRegPhoneNumberMsg.setText(field.getMessage());
                    mrtRegPhoneNumberField.setStyle(field.getStyle());
            }
        }
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

    private void makeToggleGroup(List<RadioButton> phoneTypes) {
        toggleGroup = new ToggleGroup();

        phoneTypes.forEach(radioButton -> {
            radioButton.setToggleGroup(toggleGroup);
        });

        toggleGroup.selectToggle(mrtRegPersonalPhoneRadioBtn);
    }

    private List<CustomField> initializeCustomFields() {
        List<CustomField> inputFields = new ArrayList<>();

        inputFields.add(new CustomField(USERNAME_FIELD_NAME, mrtRegUsernameField.getText()));
        inputFields.add(new CustomField(PASSWORD_FIELD_NAME, mrtRegPasswordField.getText()));
        inputFields.add(new CustomField(REPEAT_PASSWORD_FIELD_NAME, mrtRegRepeatPasswordField.getText()));
        inputFields.add(new CustomField(EMAIL_FIELD_NAME, mrtRegEmailField.getText()));
        inputFields.add(new CustomField(FIRST_NAME_FIELD_NAME, mrtRegFirstNameField.getText()));
        inputFields.add(new CustomField(LAST_NAME_FIELD_NAME, mrtRegLastNameField.getText()));
        inputFields.add(new CustomField(EGN_FIELD_NAME, mrtRegEgnField.getText()));
        inputFields.add(new CustomField(COUNTRY_FIELD_NAME, countryComboBox.getSelectionModel().getSelectedItem()));
        inputFields.add(new CustomField(CITY_FIELD_NAME, cityComboBox.getSelectionModel().getSelectedItem()));
        inputFields.add(new CustomField(STREET_FIELD_NAME, mrtRegStreetField.getText()));
        inputFields.add(new CustomField(ADDRESS_DETAILS_FIELD_NAME, mrtRegDetailsField.getText()));
        inputFields.add(new CustomField(PHONE_NUMBER_FIELD_NAME, mrtRegPhoneNumberField.getText()));

        return inputFields;
    }

}

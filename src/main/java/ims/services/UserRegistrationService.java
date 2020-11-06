package ims.services;

import ims.daos.*;
import ims.entities.*;
import ims.enums.PhoneType;
import ims.enums.Role;
import ims.enums.State;
import ims.supporting.CustomField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

import static ims.controllers.resources.RegisterMrtControllerResources.*;

public class UserRegistrationService {
    private final UserDao userDao;
    private final CountryDao countryDao;
    private final CityDao cityDao;
    private final PhoneNumberDao phoneNumberDao;
    private final AddressDao addressDao;
    private final PersonInfoDao personInfoDao;

    public UserRegistrationService() {
        this.userDao = new UserDao();
        this.countryDao = new CountryDao();
        this.cityDao = new CityDao();
        this.addressDao = new AddressDao();
        this.phoneNumberDao = new PhoneNumberDao();
        this.personInfoDao = new PersonInfoDao();
    }

    public void initializeCountries(ComboBox<String> countryChoiceBox) {
        List<Country> countries = countryDao.getAll();

        countries.forEach(country -> {
            countryChoiceBox.getItems().add(country.getName());
        });
    }

    public void generateCities(ComboBox<String> countryComboBox, ComboBox<String> cityComboBox) {
        ChangeListener<String> changeListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String country, String t1) {
                initializeCitiesAccordingCountry(cityComboBox, countryComboBox.getSelectionModel().getSelectedItem());
            }
        };
        countryComboBox.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    private void initializeCitiesAccordingCountry(ComboBox<String> cityComboBox, String selectedCountryName) {
        cityComboBox.getItems().clear(); //Deletes previous country's cities

        List<Country> countries = countryDao.getAll();
        Set<City> cities = new HashSet<>();

        for (Country country : countries) {
            if (country.getName().equals(selectedCountryName))
                cities = country.getCities();
        }

        for (City city : cities) {
            cityComboBox.getItems().add(city.getName() + " (" + city.getRegion() + ")");
        }
    }

    public boolean validateData(Map<String, CustomField> fieldsByName) {
        boolean handlingResult = true;
        String inputUsername = fieldsByName.get(USERNAME_FIELD_NAME).getFieldValue();
        String inputEmail = fieldsByName.get(EMAIL_FIELD_NAME).getFieldValue();
        String inputEgn = fieldsByName.get(EGN_FIELD_NAME).getFieldValue();

        Map<String, User> existingUsersByFieldName = requestData(inputUsername, inputEmail, inputEgn);

        if (inputUsername.equals(existingUsersByFieldName.get(USERNAME_FIELD_NAME).getNickname())) {
            fieldsByName.get(USERNAME_FIELD_NAME).setState(State.INVALID);
            fieldsByName.get(USERNAME_FIELD_NAME).setMessage(BUSY_USERNAME_MSG);
            handlingResult = false;
        }
        if (inputEmail.equals(existingUsersByFieldName.get(EMAIL_FIELD_NAME).getEmail())) {
            fieldsByName.get(EMAIL_FIELD_NAME).setState(State.INVALID);
            fieldsByName.get(EMAIL_FIELD_NAME).setMessage(BUSY_EMAIL_MSG);
            handlingResult = false;
        }
        if (inputEgn.equals(existingUsersByFieldName.get(EGN_FIELD_NAME).getPersonInfo().getEgn())) {
            fieldsByName.get(EGN_FIELD_NAME).setState(State.INVALID);
            fieldsByName.get(EGN_FIELD_NAME).setMessage(BUSY_EGN_MSG);
            handlingResult = false;
        }

        return handlingResult;
    }

    public Map<String, User> requestData(String username, String email, String egn) {
        Map<String, User> users = new HashMap<>();

        User userWithSameNick = new User();
        if (userDao.getUserByField("nickname", username) != null)
            userWithSameNick.setNickname(userDao.getUserByField("nickname", username).getNickname());

        User userWithSameEmail = new User();
        if(userDao.getUserByField("email", email) != null)
            userWithSameEmail.setEmail(userDao.getUserByField("email", email).getEmail());

        User userWithSameEgn = new User();
        PersonInfo personInfo = new PersonInfo();
        userWithSameEgn.setPersonInfo(personInfo);
        if(personInfoDao.getPersonInfoByEgn(egn) != null)
            userWithSameEgn.setPersonInfo(personInfoDao.getPersonInfoByEgn(egn));

        users.put(USERNAME_FIELD_NAME, userWithSameNick);
        users.put(EMAIL_FIELD_NAME, userWithSameEmail);
        users.put(EGN_FIELD_NAME, userWithSameEgn);

        return users;
    }

    public Integer getCityId(City city) {
        City tempCity = cityDao.getCity(city);

        if (tempCity == null)
            return null;

        return tempCity.getId();
    }

    public void setUserAddressCity(Address address, Integer cityId) {
         addressDao.setAddressReferenceToCity(address, cityId);
    }

    public void createUser(Map<String, CustomField> fieldsByName) {
        City city = new City();
        Address address = new Address();
        PersonInfo personInfo = new PersonInfo();
        User user = new User();
        PhoneNumber phoneNumber = new PhoneNumber();

        String cityAndRegionTogether = fieldsByName.get(CITY_FIELD_NAME).getFieldValue();
        String[] cityAndRegion = Pattern.compile("[\\(\\)]").split(cityAndRegionTogether); //separating 'City (Region)' string
        city.setName(cityAndRegion[0]);
        city.setRegion(cityAndRegion[1]);

        address.setStreet(fieldsByName.get(STREET_FIELD_NAME).getFieldValue());
        address.setDetails(fieldsByName.get(ADDRESS_DETAILS_FIELD_NAME).getFieldValue());
        setUserAddressCity(address, getCityId(city));

        personInfo.setFirstName(fieldsByName.get(FIRST_NAME_FIELD_NAME).getFieldValue());
        personInfo.setLastName(fieldsByName.get(LAST_NAME_FIELD_NAME).getFieldValue());
        personInfo.setEgn(fieldsByName.get(EGN_FIELD_NAME).getFieldValue());
        personInfo.setAddress(address);

        user.setPersonInfo(personInfo);
        user.setNickname(fieldsByName.get(USERNAME_FIELD_NAME).getFieldValue());
        user.setPassword(fieldsByName.get(PASSWORD_FIELD_NAME).getFieldValue());
        user.setEmail(fieldsByName.get(EMAIL_FIELD_NAME).getFieldValue());
        user.setRole(Role.MRT);
        user.setCreatedOn(LocalDate.now());
        user.setPhoneNumbers(Set.of());

        phoneNumber.setOwner(user);
        if (fieldsByName.get(PHONE_TYPE_FIELD_NAME).getFieldValue().equals("PERSONAL"))
            phoneNumber.setPhoneType(PhoneType.PERSONAL);
        else
            phoneNumber.setPhoneType(PhoneType.OFFICE);

        phoneNumber.setNumber(fieldsByName.get(PHONE_NUMBER_FIELD_NAME).getFieldValue());

        phoneNumberDao.addUserViaPhoneNumber(phoneNumber);
    }
}

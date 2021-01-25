package ims.services;

import ims.daos.*;
import ims.entities.*;
import ims.enums.PhoneType;
import ims.enums.Role;
import ims.enums.State;
import ims.supporting.CustomField;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

import static ims.controllers.resources.RegisterUserControllerResources.*;

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

    public List<CustomField> initializeCountries() {
        List<CustomField> controllerCountries = new ArrayList<>();
        List<Country> dbCountries = countryDao.getAll();

        dbCountries.forEach(country -> {
            controllerCountries.add(new CustomField(country.getName()));
        });

        return controllerCountries;
    }

    public List<CustomField> initializeCitiesOfCurrentCountry(String selectedCountryName) throws NoSuchFieldException {
        List<CustomField> controllerCities = new ArrayList<>();
        List<City> dbCities = cityDao.getAll();

        dbCities.forEach(city -> {
            if (city.getCountry().getName().equals(selectedCountryName))
                controllerCities.add(new CustomField(city.getName() + "(" + city.getRegion() + ")"));
        });

        return controllerCities;
    }

    public boolean checkIfPasswordsMatch(Map<String, CustomField> fieldsByName, String password, String repeatedPassword) {
        if (!password.equals(repeatedPassword)) {
            fieldsByName.get(PASSWORD_FIELD_NAME).setState(State.INVALID);
            fieldsByName.get(REPEAT_PASSWORD_FIELD_NAME).setState(State.INVALID);

            fieldsByName.get(REPEAT_PASSWORD_FIELD_NAME).setMessage(PASSWORDS_DONT_MATCH_MSG);
            if (!fieldsByName.get(PASSWORD_FIELD_NAME).getFieldValue().isEmpty())
                fieldsByName.get(PASSWORD_FIELD_NAME).setMessage(CLEAN_MSG);

            return false;
        }

        return true;
    }

    public boolean isBusyDataFound(Map<String, CustomField> customFieldsByName) throws NoSuchFieldException {
        String inputUsername = customFieldsByName.get(USERNAME_FIELD_NAME).getFieldValue();
        String inputEmail = customFieldsByName.get(EMAIL_FIELD_NAME).getFieldValue();
        String inputEgn = customFieldsByName.get(EGN_FIELD_NAME).getFieldValue();

        if (isUsernameBusy(inputUsername)) {
            editCurrentField(customFieldsByName.get(USERNAME_FIELD_NAME), BUSY_USERNAME_MSG);

            return true;
        }
        if (isEmailBusy(inputEmail)) {
            editCurrentField(customFieldsByName.get(EMAIL_FIELD_NAME), BUSY_EMAIL_MSG);

            return true;
        }
        if (isEgnBusy(inputEgn)) {
            editCurrentField(customFieldsByName.get(EGN_FIELD_NAME), BUSY_EGN_MSG);

            return true;
        }

        return false;
    }

    private boolean isUsernameBusy(String inputUsername) throws NoSuchFieldException {
        return userDao.getUserByUsername(inputUsername) != null;
    }

    private boolean isEmailBusy(String inputEmail) throws NoSuchFieldException {
        return userDao.getUserByEmail(inputEmail) != null;
    }

    private boolean isEgnBusy(String inputEgn) throws NoSuchFieldException {
        return personInfoDao.getRecordByEgn(inputEgn) != null;
    }

    private void editCurrentField(CustomField customField, String message) {
        customField.setState(State.INVALID);
        customField.setMessage(message);
    }

    public void createUser(Map<String, CustomField> customFieldsByName, Role role) throws NoSuchFieldException {
        City city = new City();
        Address address = new Address();
        PersonInfo personInfo = new PersonInfo();
        User user = new User();
        PhoneNumber phoneNumber = new PhoneNumber();

        setUserCity(customFieldsByName, city);
        setUserAddress(customFieldsByName, address, city);
        setUserPersonInfo(customFieldsByName, personInfo, address);
        setUser(customFieldsByName, user, personInfo, role);
        setPhoneNumber(customFieldsByName, phoneNumber, user);

        userDao.saveRecord(user);
        phoneNumberDao.updateRecord(phoneNumber); //Indirectly creates user (using update because of cascaded references)
    }

    private void setUserCity(Map<String, CustomField> customFieldsByName, City city) {
        String cityAndRegionTogether = customFieldsByName.get(CITY_FIELD_NAME).getFieldValue();
        String[] cityAndRegion = Pattern.compile("[\\(\\)]").split(cityAndRegionTogether); //separating 'City (Region)' string

        city.setName(cityAndRegion[0]);
        city.setRegion(cityAndRegion[1]);
    }

    private void setUserAddress(Map<String, CustomField> customFieldsByName, Address address, City city) throws NoSuchFieldException {
        address.setStreet(customFieldsByName.get(STREET_FIELD_NAME).getFieldValue());
        address.setDetails(customFieldsByName.get(ADDRESS_DETAILS_FIELD_NAME).getFieldValue());
        address.setCity(addressDao.getCityReference(getCityId(city)));
    }

    private void setUserPersonInfo(Map<String, CustomField> customFieldsByName, PersonInfo personInfo, Address address) {
        personInfo.setFirstName(customFieldsByName.get(FIRST_NAME_FIELD_NAME).getFieldValue());
        personInfo.setLastName(customFieldsByName.get(LAST_NAME_FIELD_NAME).getFieldValue());
        personInfo.setEgn(customFieldsByName.get(EGN_FIELD_NAME).getFieldValue());
        personInfo.setAddress(address);
    }

    private void setUser(Map<String, CustomField> customFieldsByName, User user, PersonInfo personInfo, Role role) {
        user.setPersonInfo(personInfo);
        user.setNickname(customFieldsByName.get(USERNAME_FIELD_NAME).getFieldValue());
        user.setPassword(customFieldsByName.get(PASSWORD_FIELD_NAME).getFieldValue());
        user.setEmail(customFieldsByName.get(EMAIL_FIELD_NAME).getFieldValue());
        user.setRole(role);
        user.setCreatedOn(LocalDate.now());
        user.setPhoneNumbers(Set.of());
    }

    private void setPhoneNumber(Map<String, CustomField> customFieldsByName, PhoneNumber phoneNumber, User user) {
        phoneNumber.setOwner(user);

        if (customFieldsByName.get(PHONE_TYPE_FIELD_NAME).getFieldValue().equals("PERSONAL"))
            phoneNumber.setPhoneType(PhoneType.PERSONAL);
        else
            phoneNumber.setPhoneType(PhoneType.OFFICE);

        phoneNumber.setNumber(customFieldsByName.get(PHONE_NUMBER_FIELD_NAME).getFieldValue());
    }

    public Integer getCityId(City city) throws NoSuchFieldException {
        City tempCity = cityDao.getRecordByNameAndRegion(city.getName(), city.getRegion());

        if (tempCity != null)
            return tempCity.getId();
        return null;
    }

}

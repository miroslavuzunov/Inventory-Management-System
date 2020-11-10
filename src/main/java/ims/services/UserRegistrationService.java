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

    public List<CustomField> initializeCountries() {
        List<CustomField> controllerCountries = new ArrayList<>();
        List<Country> dbCountries = countryDao.getAll();

        dbCountries.forEach(country -> {
            controllerCountries.add(new CustomField(country.getName()));
        });

        return controllerCountries;
    }

    public List<CustomField> initializeCitiesAccordingCountry(String selectedCountryName) {
        List<CustomField> controllerCities = new ArrayList<>();
        List<City> dbCities = cityDao.getAll();

        dbCities.forEach(city -> {
            if (city.getCountry().getName().equals(selectedCountryName))
                controllerCities.add(new CustomField(city.getName() + "(" + city.getRegion() + ")"));
        });

        return controllerCities;
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
        if (userDao.getUserByUsername(username) != null)
            userWithSameNick.setNickname(userDao.getUserByUsername(username).getNickname());

        User userWithSameEmail = new User();
        if (userDao.getUserByEmail(email) != null)
            userWithSameEmail.setEmail(userDao.getUserByEmail(email).getEmail());

        User userWithSameEgn = new User();
        PersonInfo personInfo = new PersonInfo();
        userWithSameEgn.setPersonInfo(personInfo);
        if (personInfoDao.getRecordByEGN(egn) != null)
            userWithSameEgn.setPersonInfo(personInfoDao.getRecordByEGN(egn));

        users.put(USERNAME_FIELD_NAME, userWithSameNick);
        users.put(EMAIL_FIELD_NAME, userWithSameEmail);
        users.put(EGN_FIELD_NAME, userWithSameEgn);

        return users;
    }

    public Integer getCityId(City city) {
        City tempCity = cityDao.getRecordByNameAndRegion(city.getName(), city.getRegion());

        if (tempCity == null)
            return null;

        return tempCity.getId();
    }

    public void createUser(Map<String, CustomField> customFieldsByName) {
        City city = new City();
        Address address = new Address();
        PersonInfo personInfo = new PersonInfo();
        User user = new User();
        PhoneNumber phoneNumber = new PhoneNumber();

        String cityAndRegionTogether = customFieldsByName.get(CITY_FIELD_NAME).getFieldValue();
        String[] cityAndRegion = Pattern.compile("[\\(\\)]").split(cityAndRegionTogether); //separating 'City (Region)' string
        city.setName(cityAndRegion[0]);
        city.setRegion(cityAndRegion[1]);

        address.setStreet(customFieldsByName.get(STREET_FIELD_NAME).getFieldValue());
        address.setDetails(customFieldsByName.get(ADDRESS_DETAILS_FIELD_NAME).getFieldValue());
        address.setCity(addressDao.getCityReference(getCityId(city)));

        personInfo.setFirstName(customFieldsByName.get(FIRST_NAME_FIELD_NAME).getFieldValue());
        personInfo.setLastName(customFieldsByName.get(LAST_NAME_FIELD_NAME).getFieldValue());
        personInfo.setEgn(customFieldsByName.get(EGN_FIELD_NAME).getFieldValue());
        personInfo.setAddress(address);

        user.setPersonInfo(personInfo);
        user.setNickname(customFieldsByName.get(USERNAME_FIELD_NAME).getFieldValue());
        user.setPassword(customFieldsByName.get(PASSWORD_FIELD_NAME).getFieldValue());
        user.setEmail(customFieldsByName.get(EMAIL_FIELD_NAME).getFieldValue());
        user.setRole(Role.MRT);
        user.setCreatedOn(LocalDate.now());
        user.setPhoneNumbers(Set.of());

        phoneNumber.setOwner(user);
        if (customFieldsByName.get(PHONE_TYPE_FIELD_NAME).getFieldValue().equals("PERSONAL"))
            phoneNumber.setPhoneType(PhoneType.PERSONAL);
        else
            phoneNumber.setPhoneType(PhoneType.OFFICE);

        phoneNumber.setNumber(customFieldsByName.get(PHONE_NUMBER_FIELD_NAME).getFieldValue());

        phoneNumberDao.updateRecord(phoneNumber); //Indirectly creates user (using update because of cascaded references)
    }
}

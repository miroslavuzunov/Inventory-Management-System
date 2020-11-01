package ims.services;

import ims.daos.*;
import ims.entities.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRegistrationService {
    private final UserDao userDao;
    private final CountryDao countryDao;
    private final CityDao cityDao;
    private final PhoneNumberDao phoneNumberDao;
    private final AddressDao addressDao;

    public UserRegistrationService() {
        this.userDao = new UserDao();
        this.countryDao = new CountryDao();
        this.cityDao = new CityDao();
        this.addressDao = new AddressDao();
        this.phoneNumberDao = new PhoneNumberDao();
    }

    public void initializeCountries(ComboBox<String> countryChoiceBox) {
        List<Country> countries = countryDao.getAll();

        countries.forEach(country -> {
            countryChoiceBox.getItems().add(country.getName());
        });
    }

    public void initializeCities(ComboBox<String> countryComboBox, ComboBox<String> cityComboBox) {
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

    public boolean checkIfUsernameExists(String username) {
        return userDao.getUserByField("nickname", username) != null;
    }

    public boolean checkIfPasswordsMatch(String pass, String rePass) {
        return pass.equals(rePass);
    }

    public boolean checkIfEmailIsUsed(String email) {
        return userDao.getUserByField("email", email) != null;
    }

    public boolean checkIfEgnIsValid(String egn) {
        //TODO real egn validations
        return egn.length() == 10;
    }

    public Integer getCityId(City city) {
        City tempCity = cityDao.getCity(city);

        if (tempCity == null)
            return null;

        return tempCity.getId();
    }

    public City getUserAddressCity(Address address, Integer cityId) {
        return addressDao.getAddressReferenceToCity(address, cityId);
    }

    public void saveUser(User user) {
        userDao.addUser(user);
    }

    public void savePhone(PhoneNumber phoneNumber) {
        phoneNumberDao.addPhone(phoneNumber);
    }
}

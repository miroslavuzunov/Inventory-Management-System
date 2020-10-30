package ims.services;

import ims.daos.*;
import ims.entities.*;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class UserRegistrationService {
    private final UserDao userDao;
    private final CountryDao countryDao;
    private final CityDao cityDao;
    private final PersonInfoDao personInfoDao;
    private final AddressDao addressDao;

    public UserRegistrationService() {
        this.userDao = new UserDao();
        this.countryDao = new CountryDao();
        this.cityDao = new CityDao();
        this.personInfoDao = new PersonInfoDao();
        this.addressDao = new AddressDao();
    }

    public void initializeCountries(ChoiceBox<String> countryChoiceBox) {
        List<Country> countries = countryDao.getAll();

        countries.forEach(country -> {
            countryChoiceBox.getItems().add(country.getName());
        });
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

    public void saveUser(User user){
        userDao.addUser(user);
    }

    public Integer getCityId(City city){
        City tempCity = cityDao.getCity(city);

        if(tempCity == null)
            return null;

        return tempCity.getId();
    }

    public Integer getCountryId(Country country) {
        Country tempCountry = countryDao.getCountry(country);

        if(tempCountry == null)
            return null;

        return tempCountry.getId();
    }

    public void setUserAddressCity(Address address, Integer cityId){
        addressDao.setAddressByCityId(address, cityId);
    }

    public void setUserAddressCountry(Address address, Integer countryId) {
        addressDao.setAddressCountryById(address, countryId);
    }

    public void beginTransaction(){addressDao.beginTransaction();}

    public void commitTransaction(){addressDao.commitTransaction();}
}

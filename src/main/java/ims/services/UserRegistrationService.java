package ims.services;

import ims.daos.CountryDao;
import ims.daos.UserDao;
import ims.entities.Country;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class UserRegistrationService {
    private UserDao userDao;
    private CountryDao countryDao;

    public UserRegistrationService() {
        this.userDao = new UserDao();
        this.countryDao = new CountryDao();
    }

    public boolean checkIfUsernameExists(String username) {
        return userDao.getUserByField("nickname", username) != null;
    }


    public void initializeCountries(ChoiceBox<String> countryChoiceBox) {
        List<Country> countries = countryDao.getAll();

        countries.forEach(country -> {
            countryChoiceBox.getItems().add(country.getName());
        });
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
}

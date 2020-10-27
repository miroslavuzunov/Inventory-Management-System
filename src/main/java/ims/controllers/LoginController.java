package ims.controllers;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import ims.App;
import ims.entities.*;
import ims.enums.Role;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void login() throws IOException {

        String username = usernameField.getText();
        String password = passwordField.getText();

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA");
        EntityManager manager = factory.createEntityManager();

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        criteriaQuery.where(criteriaBuilder.equal(userRoot.get("nickname"), username));
        List<User> users = manager.createQuery(criteriaQuery).getResultList();

        if(users.isEmpty())
            messageLabel.setText("Invalid username! Try again!");
        else
            if(users.get(0).getPassword().equals(password))
                App.setRoot("/view/AdminPanel");
            else
                messageLabel.setText("Wrong password! Try again!");
    }


}
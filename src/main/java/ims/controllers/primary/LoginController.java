package ims.controllers.primary;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import ims.App;
import ims.controllers.primary.AdminController;
import ims.entities.*;
import ims.enums.PhoneType;
import ims.enums.Role;
import ims.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loginBtn;

    private UserService userService;
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginBtn.setDefaultButton(true); // ENTER key calls login
        userService = new UserService();
    }

    @FXML
    private void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        user = userService.getUserByUsername(username);

        if(user == null)
            messageLabel.setText("Invalid username! Try again!");
        else
            if(user.getPassword().equals(password)) {
                switch (user.getRole()) {
                    case ADMIN:
                        AdminController.passUser(user);
                        App.setScene("/view/Admin");
                        break;
                    case MRT:
                        //TODO
                    case CLIENT:
                        //TODO
                }
            }
            else
                messageLabel.setText("Wrong password! Try again!");
    }

}
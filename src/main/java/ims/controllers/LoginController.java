package ims.controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ims.App;
import ims.enums.Role;
import ims.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginBtn.setDefaultButton(true); // ENTER key calls login
        userService = new UserService();
    }

    @FXML
    private void login() throws IOException {

        String username = usernameField.getText();
        String password = passwordField.getText();



        if(userService.getUserByUsername(username) == null)
            messageLabel.setText("Invalid username! Try again!");
        else
            if(userService.getUserByUsername(username).getPassword().equals(password)) {
                AdminController.passUserFirstName(userService.getUserByUsername(username));

                switch (userService.getUserByUsername(username).getRole()) {
                    case ADMIN:
                        App.setRoot("/view/AdminPanel");
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
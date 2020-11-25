package ims.controllers.primary;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ims.App;
import ims.daos.AbstractDao;
import ims.entities.*;
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
    private static User loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();
        loginBtn.setDefaultButton(true); // ENTER key calls login
        userService = new UserService();
    }

    @FXML
    private void login() throws IOException, NoSuchFieldException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        loggedUser = userService.getUserByUsername(username);

        if(loggedUser == null)
            messageLabel.setText("Invalid username! Try again!");
        else
            if(loggedUser.getPassword().equals(password)) {
                switch (loggedUser.getRole()) {
                    case ADMIN:
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

    public static User getLoggedUser() {
        return loggedUser;
    }
}
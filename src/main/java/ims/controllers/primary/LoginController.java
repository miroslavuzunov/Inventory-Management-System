package ims.controllers.primary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ims.daos.AbstractDao;
import ims.entities.*;
import ims.supporting.Authenticator;
import ims.supporting.CustomScene;
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

    private Authenticator authenticator;
    private static User loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginBtn.setDefaultButton(true); // ENTER key calls login
        authenticator = new Authenticator();
    }

    @FXML
    private void login() throws IOException, NoSuchFieldException {
        AbstractDao.newEntityManager();
        boolean isUserValid;

        String username = usernameField.getText();
        String password = passwordField.getText();

        isUserValid = authenticator.authenticateUser(username, password);

        if (isUserValid) {
            messageLabel.setText("");
            loggedUser = authenticator.getLoggedUser();

            switch (loggedUser.getRole()) {
                case ADMIN:
                    SceneController.setInitialScene(new CustomScene("Admin"));
                    break;
                case MRT:
                    SceneController.setInitialScene(new CustomScene("MRT"));
                    break;
                case CLIENT:
                    SceneController.setInitialScene(new CustomScene("Client"));
                    break;
            }
        } else {
            messageLabel.setText(authenticator.getInfoMessage());
        }
    }

    public static User getLoggedUser() {
        return loggedUser;
    }
}
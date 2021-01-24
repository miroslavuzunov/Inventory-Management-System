package ims.controllers.primary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ims.daos.AbstractDao;
import ims.supporting.Authenticator;
import ims.supporting.CustomScene;
import ims.supporting.UserSession;
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
            UserSession.createInstance(authenticator.getAuthenticatedUser());

            switch (UserSession.getLoggedUser().getRole()) {
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
            clearTheFields();
        } else {
            messageLabel.setText(authenticator.getInfoMessage());
        }
    }

    private void clearTheFields() {
        messageLabel.setText("");
        usernameField.setText("");
        passwordField.setText("");
    }
}
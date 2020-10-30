package ims.controllers.primary;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import ims.controllers.SceneController;
import ims.controllers.resources.AdminControllerResources;
import ims.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class AdminController extends AdminControllerResources implements Initializable {
    private static User loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userStatus.setText("Status: " + loggedUser.getRole());
        todaysDate.setText("Today's date: " + LocalDate.now().toString());
        nameLabel.setText("Hello, " + loggedUser.getPersonInfo().getFirstName() + "!");

        initializeScenes();
    }

    @FXML
    private void handleClicks(ActionEvent event) throws NoSuchFieldException, IOException {
        if (event.getSource() == genRefBtn) {
            fadeMain.toFront();
            genRefPane.toFront();
        }
        if (event.getSource() == clientManipBtn) {
            fadeMain.toFront();
            clientManipPane.toFront();
        }

        SceneController.switchSceneByButton((Button)event.getSource());
    }

    public static void passUser(User passedUser) {
        loggedUser = passedUser;
    }
}

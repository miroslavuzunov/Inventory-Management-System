package ims.controllers.primary;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import ims.controllers.resources.AdminControllerResources;
import ims.controllers.secondary.RegisterUserController;
import ims.dialogs.ConfirmationDialog;
import ims.entities.User;
import ims.enums.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

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
    private void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == genRefBtn) {
            fadeMain.toFront();
            genRefPane.toFront();
        }
        if (event.getSource() == clientManipBtn) {
            fadeMain.toFront();
            clientManipPane.toFront();
        }
        if(event.getSource() == regMrtBtn)
            RegisterUserController.passRole(Role.MRT);
        if(event.getSource() == regCardBtn)
            RegisterUserController.passRole(Role.CLIENT);
        if (event.getSource() == exitBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want exit from the system?");

            if (result == ButtonType.YES)
                SceneController.switchSceneByButton((Button) event.getSource());
        }
        else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    public static void passUser(User passedUser) {
        loggedUser = passedUser;
    }
}

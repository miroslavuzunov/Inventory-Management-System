package ims.controllers.primary;

import ims.controllers.resources.ClientControllerResources;
import ims.controllers.secondary.ClientCardReviewController;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.dialogs.CustomDialog;
import ims.supporting.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController extends ClientControllerResources implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUserInfo();
    }

    @FXML
    public void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == exitBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want exit from the system?");

            if (result == ButtonType.YES) {
                SceneController.getBack();
                UserSession.cleanUserSession();
                AbstractDao.closeEntityManager();
            }
        } else{
            ClientCardReviewController.setEgn(UserSession.getLoggedUser().getPersonInfo().getEgn());
            CustomDialog customDialog = new CustomDialog("ClientCardReview.fxml");
            customDialog.setTitle("Client card review");

            customDialog.showAndWait();
        }
    }

}

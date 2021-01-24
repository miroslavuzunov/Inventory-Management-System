package ims.controllers.primary;

import ims.controllers.contracts.SceneControllerConfig;
import ims.controllers.resources.SystemUserControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.enums.MenuType;
import ims.supporting.ProductStateTracker;
import ims.supporting.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class SystemUserController extends SystemUserControllerResources implements Initializable, SceneControllerConfig {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUserInfo();
        startProductTracker();
        try {
            initializeScenes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startProductTracker() {
        Thread stateTrackerThread = new Thread(new ProductStateTracker());

        stateTrackerThread.start();
    }

    @FXML
    protected void handleClicks(ActionEvent event) throws IOException {
        handleSubMenuOpened(event);
        handleRegistrationType(event);
        handleOtherEvents(event);
    }

    private void handleSubMenuOpened(ActionEvent event) {
        if (event.getSource() == clientManipBtn)
            showClientManipulationSubMenu();
        if (event.getSource() == getBackFromClientManipBtn) {
            hideClientManipulationSubMenu();
        }
    }

    private void handleOtherEvents(ActionEvent event) throws IOException {
        if (event.getSource() == exitBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want exit from the system?");

            if (result == ButtonType.YES) {
                SceneController.getBack();
                UserSession.cleanUserSession();
                AbstractDao.closeEntityManager();
            }
        } else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    private void showClientManipulationSubMenu() {
        clientManipPane.setPrefWidth(200);
        clientManipPane.toFront();
        showButtonText(MenuType.SUBMENU);
    }

    private void hideClientManipulationSubMenu() {
        clientManipPane.setPrefWidth(60);
        clientManipPane.toBack();
        hideButtonText(MenuType.SUBMENU);
    }

    protected abstract void handleRegistrationType(ActionEvent event);
}

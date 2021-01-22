package ims.controllers.primary;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import ims.controllers.resources.AdminControllerResources;
import ims.controllers.secondary.RegisterUserController;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.enums.Role;
import ims.supporting.*;
import javafx.animation.*;
import javafx.beans.value.WritableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;


public class AdminController extends AdminControllerResources implements Initializable{

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userStatus.setText("Status: " + LoginController.getLoggedUser().getRole());
        todaysDate.setText("Today's date: " + LocalDate.now().toString());
        nameLabel.setText("Hello, " + LoginController.getLoggedUser().getPersonInfo().getFirstName() + "!");
        AbstractDao.closeEntityManager();

        initializeScenes();
        startProductTracker();
    }

    public void startProductTracker() {
        Thread stateTrackerThread = new Thread(new ProductStateTracker());

        stateTrackerThread.start();
    }

    @FXML
    private void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == clientManipBtn)
            showClientManipulationSubMenu();
        if (event.getSource() == getBackFromClientManipBtn) {
            hideClientManipulationSubMenu();
        }
        if (event.getSource() == regMrtBtn)
            RegisterUserController.passRole(Role.MRT);
        if (event.getSource() == regCardBtn)
            RegisterUserController.passRole(Role.CLIENT);

        if (event.getSource() == exitBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want exit from the system?");

            if (result == ButtonType.YES)
                SceneController.getBack();
        } else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    private void showClientManipulationSubMenu() {
        clientManipPane.setPrefWidth(200);
        clientManipPane.toFront();

        checkClientProductsBtn.setStyle("-fx-content-display: left;");
        getBackFromClientManipBtn.setStyle("-fx-content-display: left;");
        clientManipLabel.setStyle("-fx-content-display: center;");
        regCardBtn.setStyle("-fx-content-display: left;");
    }

    private void hideClientManipulationSubMenu() {
        clientManipPane.setPrefWidth(60);
        clientManipPane.toBack();

        checkClientProductsBtn.setStyle("-fx-content-display: graphic-only;");
        getBackFromClientManipBtn.setStyle("-fx-content-display: graphic-only;");
        clientManipLabel.setStyle("-fx-content-display: graphic-only;");
        regCardBtn.setStyle("-fx-content-display: graphic-only;");
    }

    @FXML
    public void openMenu() {
        //TODO refactoring

        mainPane.toBack();

        Timeline flash = new Timeline(
                new KeyFrame(Duration.seconds(0.20), new KeyValue(writableWidth, 200.00, Interpolator.EASE_IN))
        );
        flash.setCycleCount(1);
        flash.play();

        leftPane.setPrefWidth(200);

        regMrtBtn.setStyle("-fx-content-display: left;");
        notificationBtn.setStyle("-fx-content-display: left;");
        referencesBtn.setStyle("-fx-content-display: left;");
        forcedScrapBtn.setStyle("-fx-content-display: left;");
        regProductBtn.setStyle("-fx-content-display: left;");
        clientManipBtn.setStyle("-fx-content-display: left;");
        exitBtn.setStyle("-fx-content-display: left;");
        homeLabel.setStyle("-fx-content-display: center;");
    }

    @FXML
    public void closeMenu() {
        //TODO refactoring

        leftPane.setPrefWidth(60);
        fadeMain.toBack();

        Timeline flash = new Timeline(
                new KeyFrame(Duration.seconds(0.15), new KeyValue(writableWidth, 60.00, Interpolator.EASE_OUT))
        );
        flash.setCycleCount(1);
        flash.play();

        regMrtBtn.setStyle("-fx-content-display: graphic-only;");
        notificationBtn.setStyle("-fx-content-display: graphic-only;");
        referencesBtn.setStyle("-fx-content-display: graphic-only;");
        forcedScrapBtn.setStyle("-fx-content-display: graphic-only;");
        regProductBtn.setStyle("-fx-content-display: graphic-only;");
        clientManipBtn.setStyle("-fx-content-display: graphic-only;");
        exitBtn.setStyle("-fx-content-display: graphic-only;");
        homeLabel.setStyle("-fx-content-display: graphic-only;");
    }

    WritableValue<Double> writableWidth = new WritableValue<Double>() {
        @Override
        public Double getValue() {
            return leftPane.getWidth();
        }

        @Override
        public void setValue(Double value) {
            leftPane.setPrefWidth(value);
        }
    };
}

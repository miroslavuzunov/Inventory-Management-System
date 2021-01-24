package ims.controllers.resources;

import ims.controllers.primary.BaseUserController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class SystemUserControllerResources extends BaseUserController {
    @FXML
    protected Pane fadeMain;

    @FXML
    protected AnchorPane mainPane;

    @FXML
    protected Label nameLabel;

    @FXML
    protected VBox clientManipPane;

    @FXML
    protected Button regCardBtn;

    @FXML
    protected Button checkClientProductsBtn;

    @FXML
    protected VBox leftPane;

    @FXML
    protected Button notificationBtn;

    @FXML
    protected Button referencesBtn;

    @FXML
    protected Button forcedScrapBtn;

    @FXML
    protected Button regProductBtn;

    @FXML
    protected Button clientManipBtn;

    @FXML
    protected Label todaysDate;

    @FXML
    protected Label userStatus;

    @FXML
    protected Button exitBtn;

    @FXML
    protected Button getBackFromClientManipBtn;

    @FXML
    protected Label homeLabel;

    @FXML
    protected Label clientManipLabel;
}

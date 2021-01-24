package ims.controllers.resources;

import ims.controllers.primary.BaseUserController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class ClientControllerResources extends BaseUserController {
    @FXML
    protected Pane fadeMain;

    @FXML
    protected AnchorPane mainPane;

    @FXML
    protected Label nameLabel;

    @FXML
    protected Label userStatus;

    @FXML
    protected Label todaysDate;

    @FXML
    protected VBox leftPane;

    @FXML
    protected Label homeLabel;

    @FXML
    protected Button checkClientProductsBtn;

    @FXML
    protected Button exitBtn;
}

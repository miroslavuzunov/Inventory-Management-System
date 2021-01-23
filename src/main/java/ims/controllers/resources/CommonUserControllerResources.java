package ims.controllers.resources;

import ims.controllers.contracts.SceneControllerConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class CommonUserControllerResources{
    @FXML
    protected Pane fadeMain;

    @FXML
    protected AnchorPane mainPane;

    @FXML
    protected Label nameLabel;

    @FXML
    protected VBox clientManipPane;

    @FXML
    protected VBox leftPane;

    @FXML
    protected Label todaysDate;

    @FXML
    protected Label userStatus;
}

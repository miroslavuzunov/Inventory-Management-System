package ims.controllers.resources;

import ims.controllers.primary.SceneController;
import ims.controllers.contracts.SceneControllerConfig;
import ims.supporting.CustomScene;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public abstract class AdminControllerResources implements SceneControllerConfig {

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
    protected Button regMrtBtn;

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

    @Override
    public void initializeScenes() {
        SceneController.initializeScenes(new HashMap<>() {{
            put(regMrtBtn, new CustomScene("RegisterUser"));
            put(regCardBtn, new CustomScene("RegisterUser"));
            put(referencesBtn, new CustomScene("References"));
            put(notificationBtn, new CustomScene("Notifications"));
            put(forcedScrapBtn, new CustomScene(""));
            put(regProductBtn, new CustomScene("RegisterProduct"));
            put(checkClientProductsBtn, new CustomScene("ClientCard"));
        }});
    }
}

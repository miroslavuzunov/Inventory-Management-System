package ims.controllers.resources;

import ims.controllers.SceneController;
import ims.controllers.contracts.ControllerConfig;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public abstract class AdminControllerResources implements ControllerConfig {

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
    protected Button addProductBtn;

    @FXML
    protected Button removeProductBtn;

    @FXML
    protected Button checkClientProductsBtn;

    @FXML
    protected VBox genRefPane;

    @FXML
    protected Button allProductsBtn;

    @FXML
    protected Button checkStatusBtn;

    @FXML
    protected Button productsByCategoryBtn;

    @FXML
    protected Button scrappedProductsBtn;

    @FXML
    protected VBox leftPane;

    @FXML
    protected Button regMrtBtn;

    @FXML
    protected Button notificationBtn;

    @FXML
    protected Button genRefBtn;

    @FXML
    protected Button stockRefBtn;

    @FXML
    protected Button regProductBtn;

    @FXML
    protected Button clientManipBtn;

    @FXML
    protected Label todaysDate;

    @FXML
    protected Label userStatus;

    @FXML
    private Button exitBtn;

    @FXML
    private Button getBackFromClientManipBtn;

    @FXML
    private Button getBackFromGenRefBtn;

    @Override
    public void initializeScenes(){
        SceneController.loadScenes(new HashMap<>(){{
            put(regMrtBtn, "RegisterMrt");
            put(notificationBtn, "");
            put(stockRefBtn, "");
            put(regProductBtn, "");
            put(exitBtn, "Login");
            put(getBackFromClientManipBtn, "Admin");
            put(getBackFromGenRefBtn, "Admin");
        }});
    }
}

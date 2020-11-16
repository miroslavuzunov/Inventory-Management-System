package ims.controllers.resources;

import ims.controllers.contracts.ControllerConfig;
import ims.controllers.primary.SceneController;
import ims.controllers.secondary.AbstractController;
import ims.entities.Product;
import ims.supporting.TableProduct;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public abstract class ClientCardControllerResources extends AbstractController implements ControllerConfig {
    public static final String EGN_FIELD_NAME = "Egn";

    @FXML
    protected VBox cleanLeftPane;

    @FXML
    protected Button backBtn;

    @FXML
    protected AnchorPane regPane;

    @FXML
    protected Label clientName;

    @FXML
    protected TableView<TableProduct> clientsProductsTable;

    @FXML
    protected TableColumn<String, String> productColumn;

    @FXML
    protected TableColumn<String, String> invNumberColumn;

    @FXML
    protected TableColumn<String, String> givenByColumn;

    @FXML
    protected TableColumn<String, String> givenOnColumn;

    @FXML
    protected Button removeSelectedBtn;

    @FXML
    protected Button addAnotherBtn;

    @FXML
    protected TextField egnField;

    @FXML
    protected Label egnMsg;

    @FXML
    protected Button searchBtn;

    @FXML
    protected DatePicker startDate;

    @FXML
    protected DatePicker endDate;


    @Override
    public void initializeScenes() {
        SceneController.loadScenes(new HashMap<>() {{
            put(backBtn, "Admin");
        }});
    }
}

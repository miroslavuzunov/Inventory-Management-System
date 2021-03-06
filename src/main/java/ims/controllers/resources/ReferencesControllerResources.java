package ims.controllers.resources;

import ims.controllers.contracts.SceneControllerConfig;
import ims.controllers.primary.SceneController;
import ims.supporting.TableProduct;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public abstract class ReferencesControllerResources{
    @FXML
    protected VBox cleanLeftPane;

    @FXML
    protected Button backBtn;

    @FXML
    protected AnchorPane regPane;

    @FXML
    protected TableView<TableProduct> productsTable;

    @FXML
    protected TableColumn<String, String> productColumn;

    @FXML
    protected TableColumn<String, String> invNumberColumn;

    @FXML
    protected TableColumn<String, String> productTypeColumn;

    @FXML
    protected TableColumn<String, String> statusColumn;

    @FXML
    protected TableColumn<String, String> initialPriceColumn;

    @FXML
    protected TableColumn<String, String> currentPriceColumn;

    @FXML
    protected TableColumn<String, String> registrationDateColumn;

    @FXML
    protected DatePicker startDate;

    @FXML
    protected DatePicker endDate;

    @FXML
    protected CheckBox taCheckBox;

    @FXML
    protected CheckBox lttaCheckBox;

    @FXML
    protected CheckBox scrappedCheckBox;

    @FXML
    protected CheckBox availableCheckBox;

    @FXML
    protected CheckBox busyCheckBox;

    @FXML
    protected CheckBox missingCheckBox;
}

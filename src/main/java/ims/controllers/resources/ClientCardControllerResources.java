package ims.controllers.resources;

import ims.controllers.secondary.InputFieldBasedController;
import ims.supporting.TableProduct;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public abstract class ClientCardControllerResources extends InputFieldBasedController {
    public static final String EGN_FIELD_NAME = "Egn";

    @FXML
    protected VBox cleanLeftPane;

    @FXML
    protected Button backBtn;

    @FXML
    protected AnchorPane regPane;

    @FXML
    protected Label clientNameLabel;

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
    protected TableColumn<String, String> statusColumn;

    @FXML
    protected TableColumn<String, Button> buttonColumn;

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
}

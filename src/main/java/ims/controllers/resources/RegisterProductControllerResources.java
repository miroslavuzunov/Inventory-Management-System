package ims.controllers.resources;

import ims.controllers.contracts.SceneControllerConfig;
import ims.controllers.primary.SceneController;
import ims.controllers.secondary.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public abstract class RegisterProductControllerResources extends AbstractController{
    public static final String MODEL_OF_BRAND_EXISTS = "Such model of this brand already figures in the system!";
    public static final String EMPTY_FIELD_MSG = "Empty field forbidden!";
    public static final String INVALID_INFO_MSG = "Invalid information entered!";
    public static final String CLEAN_MSG = "";
    public static final String BRAND_FIELD_NAME = "Brand";
    public static final String BRAND_MODEL_EXISTS = "Such model of this brand is already in the system!";
    public static final String MODEL_FIELD_NAME = "Field";
    public static final String DESCRIPTION_FIELD_NAME = "Description";
    public static final String UNIT_PRICE_FIELD_NAME = "Unit price";
    public static final String UNIT_PRICE_COMBO_NAME = "Unit price currency";
    public static final String QUANTITY_FIELD_NAME = "Quantity";
    public static final String PRODUCT_TYPE_FIELD_NAME = "Product type";
    public static final String SCRAPPING_CRITERIA_FIELD_NAME = "Scrapping criteria";
    public static final String DEPRECIATION_DEGREE_FIELD_NAME = "Depreciation degree";
    public static final String SCRAP_PERIOD_FIELD_NAME = "Scrap period";

    @FXML
    protected AnchorPane regPane;

    @FXML
    protected TextField brandField;

    @FXML
    protected Label brandMsg;

    @FXML
    protected TextField modelField;

    @FXML
    protected Label modelMsg;

    @FXML
    protected TextArea descriptionArea;

    @FXML
    protected TextField priceValueField;

    @FXML
    protected ComboBox<String> priceUnitComboBox;

    @FXML
    protected Label priceMsg;

    @FXML
    protected TextField quantityField;

    @FXML
    protected Label quantityMsg;

    @FXML
    protected RadioButton lttaRadioBtn;

    @FXML
    protected RadioButton taRadioBtn;

    @FXML
    protected AnchorPane lttaPanel;

    @FXML
    protected VBox depreciationDegreeVBox;

    @FXML
    protected ComboBox<String> depreciationDegreeComboBox;

    @FXML
    protected Label depreciationDegreeMsg;

    @FXML
    protected Button addProductBtn;

    @FXML
    protected VBox cleanLeftPane;

    @FXML
    protected Button backBtn;
}

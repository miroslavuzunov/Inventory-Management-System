package ims.controllers.resources;

import ims.controllers.contracts.ControllerConfig;
import ims.controllers.primary.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public abstract class RegisterProductControllerResources implements ControllerConfig {
    @FXML
    protected AnchorPane regPane;

    @FXML
    protected Button addProductBtn;

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
    protected Pane hideCriteriaValuePane;

    @FXML
    protected Pane hideCriteriaValuePane1;

    @FXML
    protected ComboBox<String> depreciationDegreeComboBox;

    @FXML
    protected Label depreciationDegreeMsg;

    @FXML
    protected TextField yearsOrMonthsField;

    @FXML
    protected Label yearsOrMonthsMsg;

    @FXML
    protected ComboBox<String> scrappingCriteriaComboBox;

    @FXML
    protected Label scrappingCriteriaMsg;

    @FXML
    protected VBox cleanLeftPane;

    @FXML
    protected Button backBtn;

    @Override
    public void initializeScenes() {
        SceneController.loadScenes(new HashMap<>() {{
            put(backBtn, "Admin");
        }});
    }
}

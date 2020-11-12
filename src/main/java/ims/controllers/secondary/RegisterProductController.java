package ims.controllers.secondary;

import ims.App;
import ims.controllers.primary.SceneController;
import ims.controllers.resources.RegisterProductControllerResources;
import ims.dialogs.ConfirmationDialog;
import ims.dialogs.SuccessDialog;
import ims.enums.PriceCurrency;
import ims.services.ProductRegistrationService;
import ims.supporting.Cache;
import ims.supporting.CustomField;
import ims.supporting.GroupToggler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class RegisterProductController extends RegisterProductControllerResources implements Initializable {
    private ProductRegistrationService productRegistrationService;
    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productRegistrationService = new ProductRegistrationService();

        toggleGroup = GroupToggler.makeToggleGroup(List.of(lttaRadioBtn, taRadioBtn));
        initializeScenes();
        initializeCurrencies();
        manipulateLttaPanel();
    }

    @FXML
    private void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == addProductBtn)
            addProduct();
        if (event.getSource() == backBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Input data will be lost. Are you sure you want to get back?");

            if (result == ButtonType.YES)
                SceneController.switchSceneByButton((Button) event.getSource());
        } else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    @FXML
    private void manipulateLttaPanel() {
        if (toggleGroup.getSelectedToggle().equals(lttaRadioBtn))
            enableLttaOptions();
        if (toggleGroup.getSelectedToggle().equals(taRadioBtn))
            disableLttaOptions();
    }

    private void addProduct() throws IOException {
        boolean noEmptyFields = true;
        boolean noBusyData = true;

        noEmptyFields = handleEmptyFields();

        if (noEmptyFields)
            noBusyData = handleBusyData(customFieldsByName);

        if(noEmptyFields && noBusyData){
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want to sign up new MRT?");

            if (result == ButtonType.YES) {
                productRegistrationService.createProduct(customFieldsByName);
                SuccessDialog.success("Done! New product added to the system!");
                App.setScene("/view/RegisterProduct"); //reloading same scene to clean the fields
            }
        }
    }

    @Override
    protected void initializeCustomFields() {
        customFieldsByName.put(BRAND_FIELD_NAME, new CustomField(brandField.getText()));
        customFieldsByName.put(MODEL_FIELD_NAME, new CustomField(modelField.getText()));
        customFieldsByName.put(DESCRIPTION_FIELD_NAME, new CustomField(descriptionArea.getText(), true));
        customFieldsByName.put(UNIT_PRICE_FIELD_NAME, new CustomField(priceValueField.getText()));
        customFieldsByName.put(UNIT_PRICE_COMBO_NAME, new CustomField(priceUnitComboBox.getSelectionModel().getSelectedItem()));
        customFieldsByName.put(QUANTITY_FIELD_NAME, new CustomField(quantityField.getText()));

        if (toggleGroup.getSelectedToggle().equals(lttaRadioBtn)) {
            customFieldsByName.put(PRODUCT_TYPE_FIELD_NAME, new CustomField("LTTA"));
            customFieldsByName.put(DEPRECIATION_DEGREE_FIELD_NAME, new CustomField(depreciationDegreeComboBox.getSelectionModel().getSelectedItem()));
        } else {
            customFieldsByName.put(PRODUCT_TYPE_FIELD_NAME, new CustomField("TA"));
            customFieldsByName.put(DEPRECIATION_DEGREE_FIELD_NAME, new CustomField(depreciationDegreeComboBox.getSelectionModel().getSelectedItem(), true));
        }
    }

    private boolean handleBusyData(Map<String, CustomField> fieldsByName) {
        boolean handlingResult = true;
        handlingResult = productRegistrationService.validateData(fieldsByName);

        for (CustomField field : fieldsByName.values())
            handleField(field, field.getState(), field.getMessage());

        displayMessages(fieldsByName);

        return handlingResult;
    }

    private void initializeCurrencies() {
        for (PriceCurrency currency : PriceCurrency.values()) {
            priceUnitComboBox.getItems().add(currency.toString());
        }
    }

    private void initializeDepreciationDegree() {
        depreciationDegreeComboBox.getItems().clear(); //Prevents data duplicating

        List<CustomField> degreesFromDb = Cache.getCachedFields("Degrees");

        if (degreesFromDb == null) {
            degreesFromDb = productRegistrationService.initializeDepreciationDegree();
            Cache.cacheList("Degrees", degreesFromDb);
        }

        degreesFromDb.forEach(degree -> {
            depreciationDegreeComboBox.getItems().add(degree.getFieldValue());
        });
    }

    private void enableLttaOptions() {
        lttaPanel.setDisable(false);
        initializeDepreciationDegree();
    }

    private void disableLttaOptions() {
        lttaPanel.setDisable(true);
    }

    @Override
    protected void displayMessages(Map<String, CustomField> fieldsByName) {
        brandMsg.setText(fieldsByName.get(BRAND_FIELD_NAME).getMessage());
        brandField.setStyle(fieldsByName.get(BRAND_FIELD_NAME).getStyle());
        modelMsg.setText(fieldsByName.get(MODEL_FIELD_NAME).getMessage());
        modelField.setStyle(fieldsByName.get(MODEL_FIELD_NAME).getStyle());
        priceMsg.setText(fieldsByName.get(UNIT_PRICE_COMBO_NAME).getMessage());
        priceMsg.setText(fieldsByName.get(UNIT_PRICE_FIELD_NAME).getMessage());
        priceUnitComboBox.setStyle(fieldsByName.get(UNIT_PRICE_COMBO_NAME).getStyle());
        priceValueField.setStyle(fieldsByName.get(UNIT_PRICE_FIELD_NAME).getStyle());
        quantityMsg.setText(fieldsByName.get(QUANTITY_FIELD_NAME).getMessage());
        quantityField.setStyle(fieldsByName.get(QUANTITY_FIELD_NAME).getStyle());

        if (toggleGroup.getSelectedToggle().equals(lttaRadioBtn)) {
            depreciationDegreeMsg.setText(fieldsByName.get(DEPRECIATION_DEGREE_FIELD_NAME).getMessage());
            depreciationDegreeComboBox.setStyle(fieldsByName.get(DEPRECIATION_DEGREE_FIELD_NAME).getStyle());
        }
    }

}

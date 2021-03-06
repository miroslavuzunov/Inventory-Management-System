package ims.controllers.secondary;

import ims.controllers.contracts.EventBasedController;
import ims.controllers.primary.SceneController;
import ims.controllers.resources.RegisterProductControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.dialogs.CustomDialog;
import ims.dialogs.InformationDialog;
import ims.entities.ProductDetails;
import ims.enums.PriceCurrency;
import ims.services.ProductRegistrationService;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class RegisterProductController extends RegisterProductControllerResources implements Initializable, EventBasedController {
    private ProductRegistrationService productRegistrationService;
    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();

        productRegistrationService = new ProductRegistrationService();
        setInterfaceElements();
        initializeCustomFieldMap();
        initializeCurrencies();
        setLttaPanel();
    }

    @FXML
    public void handleClicks(ActionEvent event) throws IOException{
        if (event.getSource() == backBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Input data will be lost. Are you sure you want to get back?");

            if (result == ButtonType.YES) {
                SceneController.getBack();
                AbstractDao.closeEntityManager();
            }
        } else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    @FXML
    private void setLttaPanel() {
        if (toggleGroup.getSelectedToggle().equals(lttaRadioBtn))
            enableLttaOptions();
        if (toggleGroup.getSelectedToggle().equals(taRadioBtn))
            disableLttaOptions();
    }

    private void enableLttaOptions() {
        customFieldsByName.get(DEPRECIATION_DEGREE_FIELD_NAME).setNullable(false);
        lttaPanel.setDisable(false);
        initializeDepreciationDegree();
    }

    private void disableLttaOptions() {
        customFieldsByName.get(DEPRECIATION_DEGREE_FIELD_NAME).setNullable(true);
        lttaPanel.setDisable(true);
    }

    private void setInterfaceElements() {
        toggleGroup = GroupToggler.makeToggleGroup(List.of(lttaRadioBtn, taRadioBtn));
    }

    private void initializeCurrencies() {
        for (PriceCurrency currency : PriceCurrency.values()) {
            priceUnitComboBox.getItems().add(currency.toString());
        }
    }

    private void initializeDepreciationDegree() {
        depreciationDegreeComboBox.getItems().clear(); //Prevents data duplicating

        List<CustomField> degreesFromDb = productRegistrationService.getDepreciationDegrees();

        degreesFromDb.forEach(degree -> {
            depreciationDegreeComboBox.getItems().add(degree.getFieldValue());
        });
    }

    @FXML
    private void addProduct() throws IOException, NoSuchFieldException {
        if (isInputDataValid()) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want to add new product to the system?");

            if (result == ButtonType.YES) {
                productRegistrationService.registerProduct(customFieldsByName);
                InformationDialog.displayInformation("Done! New product added to the system!");
            }
        }
    }

    private boolean isInputDataValid() throws NoSuchFieldException, IOException {
        boolean noEmptyFields = true;
        boolean isDataBusy = true;
        boolean noForbiddenChars = true;

        noEmptyFields = handleEmptyFields();
        //noForbiddenChars = handleForbiddenChars(inputFields);

        boolean isInitialCheckValid = noEmptyFields && noForbiddenChars;

        if(isInitialCheckValid)
            isDataBusy = isDataBusy(customFieldsByName);

        if(isDataBusy && noEmptyFields)
            addProductQuantity();

        return !isDataBusy;
    }

    private boolean isDataBusy(Map<String, CustomField> fieldsByName) throws NoSuchFieldException {
        boolean isDataBusy = productRegistrationService.isDataBusy(fieldsByName);

        for (CustomField field : fieldsByName.values()) {
            editField(field, field.getState(), field.getMessage());
        }

        displayMessages(fieldsByName);

        return isDataBusy;
    }

    private void addProductQuantity() throws IOException, NoSuchFieldException {
        CustomDialog customDialog = new CustomDialog("AddProductQuantity.fxml");
        customDialog.setTitle("Adding quantity to existing product");

        Optional<ButtonType> clickedButton = customDialog.showAndWait();

        if (clickedButton.get() == ButtonType.OK) {
            ProductDetails productDetails = AddProductQuantityController.getSelectedProduct();
            int quantity = AddProductQuantityController.getNewQuantity();

            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want to add new quantity to the same product?");

            if (result == ButtonType.YES) {
                customFieldsByName.get(QUANTITY_FIELD_NAME).setFieldValue(String.valueOf(quantity));
                productRegistrationService.generateProductsByDetails(customFieldsByName, productDetails, quantity);
                productRegistrationService.updateProductQuantity(productDetails, quantity);

                InformationDialog.displayInformation("Done! New quantity added to the product!");
            }
        }
    }

    @Override
    protected void initializeCustomFieldMap() {
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

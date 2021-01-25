package ims.controllers.secondary;

import ims.dialogs.ErrorDialog;
import ims.entities.ProductDetails;
import ims.services.ProductRegistrationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddProductQuantityController implements Initializable {
    @FXML
    private ComboBox<String> brandModelComboBox;
    @FXML
    private TextField productQuantity;

    private static String selectedItem;
    private static int quantity;
    private static final ProductRegistrationService productRegistrationService = new ProductRegistrationService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<ProductDetails> productsFromDb = productRegistrationService.getAllProductsDetails();

        productsFromDb.forEach(productDetails -> {
            brandModelComboBox.getItems().add(productDetails.getBrandAndModel());
        });

        productQuantity.textProperty().addListener(observable -> {
            setQuantity();
        });
    }

    @FXML
    private void setSelectedItem(ActionEvent event) {
        selectedItem = brandModelComboBox.getSelectionModel().getSelectedItem();
    }

    private void setQuantity() {
        try {
            quantity = Integer.parseInt(productQuantity.getText());
        }catch (Exception e){
            e.printStackTrace();
            ErrorDialog.callError("Quantity must be a number!");
        }
    }

    public static ProductDetails getSelectedProduct() throws NoSuchFieldException {
        return productRegistrationService.getProductDetailsByBrandAndModel(selectedItem);
    }

    public static int getNewQuantity() {
        return quantity;
    }
}

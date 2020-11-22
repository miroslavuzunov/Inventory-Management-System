package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.entities.Product;
import ims.entities.ProductDetails;
import ims.services.CardService;
import ims.supporting.TableProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    @FXML
    private TableView<TableProduct> listOfProductsAvailable;
    @FXML
    private TableColumn<String, String> brandAndModelColumn;
    @FXML
    private TableColumn<String, String> productTypeColumn;
    @FXML
    private TableColumn<String, String> totalQuantityColumn;
    @FXML
    private TableColumn<String, String> availableQuantityColumn;

    private final CardService cardService = new CardService();
    private static TableProduct selectedProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableColumns();

        cardService.getAllProductsDetails().forEach(productDetails -> {
            TableProduct tableProduct = new TableProduct();

            tableProduct.setBrand(productDetails.getBrandAndModel());
            tableProduct.setProductType(productDetails.getProductType().toString());
            tableProduct.setTotalQuantity(productDetails.getQuantity().toString());
            tableProduct.setAvailableQuantity(getProductAvailableQuantity(productDetails).toString());

            listOfProductsAvailable.getItems().add(tableProduct);
        });

        listOfProductsAvailable.getSelectionModel().selectedItemProperty().addListener((action) -> {
            selectedProduct = listOfProductsAvailable.getSelectionModel().getSelectedItem();
        });

    }

    private Integer getProductAvailableQuantity(ProductDetails productDetails) {
        int availableCount = 0;

        for (Product product : productDetails.getProducts()) {
            if (product.isAvailable())
                availableCount++;
        }

        return availableCount;
    }

    private void setTableColumns() {  //Mapping with TableProduct fields
        brandAndModelColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productType"));
        totalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
    }

    public static TableProduct getSelectedProduct() {
        return selectedProduct;
    }
}

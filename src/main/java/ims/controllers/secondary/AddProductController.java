package ims.controllers.secondary;

import ims.services.AddProductService;
import ims.supporting.Cache;
import ims.supporting.TableProduct;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
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

    private AddProductService addProductService;
    private List<TableProduct> tableProducts;
    private static TableProduct selectedProduct;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addProductService = new AddProductService();

        tableProducts = addProductService.getAllProducts();

        setTableColumns();
        fillTable(tableProducts);

        listOfProductsAvailable.getSelectionModel().selectedItemProperty().addListener((action) -> {
            selectedProduct = listOfProductsAvailable.getSelectionModel().getSelectedItem();
        });
    }

    private void setTableColumns() {  //Mapping with TableProduct fields
        brandAndModelColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productType"));
        totalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));
        availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
    }

    private void fillTable(List<TableProduct> tableProducts) {
        listOfProductsAvailable.getItems().clear();

        for (TableProduct product : tableProducts) {
            listOfProductsAvailable.getItems().add(product);
        }
    }

    public static TableProduct getSelectedProduct() {
        if (Integer.parseInt(selectedProduct.getAvailableQuantity()) > 0)
            return selectedProduct;
        return null;
    }
}

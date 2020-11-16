package ims.controllers.secondary;

import ims.entities.ProductDetails;
import ims.services.CardService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    @FXML
    private ListView<ProductDetails> listOfProductsAvailable;
    CardService cardService = new CardService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cardService.getAllProductsDetails().forEach(productDetails -> {
            listOfProductsAvailable.getItems().add(productDetails);
        });
    }
}

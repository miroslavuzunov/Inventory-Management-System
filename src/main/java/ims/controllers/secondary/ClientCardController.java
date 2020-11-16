package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.controllers.resources.ClientCardControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.entities.Product;
import ims.services.CardService;
import ims.supporting.CustomField;
import ims.supporting.TableProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class ClientCardController extends ClientCardControllerResources implements Initializable {
    private CardService cardService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();
        cardService = new CardService();
        initializeScenes();
        endDate.setValue(LocalDate.now());  //Default period
        startDate.setValue(endDate.getValue().minusYears(1));
    }

    @FXML
    public void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == backBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want to get back?");

            if (result == ButtonType.YES) {
                SceneController.switchSceneByButton((Button) event.getSource());
                AbstractDao.closeEntityManager();
            }
        } else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    @Override
    protected void initializeCustomFields() {
        customFieldsByName.put(EGN_FIELD_NAME, new CustomField(egnField.getText()));
    }

    @FXML
    private void searchByEgn() {
        boolean noEmptyFields = true;
        boolean noForbiddenChars = true;

        noEmptyFields = handleEmptyFields();
        //noForbiddenChars = handleForbiddenChars(inputFields);
        List<TableProduct> tableProducts = new ArrayList<>();
        StringBuilder clientsName = new StringBuilder();

        if (noEmptyFields && noForbiddenChars)
            tableProducts = cardService.getClientsProductsByEgnAndPeriod(
                    egnField.getText(),
                    clientsName,
                    startDate.getValue(),
                    endDate.getValue()
            );

        clientName.setText(String.valueOf(clientsName));
        setTableColumns();
        fillTable(tableProducts);
    }

    @FXML
    private void addAnotherProductToCard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/AddProduct.fxml"));
        DialogPane chooseProductDialog = fxmlLoader.load();
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setDialogPane(chooseProductDialog);
        dialog.setTitle("Adding new product to the card");
        dialog.setResizable(false);

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if(clickedButton.get() == ButtonType.OK){
            System.out.println("test");
            //cardService.addAnotherProductToCard();
        }
    }

    @FXML
    private void removeSelectedProductFromCard() {
        Product selectedProduct = clientsProductsTable.getSelectionModel().getSelectedItem().getProduct();

        clientsProductsTable.getItems().removeIf(new Predicate<TableProduct>() {
            @Override
            public boolean test(TableProduct tableProduct) {
                return tableProduct.getInvNum().equals(selectedProduct.getInventoryNumber());
            }
        });

        cardService.removeSelectedProduct(selectedProduct);
    }

    private void setTableColumns() {  //Mapping with TableProduct fields
        productColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        invNumberColumn.setCellValueFactory(new PropertyValueFactory<>("invNum"));
        givenByColumn.setCellValueFactory(new PropertyValueFactory<>("givenBy"));
        givenOnColumn.setCellValueFactory(new PropertyValueFactory<>("givenOn"));
    }

    private void fillTable(List<TableProduct> tableProducts) {
        clientsProductsTable.getItems().clear();
        for (TableProduct product : tableProducts) {
            clientsProductsTable.getItems().add(product);
        }
    }

    @Override
    protected void displayMessages(Map<String, CustomField> fieldsByName) {
        egnMsg.setText(fieldsByName.get(EGN_FIELD_NAME).getMessage());
        egnField.setStyle(fieldsByName.get(EGN_FIELD_NAME).getStyle());
    }
}

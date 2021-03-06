package ims.controllers.secondary;

import ims.controllers.contracts.EventBasedController;
import ims.controllers.primary.SceneController;
import ims.controllers.resources.ClientCardControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.dialogs.CustomDialog;
import ims.dialogs.ErrorDialog;
import ims.entities.Product;
import ims.services.ClientCardService;
import ims.supporting.CustomField;
import ims.supporting.TableProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClientCardController extends ClientCardControllerResources implements Initializable, EventBasedController {
    protected ClientCardService clientCardService;
    protected List<TableProduct> tableProducts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();
        clientCardService = new ClientCardService();
        tableProducts = new ArrayList<>();

        customizeTable();
        addAnotherBtn.setDisable(true);
        removeSelectedBtn.setDisable(true);
        endDate.setValue(LocalDate.now());  //Default period
        startDate.setValue(endDate.getValue().minusYears(1));
    }

    @FXML
    public void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == backBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want to get back?");

            if (result == ButtonType.YES) {
                SceneController.getBack();
                AbstractDao.closeEntityManager();
            }
        } else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    @Override
    protected void initializeCustomFieldMap() {
        customFieldsByName.put(EGN_FIELD_NAME, new CustomField(egnField.getText()));
    }

    @FXML
    protected void searchByEgn() throws NoSuchFieldException {
        boolean noEmptyFields = true;
        boolean noForbiddenChars = true;
        StringBuilder clientName = new StringBuilder();

        noEmptyFields = handleEmptyFields();
        //noForbiddenChars = handleForbiddenChars(inputFields);

        if (noEmptyFields && noForbiddenChars)
            tableProducts = clientCardService.getClientsProductsByEgn(egnField.getText(), clientName);

        clientNameLabel.setText(String.valueOf(clientName));

        setTableColumns();
        fillTable(tableProducts);
        handleButtonsStatus(noEmptyFields);
    }

    @FXML
    private void addAnotherProductToCard() throws IOException, NoSuchFieldException {
        CustomDialog customDialog = new CustomDialog("AddProduct.fxml");
        customDialog.setTitle("Adding new product to the card");

        Optional<ButtonType> clickedButton = customDialog.showAndWait();

        if (clickedButton.get() == ButtonType.OK) {
            if (AddProductController.getSelectedProduct() != null) {
                clientCardService.addAnotherProductToCard(AddProductController.getSelectedProduct(), egnField.getText());
                searchByEgn(); //Refreshes the table
            } else
                ErrorDialog.callError("There is no available quantity of this product!");
        }
    }

    @FXML
    private void removeSelectedProductFromCard() throws NoSuchFieldException {
        if (clientsProductsTable.getSelectionModel().getSelectedItem() != null) {
            Product selectedProduct = clientsProductsTable.getSelectionModel().getSelectedItem().getProduct();

            if (selectedProduct.isExisting()) {
                removeSelectedProductFromTable(selectedProduct);
                removeSelectedProductFromListOfProducts(selectedProduct);

                clientCardService.removeSelectedProduct(selectedProduct, egnField.getText());
                removeSelectedBtn.setDisable(tableProducts.isEmpty());
            } else
                ErrorDialog.callError("Missing product can't be returned!");
        }
    }

    private void removeSelectedProductFromTable(Product selectedProduct) {
        clientsProductsTable.getItems().removeIf(new Predicate<TableProduct>() {
            @Override
            public boolean test(TableProduct tableProduct) {
                return tableProduct.getInvNum().equals(selectedProduct.getInventoryNumber());
            }
        });
    }

    private void removeSelectedProductFromListOfProducts(Product selectedProduct) {
        tableProducts.removeIf(new Predicate<TableProduct>() {
            @Override
            public boolean test(TableProduct tableProduct) {
                return tableProduct.getProduct().equals(selectedProduct);
            }
        });
    }

    protected void handleButtonsStatus(boolean status) {
        boolean isDateInPeriod = isDateInPeriod(startDate.getValue(), endDate.getValue(), LocalDate.now());

        addAnotherBtn.setDisable(clientNameLabel.getText().equals("Client not found") || !status);
        if (!isDateInPeriod)
            addAnotherBtn.setDisable(true);
        removeSelectedBtn.setDisable(clientsProductsTable.getItems().isEmpty());
    }

    private boolean isDateInPeriod(LocalDate startDate, LocalDate endDate, LocalDate givenOn) {
        return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList()).contains(givenOn); // Checks if the transaction date is in the specified period (inclusive)
    }

    private Button getStatusButton() {
        Button button = new Button();

        button.setText("Change status");
        button.setCursor(Cursor.HAND);

        button.setOnAction((event -> {
            handleChangeStatusButton();
        }));

        return button;
    }

    private void handleChangeStatusButton() {
        TableProduct selectedRow = getSelectedRow();

        if (selectedRow != null) {
            if (selectedRow.getStatus().equals("Existing")) {
                selectedRow.setStatus("Missing");
                fillTable(tableProducts); //Refreshes the table
                clientCardService.changeProductStatus(selectedRow.getProduct(), false);
            } else {
                selectedRow.setStatus("Existing");
                fillTable(tableProducts);
                clientCardService.changeProductStatus(selectedRow.getProduct(), true);
            }
        }
    }

    private TableProduct getSelectedRow() {
        for (TableProduct tableProduct : clientsProductsTable.getItems()) {
            if (tableProduct.getButton().isFocused())
                return tableProduct;
        }

        return null;
    }

    protected void customizeTable() {
        clientsProductsTable.getColumns().forEach(tableProductTableColumn -> {
            tableProductTableColumn.setResizable(false);
            tableProductTableColumn.setStyle("-fx-alignment: CENTER;");
        });
    }


    protected void setTableColumns() {  //Mapping with TableProduct fields
        productColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        invNumberColumn.setCellValueFactory(new PropertyValueFactory<>("invNum"));
        givenByColumn.setCellValueFactory(new PropertyValueFactory<>("givenBy"));
        givenOnColumn.setCellValueFactory(new PropertyValueFactory<>("givenOn"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        buttonColumn.setCellValueFactory(new PropertyValueFactory<>("button"));
    }

    private void fillTable(List<TableProduct> tableProducts) {
        clientsProductsTable.getItems().clear();

        for (TableProduct product : tableProducts) {
            if (isDateInPeriod(startDate.getValue(), endDate.getValue(), LocalDate.parse(product.getGivenOn()))) {
                product.setButton(getStatusButton());
                clientsProductsTable.getItems().add(product);
            }
        }
    }

    @Override
    protected void displayMessages(Map<String, CustomField> fieldsByName) {
        egnMsg.setText(fieldsByName.get(EGN_FIELD_NAME).getMessage());
        egnField.setStyle(fieldsByName.get(EGN_FIELD_NAME).getStyle());
    }

}

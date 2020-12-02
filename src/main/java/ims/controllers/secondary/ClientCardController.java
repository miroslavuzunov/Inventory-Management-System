package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.controllers.resources.ClientCardControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.dialogs.CustomDialog;
import ims.dialogs.ErrorDialog;
import ims.entities.Product;
import ims.services.ClientCardService;
import ims.supporting.Cache;
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

public class ClientCardController extends ClientCardControllerResources implements Initializable {
    private ClientCardService clientCardService;
    private List<TableProduct> tableProducts;
    private static Cache cache = new Cache();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();
        clientCardService = new ClientCardService();
        tableProducts = new ArrayList<>();

        initializeScenes();

        customizeTable();
        addAnotherBtn.setDisable(true);
        removeSelectedBtn.setDisable(true);
        endDate.setValue(LocalDate.now());  //Default period
        startDate.setValue(endDate.getValue().minusYears(1));
    }

    @FXML
    private void handleClicks(ActionEvent event) throws IOException {
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
    private void searchByEgn() throws NoSuchFieldException {
        boolean noEmptyFields = true;
        boolean noForbiddenChars = true;
        StringBuilder clientName = new StringBuilder();

        tableProducts = (List<TableProduct>) cache.getCachedCollection(egnField.getText());

        noEmptyFields = handleEmptyFields();
        //noForbiddenChars = handleForbiddenChars(inputFields);

        if (tableProducts == null) {
            if (noEmptyFields && noForbiddenChars)
                tableProducts = clientCardService.getClientsProductsByEgn(
                        egnField.getText(),
                        clientName
                );
            else
                tableProducts = new ArrayList<>();

            cache.cacheCollection(egnField.getText(), tableProducts);
            cache.cacheObject(egnField.getText(), clientName);
        } else {
            clientName = (StringBuilder) cache.getCachedObject(egnField.getText());
        }

        this.clientNameLabel.setText(String.valueOf(clientName));

        setTableColumns();
        fillTable(tableProducts);
        handleButtonsStatus(noEmptyFields);
    }

    @FXML
    private void addAnotherProductToCard() throws IOException, NoSuchFieldException {
        CustomDialog customDialog = new CustomDialog("AddProduct.fxml");
        customDialog.setTitle("Adding new product to the card");
        customDialog.setResizable(false);

        Optional<ButtonType> clickedButton = customDialog.showAndWait();

        if (clickedButton.get() == ButtonType.OK) {
            if (AddProductController.getSelectedProduct() != null) {
                clientCardService.addAnotherProductToCard(AddProductController.getSelectedProduct(), egnField.getText());
                cache.clearCachedCollection(egnField.getText()); //Cache must be refreshed
                searchByEgn(); //Refreshes the table
            } else
                ErrorDialog.callError("There is no available quantity of this product!");
        }
    }

    @FXML
    private void removeSelectedProductFromCard() throws NoSuchFieldException {
        Product selectedProduct = clientsProductsTable.getSelectionModel().getSelectedItem().getProduct();

        clientsProductsTable.getItems().removeIf(new Predicate<TableProduct>() {
            @Override
            public boolean test(TableProduct tableProduct) {
                return tableProduct.getInvNum().equals(selectedProduct.getInventoryNumber());
            }
        });

        tableProducts.removeIf(new Predicate<TableProduct>() {
            @Override
            public boolean test(TableProduct tableProduct) {
                return tableProduct.getProduct().equals(selectedProduct);
            }
        });

        clientCardService.removeSelectedProduct(selectedProduct, egnField.getText());
        removeSelectedBtn.setDisable(tableProducts.isEmpty());
    }

    private void handleButtonsStatus(boolean status) {
        boolean isDateInPeriod = isDateInPeriod(startDate.getValue(), endDate.getValue(), LocalDate.now());

        addAnotherBtn.setDisable(clientNameLabel.getText().equals("Client not found") || !status);
        if (!isDateInPeriod)
            addAnotherBtn.setDisable(true);
        removeSelectedBtn.setDisable(clientsProductsTable.getItems().isEmpty());
    }

    private void setTableColumns() {  //Mapping with TableProduct fields
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

    private boolean isDateInPeriod(LocalDate startDate, LocalDate endDate, LocalDate givenOn) {
        return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList()).contains(givenOn); // Checks if the transaction date is in the specified period (inclusive)
    }

    @Override
    protected void displayMessages(Map<String, CustomField> fieldsByName) {
        egnMsg.setText(fieldsByName.get(EGN_FIELD_NAME).getMessage());
        egnField.setStyle(fieldsByName.get(EGN_FIELD_NAME).getStyle());
    }

    private void customizeTable() {
        clientsProductsTable.getColumns().forEach(tableProductTableColumn -> {
            tableProductTableColumn.setResizable(false);
            tableProductTableColumn.setStyle("-fx-alignment: CENTER;");
        });
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

}

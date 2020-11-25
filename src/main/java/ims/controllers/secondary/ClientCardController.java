package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.controllers.resources.ClientCardControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.dialogs.CustomDialog;
import ims.entities.Product;
import ims.services.CardService;
import ims.supporting.CustomField;
import ims.supporting.TableProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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
import java.util.stream.Collectors;

public class ClientCardController extends ClientCardControllerResources implements Initializable {
    private CardService cardService;
    List<TableProduct> tableProducts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();
        cardService = new CardService();
        tableProducts = new ArrayList<>();

        initializeScenes();

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

        noEmptyFields = handleEmptyFields();
        //noForbiddenChars = handleForbiddenChars(inputFields);
        StringBuilder clientName = new StringBuilder();

        if (noEmptyFields && noForbiddenChars)
            tableProducts = cardService.getClientsProductsByEgnAndPeriod(
                    egnField.getText(),
                    clientName,
                    startDate.getValue(),
                    endDate.getValue()
            );

        this.clientName.setText(String.valueOf(clientName));

        addAnotherBtn.setDisable(clientName.toString().equals("Client not found"));
        if(!cardService.isDateInPeriod(startDate.getValue(), endDate.getValue(), LocalDate.now()) ||
            !noEmptyFields)
            addAnotherBtn.setDisable(true);
        removeSelectedBtn.setDisable(tableProducts.isEmpty());

        setTableColumns();
        fillTable(tableProducts);
    }

    @FXML
    private void addAnotherProductToCard() throws IOException, NoSuchFieldException {
        CustomDialog customDialog = new CustomDialog("AddProduct.fxml");
        customDialog.setTitle("Adding new product to the card");
        customDialog.setResizable(false);

        Optional<ButtonType> clickedButton = customDialog.showAndWait();

        if (clickedButton.get() == ButtonType.OK) {
            cardService.addAnotherProductToCard(AddProductController.getSelectedProduct());
            searchByEgn(); //Refreshes the table
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

        tableProducts.removeIf(new Predicate<TableProduct>() {
            @Override
            public boolean test(TableProduct tableProduct) {
                return tableProduct.getProduct().equals(selectedProduct);
            }
        });

        cardService.removeSelectedProduct(selectedProduct);
        removeSelectedBtn.setDisable(tableProducts.isEmpty());
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
            product.setButton(getChangeStatusButton());
            clientsProductsTable.getItems().add(product);
        }
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

    private Button getChangeStatusButton() {
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
                cardService.changeProductStatus(selectedRow.getProduct(), false);
            } else {
                selectedRow.setStatus("Existing");
                fillTable(tableProducts);
                cardService.changeProductStatus(selectedRow.getProduct(), true);
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

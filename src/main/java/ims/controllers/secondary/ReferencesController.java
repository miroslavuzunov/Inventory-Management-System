package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.controllers.resources.ReferencesControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.dialogs.CustomDialog;
import ims.dialogs.ErrorDialog;
import ims.enums.FilterChoice;
import ims.services.ReferencesService;
import ims.supporting.TableProduct;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReferencesController extends ReferencesControllerResources implements Initializable {
    private ReferencesService referencesService;
    private Map<FilterChoice, Boolean> filterChoices;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();

        try {
            referencesService = new ReferencesService();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        filterChoices = new HashMap<>();

        setDefaultPeriod();
        customizeTable();
        updateFilterChoices();
        handleCheckedBoxes();
    }

    private void setDefaultPeriod() {
        endDate.setValue(LocalDate.now());
        startDate.setValue(endDate.getValue().minusYears(1));
    }

    @FXML
    private void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == backBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Are you sure you want to get back?");

            if (result == ButtonType.YES) {
                //SceneController.switchSceneByButton((Button) event.getSource());
                SceneController.getBack();
                AbstractDao.closeEntityManager();
            }
        } else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    @FXML
    private void handleCheckedBoxes() {
        updateFilterChoices();
        List<TableProduct> productsFromDb = new ArrayList<>();

        //TODO PAGINATION

        if (isFilterUsed())
            productsFromDb = referencesService.loadChecked(filterChoices);
        else
            productsFromDb = referencesService.loadAll();

        fillTable(productsFromDb);
    }

    @FXML
    private void clearFilter(ActionEvent event) {
        taCheckBox.setSelected(false);
        lttaCheckBox.setSelected(false);
        scrappedCheckBox.setSelected(false);
        availableCheckBox.setSelected(false);
        busyCheckBox.setSelected(false);
        missingCheckBox.setSelected(false);

        handleCheckedBoxes();
    }

    private void updateFilterChoices() {
        if (!filterChoices.isEmpty())
            filterChoices.clear();

        filterChoices.put(FilterChoice.TA, taCheckBox.isSelected());
        filterChoices.put(FilterChoice.LTTA, lttaCheckBox.isSelected());
        filterChoices.put(FilterChoice.SCRAPPED, scrappedCheckBox.isSelected());
        filterChoices.put(FilterChoice.AVAILABLE, availableCheckBox.isSelected());
        filterChoices.put(FilterChoice.BUSY, busyCheckBox.isSelected());
        filterChoices.put(FilterChoice.MISSING, missingCheckBox.isSelected());
    }

    private void fillTable(List<TableProduct> tableProducts) {
        productsTable.getItems().clear();

        for (TableProduct product : tableProducts) {
            if (isDateInPeriod(startDate.getValue(), endDate.getValue(), LocalDate.parse(product.getRegisteredOn()))) {
                productsTable.getItems().add(product);
            }
        }

        if (!productsTable.getItems().isEmpty())
            addTableContextMenu(productsTable);
    }

    private void addTableContextMenu(TableView<TableProduct> productsTable) {
        productsTable.setRowFactory(new Callback<TableView<TableProduct>, TableRow<TableProduct>>() {
            @Override
            public TableRow<TableProduct> call(TableView<TableProduct> tableView) {
                final TableRow<TableProduct> row = new TableRow<>();
                ContextMenu contextMenu = new ContextMenu();
                MenuItem checkProduct = new MenuItem("Check current product holder");
                setMenuItemAction(checkProduct);
                contextMenu.getItems().add(checkProduct);
                handleEmptyRows(row, contextMenu);

                return row;
            }
        });
    }

    private void handleEmptyRows(TableRow<TableProduct> row, ContextMenu contextMenu) {
        row.contextMenuProperty().bind(
                Bindings.when(row.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(contextMenu)
        );
    }

    private void setMenuItemAction(MenuItem checkProduct) {
        checkProduct.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    showCurrentHolderCard();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showCurrentHolderCard() throws IOException {
        TableProduct product = productsTable.getSelectionModel().getSelectedItem();
        if (product != null)
            if (!(product.getProductType().equals("TA")) && (product.getStatus().equals("Busy") || product.getStatus().equals("Missing"))) {
                String egn = referencesService.getProductHolderEgnByInvNumber(product.getInvNum());
                ClientCardReviewController.setEgn(egn);
                CustomDialog customDialog = new CustomDialog("ClientCardReview.fxml");
                customDialog.setTitle("Client card review");

                customDialog.showAndWait();
            } else
                ErrorDialog.callError("The product you have selected is not occupied by anyone or it is not LTTA.");
    }

    public boolean isDateInPeriod(LocalDate startDate, LocalDate endDate, LocalDate givenOn) {
        return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList()).contains(givenOn);
    }

    private boolean isFilterUsed() {
        for (boolean isChecked : filterChoices.values()) {
            if (isChecked)
                return true;
        }
        return false;
    }

    private void customizeTable() {
        setTableColumns();

        productsTable.getColumns().forEach(tableProductTableColumn -> {
            tableProductTableColumn.setResizable(false);
            tableProductTableColumn.setStyle("-fx-alignment: CENTER;");
        });
    }

    private void setTableColumns() {  //Mapping with TableProduct fields
        productColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        invNumberColumn.setCellValueFactory(new PropertyValueFactory<>("invNum"));
        productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productType"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        initialPriceColumn.setCellValueFactory(new PropertyValueFactory<>("initialPrice"));
        currentPriceColumn.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registeredOn"));
    }
}

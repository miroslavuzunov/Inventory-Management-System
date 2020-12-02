package ims.controllers.secondary;

import ims.App;
import ims.controllers.primary.SceneController;
import ims.controllers.resources.ReferencesControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.dialogs.ErrorDialog;
import ims.dialogs.InformationDialog;
import ims.enums.FilterChoice;
import ims.services.ReferencesService;
import ims.supporting.TableProduct;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
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
        initializeScenes();
        referencesService = new ReferencesService();
        filterChoices = new HashMap<>();

        endDate.setValue(LocalDate.now());  //Default period
        startDate.setValue(endDate.getValue().minusYears(1));

        customizeTable();
        updateFilterChoices();
        handleCheckedBoxes();
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

                checkProduct.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            getCurrentProductHolderEgn();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                contextMenu.getItems().add(checkProduct);

                row.contextMenuProperty().bind( // Prevents empty rows context menu
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu) null)
                                .otherwise(contextMenu)
                );
                return row;
            }
        });
    }

    private void getCurrentProductHolderEgn() throws IOException {
        TableProduct product = productsTable.getSelectionModel().getSelectedItem();
        if (product != null)
            if (!(product.getProductType().equals("TA")) && (product.getStatus().equals("Busy") || product.getStatus().equals("Missing"))) {
                String egn = referencesService.getProductHolderEgnByInvNumber(product.getInvNum());
                //InformationDialog.displayInformation("Current holder of this product is with EGN: " + egn + "\nYou can find him in the client's card section.");
                ClientCardController.setEgn(egn);
                Scene scene = new Scene(App.loadFXML("/view/ClientCard").load(),900,600);  //TODO REQUESTS AND DIALOG OPTIMIZATIONS
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } else
                ErrorDialog.callError("The product you have selected is not occupied by anyone or it is not LTTA.");
    }

    public boolean isDateInPeriod(LocalDate startDate, LocalDate endDate, LocalDate givenOn) {
        return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList()).contains(givenOn); // Checks if the transaction date is in the specified period (inclusive)
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

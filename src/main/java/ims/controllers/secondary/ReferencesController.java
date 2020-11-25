package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.controllers.resources.ReferencesControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.enums.FilterChoice;
import ims.services.ReferencesService;
import ims.supporting.TableProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ReferencesController extends ReferencesControllerResources implements Initializable {
    private ReferencesService referencesService;
    private Map<FilterChoice, Boolean> filterChoices;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();
        initializeScenes();
        referencesService = new ReferencesService();
        filterChoices = new HashMap<>();
        updateFilterChoices();
        customizeTable();

        endDate.setValue(LocalDate.now());  //Default period
        startDate.setValue(endDate.getValue().minusYears(1));
        selectDefaultBoxes();
        handleCheckedBoxes(new ActionEvent());
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
    private void handleCheckedBoxes(ActionEvent event) {
        updateFilterChoices();

        List<TableProduct> productsFromDb = referencesService.loadChecked(filterChoices);

        setTableColumns();
        fillTable(productsFromDb);
    }

    private void selectDefaultBoxes() {
        taCheckBox.setSelected(true);
        lttaCheckBox.setSelected(true);
        scrappedCheckBox.setSelected(true);
        availableCheckBox.setSelected(true);
        busyCheckBox.setSelected(true);
        missingCheckBox.setSelected(true);
    }

    private void setTableColumns() {  //Mapping with TableProduct fields
        productColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        invNumberColumn.setCellValueFactory(new PropertyValueFactory<>("invNum"));
        productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productType"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        initialPriceColumn.setCellValueFactory(new PropertyValueFactory<>("initialPrice"));
        currentPriceColumn.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
    }

    private void fillTable(List<TableProduct> tableProducts) {
        productsTable.getItems().clear();

        for (TableProduct product : tableProducts) {
            productsTable.getItems().add(product);
        }
    }

    private void customizeTable() {
        productsTable.getColumns().forEach(tableProductTableColumn -> {
            tableProductTableColumn.setResizable(false);
            tableProductTableColumn.setStyle("-fx-alignment: CENTER;");
        });
    }

}

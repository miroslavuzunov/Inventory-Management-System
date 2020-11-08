package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.controllers.resources.RegisterProductControllerResources;
import ims.dialogs.ConfirmationDialog;
import ims.enums.Criteria;
import ims.enums.PriceCurrency;
import ims.services.ProductRegistrationService;
import ims.supporting.CustomField;
import ims.supporting.ToggleGrouper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterProductController extends RegisterProductControllerResources implements Initializable {
    private ProductRegistrationService productRegistrationService;
    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productRegistrationService = new ProductRegistrationService();

        initializeScenes();
        initializeCurrencies();
        initializeScrappingCriteria();
        initializeDepreciationDegree();
        toggleGroup = ToggleGrouper.makeToggleGroup(List.of(lttaRadioBtn, taRadioBtn));
        manipulateLttaPanel();
    }

    @FXML
    private void handleClicks(ActionEvent event) throws IOException {
        if (event.getSource() == addProductBtn)
            System.out.println("test");
        if (event.getSource() == backBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Input data will be lost. Are you sure you want to get back?");

            if (result == ButtonType.YES)
                SceneController.switchSceneByButton((Button) event.getSource());
        } else
            SceneController.switchSceneByButton((Button) event.getSource());
    }

    @FXML
    private void manipulateLttaPanel() {
        if (toggleGroup.getSelectedToggle().equals(lttaRadioBtn))
            enableLttaOptions();
        if (toggleGroup.getSelectedToggle().equals(taRadioBtn))
            disableLttaOptions();
    }

    @FXML
    private void handleYearsOrMonthsField() {
        if (!scrappingCriteriaComboBox.getSelectionModel().isEmpty())
            if (scrappingCriteriaComboBox.getSelectionModel().getSelectedItem().equals(Criteria.CONDITION.toString()))
                yearsOrMonthsVBox.setDisable(true);
            else
                yearsOrMonthsVBox.setDisable(false);
    }

    private void initializeCurrencies() {
        for (PriceCurrency currency : PriceCurrency.values()) {
            priceUnitComboBox.getItems().add(currency.toString());
        }
    }

    private void initializeScrappingCriteria() {
        scrappingCriteriaComboBox.getItems().clear(); //Prevents data duplicating

        for (Criteria criteria : Criteria.values()) {
            scrappingCriteriaComboBox.getItems().add(criteria.toString());
        }
    }

    private void initializeDepreciationDegree() {
        depreciationDegreeComboBox.getItems().clear(); //Prevents data duplicating

        List<CustomField> degreesFromDb = productRegistrationService.initializeDepreciationDegree();

        degreesFromDb.forEach(degree -> {
            depreciationDegreeComboBox.getItems().add(degree.getFieldValue());
        });
    }

    private void enableLttaOptions() {
        lttaPanel.setDisable(false);
    }

    private void disableLttaOptions() {
        lttaPanel.setDisable(true);
    }

}

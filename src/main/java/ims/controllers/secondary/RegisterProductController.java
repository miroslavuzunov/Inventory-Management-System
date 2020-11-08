package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.controllers.resources.RegisterProductControllerResources;
import ims.dialogs.ConfirmationDialog;
import ims.enums.PriceCurrency;
import ims.services.ProductRegistrationService;
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

    private void initializeCurrencies() {
        for(PriceCurrency currency : PriceCurrency.values()){
            priceUnitComboBox.getItems().add(currency.toString());
        }
    }

    @FXML
    private void manipulateLttaPanel(){
        if(toggleGroup.getSelectedToggle().equals(lttaRadioBtn))
            enableLttaOptions();
        if(toggleGroup.getSelectedToggle().equals(taRadioBtn))
            disableLttaOptions();
    }

    private void enableLttaOptions(){
        scrappingCriteriaVBox.setDisable(false);
        yearsOrMonthsVBox.setDisable(false);
        depreciationDegreeVBox.setDisable(false);

        initializeScrappingCriteria();
    }

    private void disableLttaOptions(){
        scrappingCriteriaVBox.setDisable(true);
        yearsOrMonthsVBox.setDisable(true);
        depreciationDegreeVBox.setDisable(true);
    }

    private void initializeScrappingCriteria() {

    }
}

package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.controllers.resources.ClientCardControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.services.CardService;
import ims.supporting.CustomField;
import ims.supporting.TableProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ClientCardController extends ClientCardControllerResources implements Initializable {
    private CardService cardService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();
        cardService = new CardService();
        initializeScenes();
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
    private void searchByEgn(){
        boolean noEmptyFields = true;
        boolean noForbiddenChars = true;

        noEmptyFields = handleEmptyFields();
        //noForbiddenChars = handleForbiddenChars(inputFields);
        List<TableProduct> tableProducts = new ArrayList<>();
        StringBuilder clientsName = new StringBuilder();

        if(noEmptyFields && noForbiddenChars)
            tableProducts =  cardService.getClientsProductsByEgn(egnField.getText(), clientsName);

        clientName.setText(String.valueOf(clientsName));
        setTableColumns();
        fillTable(tableProducts);
    }

    @FXML
    public void addAnotherProductToCard(){

    }

    private void setTableColumns() {
        productColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        invNumberColumn.setCellValueFactory(new PropertyValueFactory<>("invNum"));
        givenByColumn.setCellValueFactory(new PropertyValueFactory<>("givenBy"));
        givenOnColumn.setCellValueFactory(new PropertyValueFactory<>("givenOn"));
    }

    private void fillTable(List<TableProduct> tableProducts) {
        clientsProductsTable.getItems().clear();
        for(TableProduct product : tableProducts){
            clientsProductsTable.getItems().add(product);
        }
    }

    @Override
    protected void displayMessages(Map<String, CustomField> fieldsByName) {
        egnMsg.setText(fieldsByName.get(EGN_FIELD_NAME).getMessage());
        egnField.setStyle(fieldsByName.get(EGN_FIELD_NAME).getStyle());
    }
}

package ims.controllers.secondary;

import ims.daos.AbstractDao;
import ims.services.ClientCardService;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientCardReviewController extends ClientCardController {
    private static String staticEgn = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();
        clientCardService = new ClientCardService();
        tableProducts = new ArrayList<>();

        customizeTable();
        endDate.setValue(LocalDate.now());  //Default period
        startDate.setValue(endDate.getValue().minusYears(1));

        if(!staticEgn.isEmpty()) {
            egnField.setText(staticEgn);
            try {
                searchByEgn();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        egnField.setEditable(false);
        startDate.setDisable(true);
        endDate.setDisable(true);
    }

    public static void setEgn(String egn){
        staticEgn = egn;
    }

    protected void setTableColumns() {  //Mapping with TableProduct fields
        productColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        invNumberColumn.setCellValueFactory(new PropertyValueFactory<>("invNum"));
        givenByColumn.setCellValueFactory(new PropertyValueFactory<>("givenBy"));
        givenOnColumn.setCellValueFactory(new PropertyValueFactory<>("givenOn"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @Override
    protected void handleButtonsStatus(boolean status) {

    }
}

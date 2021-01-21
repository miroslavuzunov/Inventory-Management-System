package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.controllers.resources.NotificationsControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.supporting.TableNotification;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class NotificationsController extends NotificationsControllerResources implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customizeTable();
        endDate.setValue(LocalDate.now());  //Default period
        startDate.setValue(endDate.getValue().minusWeeks(1));

        //loadNotifications();

        TableNotification tableNotification = new TableNotification();
        tableNotification.setNotificationMessage("Test1");
        tableNotification.setDateAndTime(LocalDateTime.now());
        TableNotification tableNotification2 = new TableNotification();
        tableNotification2.setNotificationMessage("Test2");
        tableNotification2.setDateAndTime(LocalDateTime.now());
        TableNotification tableNotification3 = new TableNotification();
        tableNotification3.setNotificationMessage("Test3");
        tableNotification3.setDateAndTime(LocalDateTime.now());

        notificationsTable.getItems().add(tableNotification);
        notificationsTable.getItems().add(tableNotification2);
        notificationsTable.getItems().add(tableNotification3);
    }

    public void handleClicks(ActionEvent event) {
        if (event.getSource() == backBtn) {
            ButtonType result = ConfirmationDialog.askForConfirmation("Input data will be lost. Are you sure you want to get back?");

            if (result == ButtonType.YES) {
                SceneController.getBack();
                AbstractDao.closeEntityManager();
            }
        }
    }

    public void handleDateChange(ActionEvent event) {
    }

    private void customizeTable() {
        setTableColumns();

        notificationsTable.getColumns().forEach(tableNotificationTableColumn -> {
            tableNotificationTableColumn.setResizable(false);
            tableNotificationTableColumn.setStyle("-fx-alignment: CENTER;");
        });
    }

    private void setTableColumns() {  //Mapping with TableNotification fields
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("notificationMessage"));
        dateAndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dateAndTime"));
    }

    public void refreshNotifications(ActionEvent event) {

    }
}

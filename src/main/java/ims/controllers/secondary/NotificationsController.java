package ims.controllers.secondary;

import ims.controllers.primary.SceneController;
import ims.controllers.resources.NotificationsControllerResources;
import ims.daos.AbstractDao;
import ims.dialogs.ConfirmationDialog;
import ims.entities.Notifications;
import ims.services.NotificationsService;
import ims.supporting.TableNotification;
import ims.supporting.TableProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NotificationsController extends NotificationsControllerResources implements Initializable {
    private NotificationsService notificationsService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AbstractDao.newEntityManager();

        notificationsService = new NotificationsService();

        setDefaultPeriod();
        customizeTable();
        loadNotifications();
    }

    private void loadNotifications() {
        fillTable(notificationsService.getAllNotifications());
    }

    private void fillTable(List<TableNotification> tableNotifications) {
        notificationsTable.getItems().clear();


        for (TableNotification notification : tableNotifications) {
            if (isDateInPeriod(startDate.getValue(), endDate.getValue(), getDateFromDateTimeString(notification.getDateAndTime()))){
                notificationsTable.getItems().add(notification);
            }
        }
    }

    private LocalDate getDateFromDateTimeString(String dateAndTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDate.from(LocalDateTime.parse(dateAndTime, formatter));
    }

    public boolean isDateInPeriod(LocalDate startDate, LocalDate endDate, LocalDate givenOn) {
        return startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList()).contains(givenOn);
    }

    private void setDefaultPeriod() {
        endDate.setValue(LocalDate.now());
        startDate.setValue(endDate.getValue().minusWeeks(1));
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

    @FXML
    public void handleDateChange(ActionEvent event) {
        loadNotifications();
    }

    @FXML
    public void refreshNotifications(ActionEvent event) {
        loadNotifications();
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

}

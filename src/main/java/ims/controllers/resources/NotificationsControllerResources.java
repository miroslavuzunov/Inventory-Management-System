package ims.controllers.resources;

import ims.supporting.TableNotification;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public abstract class NotificationsControllerResources {
    @FXML
    protected VBox cleanLeftPane;

    @FXML
    protected Button backBtn;

    @FXML
    protected AnchorPane regPane;

    @FXML
    protected DatePicker startDate;

    @FXML
    protected DatePicker endDate;

    @FXML
    protected TableView<TableNotification> notificationsTable;

    @FXML
    protected TableColumn<String, String> messageColumn;

    @FXML
    protected TableColumn<String, String> dateAndTimeColumn;
}

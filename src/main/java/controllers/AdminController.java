package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class AdminController {
    @FXML
    private VBox leftPane;

    @FXML
    private Button regMrtBtn;

    @FXML
    private Button genRefBtn;

    @FXML
    private Button stockRefBtn;

    @FXML
    private Button regProductBtn;

    @FXML
    private Button clientBtn;

    @FXML
    private Label userStatus;

    @FXML
    private Label todaysDate;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private void exit() throws IOException {
        App.setRoot("/view/LoginPanel");
    }
}

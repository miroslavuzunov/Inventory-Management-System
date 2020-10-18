package controllers;

import java.io.IOException;
import javafx.fxml.FXML;

public class AdminController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("/view/AdminPanel");
    }
}
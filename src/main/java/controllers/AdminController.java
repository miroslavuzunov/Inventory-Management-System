package controllers;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;

public class AdminController extends App {

    @FXML
    private void login() throws IOException {
        App.setRoot("/view/AdminPanel");
    }

    public void openGit(MouseEvent mouseEvent) {
        getHostServices().showDocument("https://github.com/miroslavuzunov/Inventory-Management-System");
    }
}
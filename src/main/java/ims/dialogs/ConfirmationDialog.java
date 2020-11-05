package ims.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class ConfirmationDialog {
    public static ButtonType askForConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("");
        alert.setTitle("Confirmation dialog");
        alert.showAndWait();

        return alert.getResult();
    }
}


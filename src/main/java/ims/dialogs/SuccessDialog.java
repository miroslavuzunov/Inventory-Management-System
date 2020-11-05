package ims.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class SuccessDialog {
        public static void success(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            alert.setHeaderText("");
            alert.setTitle("Success dialog");
            alert.showAndWait();
        }
}

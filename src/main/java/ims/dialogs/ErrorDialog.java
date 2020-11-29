package ims.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ErrorDialog {
        public static ButtonType callError(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.setHeaderText("");
            alert.setTitle("Error occurred!");
            alert.showAndWait();

            return alert.getResult();
        }
}

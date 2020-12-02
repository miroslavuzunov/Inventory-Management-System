package ims.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class InformationDialog {
        public static void displayInformation(String message) {
            TextArea textArea = new TextArea(message);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            GridPane gridPane = new GridPane();
            gridPane.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(textArea, 0, 0);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("");
            alert.setTitle("Information dialog");
            alert.getDialogPane().setContent(gridPane);
            alert.showAndWait();
        }
}

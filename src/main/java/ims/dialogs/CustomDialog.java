package ims.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.util.Optional;

public class CustomDialog {
    private final DialogPane dialogPane;
    private final Dialog<ButtonType> dialog;

    public CustomDialog(String fxmlFileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/" + fxmlFileName));
        dialogPane = fxmlLoader.load();
        dialog = new Dialog<>();
        dialog.setResizable(false);

        dialog.setDialogPane(dialogPane);
    }

    public void setTitle(String title) {
        dialog.setTitle(title);
    }

    public Optional<ButtonType> showAndWait() {
        return dialog.showAndWait();
    }
}

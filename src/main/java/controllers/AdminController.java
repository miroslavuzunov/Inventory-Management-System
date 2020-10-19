package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class AdminController {

    @FXML
    private Pane fadeMain;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private VBox genRefPane;

    @FXML
    private Button regMrtBtn;

    @FXML
    private VBox leftPane;

    @FXML
    private Button genRefBtn;

    @FXML
    private Button stockRefBtn;

    @FXML
    private Button regProductBtn;

    @FXML
    private Button clientBtn;

    @FXML
    private Label todaysDate;

    @FXML
    private Label userStatus;

    @FXML
    private void exit() throws IOException {
        App.setRoot("/view/LoginPanel");
    }

    @FXML
    private void handleClicks(ActionEvent event){
       if(event.getSource()==genRefBtn){
           mainPane.setDisable(true);
           fadeMain.toFront();
           genRefPane.toFront();
        }
    }

    public void backToLeftHome(ActionEvent event) {
        mainPane.setDisable(false);
        mainPane.toFront();
        leftPane.toFront();
    }
}

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
    private VBox clientManipPane;

    @FXML
    private Button regCardBtn;

    @FXML
    private Button addProductBtn;

    @FXML
    private Button removeProductBtn;

    @FXML
    private Button checkClientProductsBtn;

    @FXML
    private VBox genRefPane;

    @FXML
    private Button allProductsBtn;

    @FXML
    private Button checkStatusBtn;

    @FXML
    private Button productsByCategoryBtn;

    @FXML
    private Button scrappedProductsBtn;

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
    private Button clientManipBtn;

    @FXML
    private Button notificationBtn;

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
       else if(event.getSource()==clientManipBtn){
           mainPane.setDisable(true);
           fadeMain.toFront();
           clientManipPane.toFront();
       }
    }

    public void backToLeftHome(ActionEvent event) {
        mainPane.setDisable(false);
        mainPane.toFront();
        leftPane.toFront();
    }
}

package ims.controllers.primary;

import ims.controllers.resources.BaseUserControllerResources;
import ims.enums.MenuType;
import ims.supporting.TransitionAnimator;
import ims.supporting.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.time.LocalDate;

public abstract class BaseUserController extends BaseUserControllerResources implements Initializable {

    protected void displayUserInfo() {
        userStatus.setText("Status: " + UserSession.getLoggedUser().getRole());
        todaysDate.setText("Today's date: " + LocalDate.now().toString());
        nameLabel.setText("Hello, " + UserSession.getLoggedUser().getPersonInfo().getFirstName() + "!");
    }

    @FXML
    protected void openMenu() {
        leftPane.setPrefWidth(200);
        mainPane.toBack();
        showButtonText(MenuType.MAIN);
        TransitionAnimator.openTransition(leftPane);
    }

    @FXML
    protected void closeMenu() {
        leftPane.setPrefWidth(60);
        fadeMain.toBack();
        hideButtonText(MenuType.MAIN);
        TransitionAnimator.closeTransition(leftPane);
    }

    protected void showButtonText(MenuType menuType) {
        if (menuType.equals(MenuType.SUBMENU)) {
            clientManipPane.getChildren().forEach(node -> {
                node.setStyle("-fx-content-display: left;");
            });
        } else {
            leftPane.getChildren().forEach(node -> {
                node.setStyle("-fx-content-display: left;");
            });
        }
    }

    protected void hideButtonText(MenuType menuType) {
        if (menuType.equals(MenuType.SUBMENU)) {
            clientManipPane.getChildren().forEach(node -> {
                node.setStyle("-fx-content-display: graphic-only;");
            });
        } else {
            leftPane.getChildren().forEach(node -> {
                node.setStyle("-fx-content-display: graphic-only;");
            });
        }
    }
}

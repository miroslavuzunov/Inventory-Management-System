package ims.controllers.primary;

import ims.controllers.resources.CommonUserControllerResources;
import ims.enums.MenuType;
import ims.supporting.TransitionAnimator;
import javafx.fxml.FXML;

import java.time.LocalDate;

public abstract class CommonUserController extends CommonUserControllerResources {

    protected void displayUserInfo() {
        userStatus.setText("Status: " + LoginController.getLoggedUser().getRole());
        todaysDate.setText("Today's date: " + LocalDate.now().toString());
        nameLabel.setText("Hello, " + LoginController.getLoggedUser().getPersonInfo().getFirstName() + "!");
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

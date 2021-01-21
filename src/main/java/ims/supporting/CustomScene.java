package ims.supporting;

import ims.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class CustomScene {
    private String sceneName;
    private Scene scene;

    public CustomScene(String sceneName) {
        this.sceneName = sceneName;
    }

    public void loadScene() throws IOException {
        scene = new Scene(App.loadFXML("/view/" + sceneName).load(), 900, 600);
    }

    public Scene getScene() {
        return scene;
    }
}

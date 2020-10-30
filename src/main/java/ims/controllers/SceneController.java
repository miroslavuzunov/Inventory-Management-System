package ims.controllers;

import ims.App;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneController {
    private static final String FXML_DIRECTORY = "/view/";
    private static Map<Button,String> sceneNameByButton;

    public static void loadScenes(Map<Button,String> scenes){
        sceneNameByButton = new HashMap<>(scenes);
    }

    public static void switchSceneByButton(Button button) throws IOException {
        if(sceneNameByButton.containsKey(button))
            App.setScene(FXML_DIRECTORY + sceneNameByButton.get(button));
    }
}

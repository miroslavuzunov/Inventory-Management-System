package ims.controllers.primary;

import ims.App;
import ims.supporting.CustomScene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.*;

public class SceneController {
    private static Map<Button, CustomScene> sceneNameByButton;
    private static List<CustomScene> sceneQueue = new ArrayList<>();

    public static void initializeScenes(Map<Button, CustomScene> scenes) {
        sceneNameByButton = new HashMap<>(scenes);
    }

    public static void setInitialScene(CustomScene initialScene) throws IOException {
        sceneQueue.add(initialScene);
        setScene(initialScene);
    }

    public static void switchSceneByButton(Button button) throws IOException {
        if(!sceneNameByButton.isEmpty()) {
            if (sceneNameByButton.containsKey(button)) {
                setScene(sceneNameByButton.get(button));
                sceneQueue.add(sceneNameByButton.get(button));
            }
        }else
            System.out.println("empty");
    }

    public static void getBack(){
        if (!sceneQueue.isEmpty()) {
            sceneQueue.remove(sceneQueue.size() - 1);
            App.stage.setScene(sceneQueue.get(sceneQueue.size() - 1).getScene());
            App.stage.show();
        }
    }

    private static void setScene(CustomScene customScene) throws IOException {
        customScene.loadScene();
        App.stage.setScene(customScene.getScene());
        App.stage.show();
    }


}

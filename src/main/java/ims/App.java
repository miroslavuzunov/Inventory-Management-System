package ims;

import ims.controllers.primary.SceneController;
import ims.supporting.CustomScene;
import ims.supporting.EntityFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX ims.App
 */
public class App extends Application {
    private static Scene scene;
    public static Stage stage;

    @Override
    public void start(Stage newStage) throws IOException {
        stage = newStage;
        scene = new Scene(loadFXML("/view/Login").load(),900,600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("IMS - office edition");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/IMS(i).png")));
        stage.show();

        SceneController.setInitialScene(new CustomScene("Login"));
    }

    public static void setScene(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml).load());
    }

    public static FXMLLoader loadFXML(String fxml){
        return new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    }

    @Override
    public void stop(){
        EntityFactory.closeFactory();
    }

    public static void main(String[] args) {
          launch();
    }

}
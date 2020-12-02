package ims;

import ims.controllers.secondary.ReferencesController;
import ims.daos.AbstractDao;
import ims.supporting.EntityFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Queue;

/**
 * JavaFX ims.App
 */
public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/view/Login").load(),900,600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("IMS - office edition");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/IMS(i).png")));
        stage.show();
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
package ims;

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
        scene = new Scene(loadFXML("/view/Login"),900,600);
        //ArrayList<Scene>
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("IMS - office edition");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/IMS(i).png")));
        stage.show();
    }

    public static void setScene(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop(){
        EntityFactory.closeFactory();
    }

    public static void main(String[] args) {
          launch();
    }

}
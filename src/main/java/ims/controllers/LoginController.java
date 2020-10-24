package ims.controllers;
import java.io.IOException;

import ims.App;
import ims.entities.City;
import javafx.fxml.FXML;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class LoginController{

    @FXML
    private void login() throws IOException {
        App.setRoot("/view/AdminPanel");

        Configuration cfg = new Configuration().configure();

        SessionFactory sessionFactory = cfg.buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();


        session.getTransaction().commit();
        session.close();
    }

//    public void openGit(MouseEvent mouseEvent) {
//        getHostServices().showDocument("https://github.com/miroslavuzunov/Inventory-Management-System");
//    }

}
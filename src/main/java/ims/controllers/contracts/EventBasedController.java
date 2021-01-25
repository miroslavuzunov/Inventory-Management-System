package ims.controllers.contracts;

import javafx.event.ActionEvent;

import java.io.IOException;

public interface EventBasedController {
    void handleClicks(ActionEvent event) throws IOException;
}

package ims.controllers.primary;

import ims.controllers.secondary.RegisterUserController;
import ims.enums.Role;
import ims.supporting.CustomScene;
import javafx.event.ActionEvent;

import java.util.HashMap;

public class MrtController extends SystemUserController {
    @Override
    public void initializeScenes(){
        SceneController.initializeScenes(new HashMap<>() {{
            put(regCardBtn, new CustomScene("RegisterUser"));
            put(referencesBtn, new CustomScene("References"));
            put(notificationBtn, new CustomScene("Notifications"));
            put(forcedScrapBtn, new CustomScene(""));
            put(regProductBtn, new CustomScene("RegisterProduct"));
            put(checkClientProductsBtn, new CustomScene("ClientCard"));
        }});
    }

    @Override
    protected void handleRegistrationType(ActionEvent event) {
        if (event.getSource() == regCardBtn)
            RegisterUserController.passUserRegistrationRole(Role.CLIENT);
    }

}

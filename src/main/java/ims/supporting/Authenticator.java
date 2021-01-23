package ims.supporting;

import ims.entities.User;
import ims.services.UserService;

public class Authenticator {
    private UserService userService;
    private String infoMessage;
    private User loggedUser;

    public Authenticator() {
        userService = new UserService();
    }

    public boolean authenticateUser(String username, String password) throws NoSuchFieldException {
        loggedUser = userService.getUserByUsername(username);

        if (loggedUser == null) {
            infoMessage = "Invalid username! Try again!";
        } else {
            if (loggedUser.getPassword().equals(password)) {
                return true;
            }

            infoMessage = "Wrong password! Try again!";
        }

        return false;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}

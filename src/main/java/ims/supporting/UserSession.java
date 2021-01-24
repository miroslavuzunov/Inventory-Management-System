package ims.supporting;

import ims.entities.User;

public final class UserSession {
    private static UserSession instance;
    private static User loggedUser;

    private UserSession(User pLoggedUser) {
        loggedUser = pLoggedUser;
    }

    public static void createInstance(User loggedUser) {
        if (instance == null) {
            instance = new UserSession(loggedUser);
        }
    }

    public static User getLoggedUser(){
        return loggedUser;
    }

    public static void cleanUserSession(){
        loggedUser = null;
        instance = null;
    }
}

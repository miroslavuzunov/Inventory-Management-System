package ims.supporting;

import ims.controllers.contracts.Observer;
import ims.controllers.contracts.Subject;

import java.util.ArrayList;
import java.util.List;

public class Notifier implements Subject {
    private List<Notification> notifications;
    private List<Observer> observers;

    public Notifier() {
        notifications = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public void addNewNotification(Notification notification){
        notifications.add(notification);

        notifyObservers();
    }

    public void removeNotification(Notification notification){
        notifications.remove(notification);
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers){
            observer.update((ArrayList) notifications);
        }
    }
}

package ims.supporting;

import ims.controllers.contracts.Subject;

import java.time.LocalDateTime;

public class ProductStateTracker implements Runnable {
    private Subject notifier;

    public ProductStateTracker(Subject notifier) {
        this.notifier = notifier;
    }

    @Override
    public void run() {
        //for(Product product : AllProducts){
        productsScrap();

        productsPriceDecrease();

        productsTypeChange();
        //}
    }

    private void productsScrap() {
        ((Notifier)notifier).addNewNotification(new Notification("SCRAPPED!", LocalDateTime.now()));
    }

    private void productsPriceDecrease() {
        ((Notifier)notifier).addNewNotification(new Notification("PRICE DECREASED!", LocalDateTime.now()));
    }

    private void productsTypeChange() {
        ((Notifier)notifier).addNewNotification(new Notification("TYPE CHANGED!", LocalDateTime.now()));
    }
}
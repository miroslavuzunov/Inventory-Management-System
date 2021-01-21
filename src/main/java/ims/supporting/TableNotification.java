package ims.supporting;

import java.time.LocalDateTime;

public class TableNotification {
    private String notificationMessage;
    private String dateAndTime;

    public TableNotification() {
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}

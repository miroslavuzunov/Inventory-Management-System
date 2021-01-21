package ims.supporting;

import java.time.LocalDateTime;

public class TableNotification {
    private String notificationMessage;
    private LocalDateTime dateAndTime;

    public TableNotification() {
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}

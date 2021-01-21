package ims.supporting;


import java.time.LocalDateTime;

public class Notification {
    private String message;
    private LocalDateTime dateTime;

    public Notification(String message, LocalDateTime dateTime) {
        this.message = message;
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}

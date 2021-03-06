package ims.services;

import ims.daos.NotificationsDao;
import ims.entities.Notifications;
import ims.supporting.TableNotification;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsService {
    private final NotificationsDao notificationsDao;

    public NotificationsService() {
        notificationsDao = new NotificationsDao();
    }

    public List<TableNotification> getAllNotifications(){
        List<Notifications> allNotificationsFromDb = notificationsDao.getAll();
        List<TableNotification> tableNotifications = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Collections.reverse(allNotificationsFromDb); //Because of getting the latest notification at the top

        for(Notifications notifications : allNotificationsFromDb){
            TableNotification tableNotification = new TableNotification();

            tableNotification.setNotificationMessage(notifications.getMessage());
            tableNotification.setDateAndTime(notifications.getDateAndTime().format(formatter));

            tableNotifications.add(tableNotification);
        }

        return tableNotifications;
    }

}

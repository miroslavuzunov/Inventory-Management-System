package ims.daos;

import ims.entities.Notifications;
import ims.entities.Product;
import ims.supporting.Notification;

public class NotificationsDao extends AbstractDao<Notifications> {
    public NotificationsDao() {
        super(Notifications.class);
    }
}

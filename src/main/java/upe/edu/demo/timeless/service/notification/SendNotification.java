package upe.edu.demo.timeless.service.notification;

import upe.edu.demo.timeless.model.Usuario;

public interface SendNotification {
    void sendNotificationAllUser(String message);
    void sendNotificationUser(NotificationMessage message, Usuario user);


}

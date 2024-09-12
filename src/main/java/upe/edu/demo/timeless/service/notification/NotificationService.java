package upe.edu.demo.timeless.service.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.model.Usuario;

@Data
@ToString
@Service
@AllArgsConstructor
public class NotificationService implements SendNotification {

    @Override
    public void sendNotificationAllUser(String message) {

        //definir a lógica de envio de notificação para todos os usuários

    }

    @Override
    public void sendNotificationUser(NotificationMessage message, Usuario user) {

            //definir a lógica de envio de notificação para um usuário específico

    }
}

package upe.edu.demo.timeless.service.notification;



import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.model.Usuario;
import upe.edu.demo.timeless.shared.utils.enums.EmailTemplate;

import java.io.IOException;

@Data
@ToString
@Service
@AllArgsConstructor
@Slf4j
public class NotificationService implements SendNotification {

    @Override
    public void sendNotificationAllUser(String message) {

        //definir a lógica de envio de notificação para todos os usuários

    }

    @Override
    public void sendNotificationUser(NotificationMessage message, Usuario user)  {

        log.info("Enviando email para o usuario: {}", user.getCorreo());
        log.info("Mensagem: {}", message.getMessage());
        Resend resend = new Resend("re_YYUJF7AD_6mXvC698fzdsPQyY2Yb87obn");

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("notificaciones@time-less.online")
                .to(user.getCorreo())
                .subject(message.getSubject())
                .html(message.getMessage())
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            log.info(data.getId());

        } catch (ResendException e) {
            e.printStackTrace();
        }
}






}

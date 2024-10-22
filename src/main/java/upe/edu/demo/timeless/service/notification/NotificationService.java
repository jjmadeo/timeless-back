package upe.edu.demo.timeless.service.notification;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.model.Usuario;

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
        Email from = new Email("jjmadeo@gmail.com");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email(user.getCorreo());
        Content content = new Content("text/plain", "Aca va el mensaje de una notificacion. =>" + message.getMessage());
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.info("Email enviado com sucesso:Status{} Body: {}  Headers:{}", response.getStatusCode(), response.getBody(), response.getHeaders());


        } catch (Exception e) {
            log.error("Erro ao enviar email", e);

        }
    }
}

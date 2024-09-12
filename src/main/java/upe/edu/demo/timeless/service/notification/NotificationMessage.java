package upe.edu.demo.timeless.service.notification;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotificationMessage {

    private String message;
    private String template;

}

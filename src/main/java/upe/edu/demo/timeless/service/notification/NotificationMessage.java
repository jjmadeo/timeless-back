package upe.edu.demo.timeless.service.notification;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import upe.edu.demo.timeless.shared.utils.enums.EmailTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
public class NotificationMessage {

    private String subject;
    private Map<String,String> params;
    private String message;
    private String content;
    private EmailTemplate template;

    public NotificationMessage(EmailTemplate template, String subject, Map<String, String> params) {
        this.params = params;
        this.template = template;
        this.subject = subject == null ? template.getSubject() : subject;
        this.content = loadFileTemplate(template.getTemplateFile());
        log.info("Template:SIn rempazar {}", content);
        this.message = buildMessage();

    }


    private String loadFileTemplate(String templateFileName) {
        // Construir la ruta del archivo en la carpeta 'templates' dentro de 'resources'
        String filePath = "templates/" + templateFileName;

        // Usar el ClassLoader para cargar el archivo desde el classpath
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(filePath), StandardCharsets.UTF_8))) {

            // Leer todo el contenido del archivo y devolverlo como una cadena
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la plantilla: " + templateFileName, e);
        }
    }


    private String buildMessage() {
        String message = content;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            message = message.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        log.info("Template: Final{}", message);
        return message;
    }



}

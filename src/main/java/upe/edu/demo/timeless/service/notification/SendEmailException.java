package upe.edu.demo.timeless.service.notification;

import java.io.IOException;

public class SendEmailException extends Exception {
    public SendEmailException(String erroAoEnviarEmail, Exception e) {
        super(erroAoEnviarEmail, e);
    }
}


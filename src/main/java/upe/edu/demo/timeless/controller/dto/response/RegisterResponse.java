package upe.edu.demo.timeless.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.security.PrivateKey;

@Setter
@Getter
@Builder
@ToString
public class RegisterResponse {


    private String message;

     private Integer id;

    private Error error;
}

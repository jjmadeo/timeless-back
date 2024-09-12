package upe.edu.demo.timeless.controller.dto.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Data
@ToString
public class AuthRequest {
    private String correo;
    private String clave;

    // Getters y Setters
}
package upe.edu.demo.timeless.controller.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmpleadoRequest {


    private String nombre;

    private String apellido;

    private String turno;

    private String usuario;

    private String clave;





}

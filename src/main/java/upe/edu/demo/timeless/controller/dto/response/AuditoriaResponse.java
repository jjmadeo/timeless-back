package upe.edu.demo.timeless.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuditoriaResponse {

    private Integer idAuditoria;
    private String usuario;
    private String correo;
    private String telefonoUsuario;


    private LocalDateTime turno;


    private String empresa;
    private String direccion;
    private String telefonoEmpresa;
    private String canceledBy;

    private LocalDateTime fhEvent;
    private Error error;

}

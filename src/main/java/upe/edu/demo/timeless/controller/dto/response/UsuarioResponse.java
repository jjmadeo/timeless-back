package upe.edu.demo.timeless.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import upe.edu.demo.timeless.controller.dto.request.DatosPersonalesRequest;
import upe.edu.demo.timeless.controller.dto.request.DomicilioRequest;
import upe.edu.demo.timeless.model.ConfigUsuarioGeneral;

import java.util.Date;
@Data
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UsuarioResponse {

    private Long id;
    private String correo;

    private Date fhCreacion;

    private boolean activo;
    private String tipoUsuario;

    private DatosPersonalesResponse datosPersonales;

    private DomicilioResponse domicilio;

    private ConfigUsuarioGeneralResponse configUsuarioGeneral;


}

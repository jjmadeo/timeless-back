package upe.edu.demo.timeless.controller.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DatosPersonalesRequest {


    private String nombre;
    private String apellido;

    private String numeroDocumento;
    private String tipoDocumento;
    private String telefonoCelular;

    private String fNacimiento;

}

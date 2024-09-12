package upe.edu.demo.timeless.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DatosPersonalesResponse {
    private String nombre;
    private String apellido;

    private String numeroDocumento;
    private String tipoDocumento;
    private String telefonoCelular;

    private String fNacimiento;
}

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
public class DomicilioResponse {
    private String calle;

    private String numero;

    private String ciudad;

    private String localidad;

    private String provincia;

    private String pais;
}

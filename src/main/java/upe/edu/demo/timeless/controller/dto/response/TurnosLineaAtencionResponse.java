package upe.edu.demo.timeless.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TurnosLineaAtencionResponse {
    private List<TurnosResponse> hoy;
    private List<TurnosResponse> futuros;
    private List<TurnosResponse> pasados;

    private Error error;
}

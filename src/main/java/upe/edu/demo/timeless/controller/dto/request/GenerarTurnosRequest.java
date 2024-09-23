package upe.edu.demo.timeless.controller.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class GenerarTurnosRequest {


    private Integer idlineaAtencion;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
}

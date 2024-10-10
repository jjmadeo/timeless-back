package upe.edu.demo.timeless.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import upe.edu.demo.timeless.model.Rubro;

import java.util.List;

@Data
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TurnosResponseUser {



    List<TurnosResponse> hoy;
    List<TurnosResponse> futuros;
    List<TurnosResponse> pasados;



    private Error error;

}

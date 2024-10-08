package upe.edu.demo.timeless.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import upe.edu.demo.timeless.model.Rubro;

@Data
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TurnosResponse {
    private Long id;

    private String hashid;
    private String mensaje;
    private String fechaHora;
    private String direccion;
    private Integer duracion;
    private Rubro rubro;
    private String nombreEmpresa;

    private Cordenadas cordenadas;

    private UsuarioTurnoOwner usuarioTurnoOwner;
    private Error error;

}

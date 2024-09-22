package upe.edu.demo.timeless.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import upe.edu.demo.timeless.controller.dto.request.Calendario;
import upe.edu.demo.timeless.controller.dto.request.DatosFiscales;
import upe.edu.demo.timeless.controller.dto.request.LineaAtencion;
import upe.edu.demo.timeless.controller.dto.request.ParametroEmpresa;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EmpresaResponse {

    private Long id;

    private String fhCreacion;

    private UsuarioResponse dueñoEmpresa;

    private DatosFiscales datosFiscales;

    private List<ParametroEmpresa> parametros;

    private Calendario calendario;

    private List<LineaAtencion> lineasAtencion;

    private String rubro;

    private Error error;


}

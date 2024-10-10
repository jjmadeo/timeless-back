package upe.edu.demo.timeless.controller.dto.request;

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
public class ModificarEmpresaRequest {



    private DatosFiscales datosFiscales; 

    private Calendario calendario;

    private List<LineaAtencion> lineasAtencion;



}

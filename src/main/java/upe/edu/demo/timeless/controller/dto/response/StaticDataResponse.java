package upe.edu.demo.timeless.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import upe.edu.demo.timeless.model.*;

import java.util.List;
@Data
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StaticDataResponse {



    private List<Rubro> rubro;
    private List<TipoDocumento> tipoDocumentos;
    private List<TipoUsuario> tipoUsuarios;
    private List<ConfigSistema> configsGlobal;
}

package upe.edu.demo.timeless.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
@Builder
public class EmpleadosResponse {

    private List<EmpleadoResponse> empleados;
}

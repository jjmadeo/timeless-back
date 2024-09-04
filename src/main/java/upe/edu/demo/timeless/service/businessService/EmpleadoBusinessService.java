package upe.edu.demo.timeless.service.businessService;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.controller.dto.request.EmpleadoRequest;
import upe.edu.demo.timeless.controller.dto.response.EmpleadoResponse;
import upe.edu.demo.timeless.controller.dto.response.EmpleadosResponse;
import upe.edu.demo.timeless.model.Empleado;
import upe.edu.demo.timeless.service.EmpleadoService;


import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmpleadoBusinessService {

    private final EmpleadoService empleadoService;




    public EmpleadoResponse getEmpleado(Integer id){

        return mapToResponse(empleadoService.findEmpleadoById(id));
    }
    public EmpleadosResponse getEmpleados(){

        List<Empleado> empleados = empleadoService.findAll();


        List<EmpleadoResponse> empleadosResponse = empleados.stream().map(this::mapToResponse).collect(Collectors.toList());

        return EmpleadosResponse.builder().empleados(empleadosResponse).build();
    }


    public EmpleadoResponse crearEmpleado( EmpleadoRequest empleado){

        return mapToResponse(empleadoService.saveEmpleado(mapToEmpleado(empleado)));

    }




    public Integer BorrarEmpleado(Integer id){
        return empleadoService.deleteEmpleado(id);
    }


    //mapper de Empleado to EmpleadoResponse;
    private EmpleadoResponse mapToResponse(Empleado empleado) {
        return EmpleadoResponse.builder()

                .nombre(empleado.getNombre())
                .apellido(empleado.getApellido())
                .turno(empleado.getTurno())
                .usuario(empleado.getUsuario())
                .roleId(empleado.getRoleId())
                .token(empleado.getToken())
                .id(empleado.getId())
                .build();
    }

    private Empleado mapToEmpleado(EmpleadoRequest empleado) {
        Empleado emp = new Empleado();
        emp.setNombre(empleado.getNombre());
        emp.setApellido(empleado.getApellido());
        emp.setTurno(empleado.getTurno());
        emp.setUsuario(empleado.getUsuario());
        emp.setClave(empleado.getClave());
        emp.setRoleId(1);
        return emp;

    }
}

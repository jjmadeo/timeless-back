package upe.edu.demo.timeless.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upe.edu.demo.timeless.controller.dto.request.EmpleadoRequest;
import upe.edu.demo.timeless.controller.dto.response.EmpleadoResponse;
import upe.edu.demo.timeless.controller.dto.response.EmpleadosResponse;
import upe.edu.demo.timeless.service.businessService.EmpleadoBusinessService;


@RestController
@RequestMapping("v1/timeless/")
@AllArgsConstructor

@Tag(name = "Tutorial", description = "Tutorial management APIs")
public class EmpleadoController {


    private final EmpleadoBusinessService empleadoService;



    @GetMapping(value = "/empleados")
    public ResponseEntity<EmpleadosResponse> ObtenerEmpleados() {
        return new ResponseEntity<>(empleadoService.getEmpleados(), HttpStatus.OK);
    }

    @GetMapping(value = "/empleado/{id}")
    public ResponseEntity<EmpleadoResponse> ObtenerEmpleado(@PathVariable  Integer id) {
        return new ResponseEntity<>(empleadoService.getEmpleado(id), HttpStatus.OK);
    }

    @PostMapping(value = "/empleado")
    public ResponseEntity<EmpleadoResponse> CrearEmpleado(@RequestBody EmpleadoRequest empleado) {
        return new ResponseEntity<>(empleadoService.crearEmpleado(empleado), HttpStatus.CREATED);
    }






    @DeleteMapping(value = "/empleado/{id}")
    public ResponseEntity<String> BorrarEmpleado( @PathVariable  Integer id) {
        return new ResponseEntity<>("Empleado borrado numero:" + empleadoService.BorrarEmpleado(id), HttpStatus.OK);

    }
}

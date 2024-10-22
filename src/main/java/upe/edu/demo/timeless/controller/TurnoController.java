package upe.edu.demo.timeless.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upe.edu.demo.timeless.controller.dto.request.CrearEmpresaRequest;
import upe.edu.demo.timeless.controller.dto.request.GenerarTurnosRequest;
import upe.edu.demo.timeless.controller.dto.response.*;
import upe.edu.demo.timeless.service.EmpresaService;
import upe.edu.demo.timeless.service.TurnosService;

@RestController
@AllArgsConstructor
@RequestMapping("v1/timeless/")
@Slf4j
public class TurnoController {
    
    private final TurnosService turnosService;
    
    

    @GetMapping("/turnos")
    public ResponseEntity<MultiEntityResponse<TurnosResponse>> getturnos(){
        return turnosService.getTurnosDisponibles();
    }

    @GetMapping("/turnosEmpresa/{id}")
    public ResponseEntity<MultiEntityResponse<TurnosResponse>> getTurnosEmpresa(@PathVariable("id") Long id, @Param("fecha") String fecha){
        return turnosService.getTurnosDisponiblesEmpresa(id,fecha);
    }

    @GetMapping("/turnosLineaAtencion/{id}")
    public ResponseEntity<MultiEntityResponse<TurnosResponse>> getTurnosLinea(@PathVariable("id") Integer id, @Param("fecha") String fecha){
        return turnosService.getTurnosDisponiblesLineaAtencion(id,fecha);
    }

    @GetMapping("/turnos/lineaAtencion/{id}")
    public ResponseEntity<TurnosLineaAtencionResponse> getTurnosLineaByid(@PathVariable("id") Long id){
        return turnosService.getTurnosLineaAtencion(id);
    }

    // Actualizar un usuario existente
  /* @PutMapping("/empresa/{id}")
    public ResponseEntity<GenericResponse<UsuarioResponse>> updateUser(@PathVariable Long id, @RequestBody CrearEmpresaRequest user) {

        return empresaService.updateEmpresa(id, user);
    }*/

    @PostMapping("/GenerarTurnos")
    public ResponseEntity<GenerarTurnosResponse> crearEmpresaProcess(@RequestBody GenerarTurnosRequest generarTurnosRequest) {

        return turnosService.generarTurnos(generarTurnosRequest);

    }

    @PostMapping("/preselccionarTurno/{hashid}")
    public ResponseEntity<PreseleccionarTurnoResponse> preseleccionarTurno(@PathVariable String hashid) {

        return turnosService.preseleccionarTurno(hashid);

    }

    @PostMapping("/ConfirmarTurno/{hashid}")
    public ResponseEntity<ConfirmacionTurnoResponse> confirmarTurno(@PathVariable String hashid) {

        return turnosService.confirmarTurno(hashid);

    }

    @PostMapping("/CancelpreselccionarTurno/{hashid}")
    public ResponseEntity<ConfirmacionTurnoResponse> CancelarPreseleccionTurno(@PathVariable String hashid) {

        return turnosService.cacelarPreseleccion(hashid);

    }

    @PostMapping("/CancelarTurno/{hashid}")
    public ResponseEntity<CancelarTurnoResponse> cancelarTurno(@PathVariable String hashid) {

        return turnosService.cancelarTurno(hashid);

    }

    @GetMapping("/turnosByUser")
    public ResponseEntity<TurnosResponseUser> getturnosByUser(){
        return turnosService.getTurnosDisponiblesUser();
    }

    @GetMapping("/VisualizarTurnosDisponibles/{id}")
    public ResponseEntity<MultiEntityResponse<TurnosResponse>> getTurnosLineaAtencion(@PathVariable("id") Integer id, @RequestParam("desde") String fechadesde, @RequestParam("hasta") String fechahasta){
        return turnosService.getTurnosDisponiblesLineaAtencionAndFecha(id,fechadesde,fechahasta);
    }




}

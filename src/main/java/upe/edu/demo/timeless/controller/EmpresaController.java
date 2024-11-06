package upe.edu.demo.timeless.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upe.edu.demo.timeless.controller.dto.request.Ausencia;
import upe.edu.demo.timeless.controller.dto.request.CrearEmpresaRequest;
import upe.edu.demo.timeless.controller.dto.request.ModificarEmpresaRequest;
import upe.edu.demo.timeless.controller.dto.response.*;
import upe.edu.demo.timeless.service.EmpresaService;

@RestController
@AllArgsConstructor
@RequestMapping("v1/timeless/")
@Slf4j
public class EmpresaController {
    
    private final EmpresaService empresaService;
    
    

    @GetMapping("/empresas")
    public ResponseEntity<MultiEntityResponse<EmpresaResponse>> getUsuarios(){
        return empresaService.getAllEmpresas();
    }



   @GetMapping("/empresa/{id}")
    public ResponseEntity<EmpresaResponse> getUsuarioById(@PathVariable Long id) {
       return empresaService.getEmpresaById(id);
   }
    // Actualizar un usuario existente
  /* @PutMapping("/empresa/{id}")
    public ResponseEntity<GenericResponse<UsuarioResponse>> updateUser(@PathVariable Long id, @RequestBody CrearEmpresaRequest user) {

        return empresaService.updateEmpresa(id, user);
    }*/

    @PostMapping("/empresa")
    public ResponseEntity<CrearEmpresaResponse> crearEmpresaProcess(@RequestBody CrearEmpresaRequest empresaRequest) {

        return empresaService.crearEmpresaProcess(empresaRequest);

    }


   @GetMapping("/empresasByLocation")
    public ResponseEntity<MultiEntityResponse<EmpresaResponse>> getEmpresasByLocation(@Param("lon") String lon, @Param("lat") String lat, @Param("distance") String distance,@Param("rubro") String rubro) {
        return empresaService.getEmpresasByLocation(lon, lat, distance, rubro);
    }


    @PutMapping("/empresa/{id}")
    public ResponseEntity<CrearEmpresaResponse> updateEmpresa(@PathVariable Long id, @RequestBody ModificarEmpresaRequest empresaRequest) {
        return empresaService.updateEmpresa(id, empresaRequest);
    }


    @DeleteMapping("/ausencia/{id}")
    public ResponseEntity<String> EliminarAusencia(@PathVariable Long id) {
        return empresaService.eliminarAusencia(id);
    }

    @PostMapping("/ausencia/{id}")
    public ResponseEntity<AusenciaResponse> crearAusencia(@PathVariable Long id , @RequestBody Ausencia ausencia) {
        return empresaService.crearAusencia(id,ausencia);
    }




}

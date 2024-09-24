package upe.edu.demo.timeless.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upe.edu.demo.timeless.controller.dto.request.CrearEmpresaRequest;
import upe.edu.demo.timeless.controller.dto.request.UsuarioRequest;
import upe.edu.demo.timeless.controller.dto.response.*;
import upe.edu.demo.timeless.service.EmpresaService;
import upe.edu.demo.timeless.service.UsuarioService;

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

    @GetMapping("/prueba")
    public ResponseEntity<?> gtest(){
        return empresaService.test();
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

   /* // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    */
   @GetMapping("/empresasByLocation/{id}")
    public ResponseEntity<MultiEntityResponse<EmpresaResponse>> getEmpresasByLocation(@PathVariable("id") Integer idEmpresa,@Param("lon") String lon, @Param("lat") String lat, @Param("distance") String distance) {
        return empresaService.getEmpresasByLocation(idEmpresa, lon, lat, distance);
    }

}

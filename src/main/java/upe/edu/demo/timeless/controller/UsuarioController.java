package upe.edu.demo.timeless.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upe.edu.demo.timeless.controller.dto.request.RegisterRequest;
import upe.edu.demo.timeless.controller.dto.request.UsuarioRequest;
import upe.edu.demo.timeless.controller.dto.response.GenericResponse;
import upe.edu.demo.timeless.controller.dto.response.MultiEntityResponse;
import upe.edu.demo.timeless.controller.dto.response.UsuarioResponse;
import upe.edu.demo.timeless.service.UsuarioService;

@RestController
@AllArgsConstructor
@RequestMapping("v1/timeless/")
@Slf4j
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    
    
    @GetMapping("/usuarios")
    public ResponseEntity<MultiEntityResponse<UsuarioResponse>> getUsuarios(){
        return usuarioService.getAllUsuarios();
    }
    
    @GetMapping("/usuario/{id}")
    public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id);
    }

    // Actualizar un usuario existente
    @PutMapping("/usuario/{id}")
    public ResponseEntity<GenericResponse<UsuarioResponse>> updateUser(@PathVariable Long id, @RequestBody UsuarioRequest user) {

        return usuarioService.updateUser(id, user);
    }

   /* // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    */
    

}

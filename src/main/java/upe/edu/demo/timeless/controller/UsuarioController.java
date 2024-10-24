package upe.edu.demo.timeless.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import upe.edu.demo.timeless.controller.dto.request.ChangePassword;
import upe.edu.demo.timeless.controller.dto.request.RegisterRequest;
import upe.edu.demo.timeless.controller.dto.request.UsuarioRequest;
import upe.edu.demo.timeless.controller.dto.response.GenericResponse;
import upe.edu.demo.timeless.controller.dto.response.MultiEntityResponse;
import upe.edu.demo.timeless.controller.dto.response.PreseleccionarTurnoResponse;
import upe.edu.demo.timeless.controller.dto.response.UsuarioResponse;
import upe.edu.demo.timeless.model.Usuario;
import upe.edu.demo.timeless.repository.UsuarioRepository;
import upe.edu.demo.timeless.service.UsuarioService;
import upe.edu.demo.timeless.service.notification.NotificationMessage;
import upe.edu.demo.timeless.service.notification.NotificationService;
import upe.edu.demo.timeless.shared.utils.Utils;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("v1/timeless/")
@Slf4j
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final NotificationService notificationService;
    private PasswordEncoder passwordEncoder;
    
    
    @GetMapping("/usuarios")
    public ResponseEntity<MultiEntityResponse<UsuarioResponse>> getUsuarios(){
        return usuarioService.getAllUsuarios();
    }
    
    @GetMapping("/usuario/{id}")
    public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id);
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponse> getUserProfile() {
        return usuarioService.getProfile();
    }


    // Actualizar un usuario existente
    @PutMapping("/usuario/{id}")
    public ResponseEntity<GenericResponse<UsuarioResponse>> updateUser(@PathVariable Long id, @RequestBody UsuarioRequest user) {

        return usuarioService.updateUser(id, user);
    }


    @GetMapping("/UsuarioByTurno/{hashid}")
    public ResponseEntity<UsuarioResponse> preseleccionarTurno(@PathVariable String hashid) {

        return usuarioService.obtenerUsuarioPorTurnoOtorgado(hashid);

    }


    @PostMapping("/resetPassword/")
    public ResponseEntity<String> resetPassword(@Param("email") String email) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(email);

        if (usuario.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        String nuevaClave = Utils.generateRandomString(6);

        usuario.get().setClave(passwordEncoder.encode(nuevaClave));

        usuarioRepository.save(usuario.get());

        notificationService.sendNotificationUser(NotificationMessage.builder().message("Clave reseteada,Su nueva clave es: " + nuevaClave+"\n Recuerde cambiar la password una vez inicado sesion.").build(),usuario.get());

        return ResponseEntity.ok("Clave reseteada, se envio por email. para que pueda iniciar sesion y cambiarla.");


    }


    @PostMapping("/changePassword/")
    public ResponseEntity<String> changePassword(@RequestBody ChangePassword changePassword) {

        String email = Utils.getUserEmail();

        Optional<Usuario> usuario = usuarioRepository.findByCorreo(email);

        if (usuario.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        if (!passwordEncoder.matches(changePassword.getOldPassword(),usuario.get().getClave())) {
            return ResponseEntity.badRequest().body("Clave incorrecta");
        }

        if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Error en la confirmacion de claves.");
        }

        usuario.get().setClave(passwordEncoder.encode(changePassword.getNewPassword()));

        usuarioRepository.save(usuario.get());

        notificationService.sendNotificationUser(NotificationMessage.builder().message("Te avisamos que tu cambio de clave fue une exito").build(),usuario.get());

        return ResponseEntity.ok("Clave cambiada con exito");

    }







    // Eliminar un usuario por ID
    @DeleteMapping("/bajaUsuario")
    public ResponseEntity<GenericResponse<String>> deleteUser() {

        return  usuarioService.deleteUser();
    }


}

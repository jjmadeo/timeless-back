package upe.edu.demo.timeless.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import upe.edu.demo.timeless.controller.dto.request.AuthRequest;
import upe.edu.demo.timeless.model.Usuario;
import upe.edu.demo.timeless.service.UsuarioService;
import upe.edu.demo.timeless.shared.utils.JwtUtil;

@RestController
@AllArgsConstructor
@RequestMapping("v1/timeless/")
public class AuthController {


    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    private UserDetailsService userDetailsService;
    private UsuarioService usuarioService;
    private PasswordEncoder passwordEncoder;



    @PostMapping("/authenticate")
    public String createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @PostMapping("/refresh-token")
    public String refreshToken(@RequestBody AuthRequest authRequest) throws Exception {
        // Aquí podrías implementar la lógica para refrescar el token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        return jwtUtil.generateToken(userDetails.getUsername());
    }


    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody AuthRequest authRequest) throws Exception {

        Usuario user = new Usuario();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setApellido("PedroGomez");
        user.setNombre("PedroPedro");
        user.setEmail("asd@gmail.com");

        usuarioService.save(user);

        // Aquí podrías implementar la lógica para registrar un usuario
        return ResponseEntity.ok("Usuario registrado");
    }

}
package upe.edu.demo.timeless.controller;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import upe.edu.demo.timeless.controller.dto.request.AuthRequest;
import upe.edu.demo.timeless.controller.dto.request.RegisterRequest;
import upe.edu.demo.timeless.controller.dto.response.Error;
import upe.edu.demo.timeless.controller.dto.response.LoginResponse;
import upe.edu.demo.timeless.controller.dto.response.RegisterResponse;
import upe.edu.demo.timeless.service.UsuarioService;
import upe.edu.demo.timeless.shared.utils.JwtUtil;

@RestController
@AllArgsConstructor
@RequestMapping("v1/timeless/")
@Slf4j
public class AuthController {


    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    private UserDetailsService userDetailsService;
    private UsuarioService usuarioService;
    private PasswordEncoder passwordEncoder;




    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {

        try {

            if  (authRequest.getCorreo().isEmpty() || authRequest.getClave().isEmpty()){

                return ResponseEntity.badRequest().body(LoginResponse.builder().error(Error.builder().code("403").status(HttpStatus.BAD_REQUEST).title("Debe completar Correo y Clave").build()).build());
            }


            log.info("Autenticando usuario {}",authRequest.toString());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getCorreo(), authRequest.getClave())
            );


            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getCorreo());

            return ResponseEntity.ok(LoginResponse.builder().token(jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities().toString(),null)).build());

        }catch (Exception e){
            log.error("Error al autenticar usuario {}",e.getMessage());
            return ResponseEntity.badRequest().body(LoginResponse.builder().error(Error.builder().code("403").status(HttpStatus.BAD_REQUEST).title("Correo o Clave incorrectos").build()).build());
        }



    }

  /*  @PostMapping("/refresh-token")
    public String refreshToken(@RequestBody AuthRequest authRequest) throws Exception {
        // Aquí podrías implementar la lógica para refrescar el token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getCorreo());
        return jwtUtil.generateToken(userDetails.getUsername(),userDetails);
    }*/


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> Register(@RequestBody RegisterRequest registerRequest) throws Exception {

        registerRequest.setClave(passwordEncoder.encode(registerRequest.getClave()));

        return usuarioService.registrarUsuario(registerRequest);
    }

}
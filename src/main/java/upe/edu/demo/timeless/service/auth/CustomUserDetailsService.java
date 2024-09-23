package upe.edu.demo.timeless.service.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import upe.edu.demo.timeless.model.Usuario;
import upe.edu.demo.timeless.service.UsuarioService;

import java.util.Collections;

@Component
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {


    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioService.findByUsername(username);


        User user = (User) User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getClave())
                .disabled(!usuario.isHabilitado())
                .roles(usuario.getTipoUsuario().getDetalle())
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .build();

            log.info("Usuario autenticado: {}", user);
        return user;

    }


}

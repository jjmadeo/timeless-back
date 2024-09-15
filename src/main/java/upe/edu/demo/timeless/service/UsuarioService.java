package upe.edu.demo.timeless.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.controller.dto.request.RegisterRequest;
import upe.edu.demo.timeless.controller.dto.request.UsuarioRequest;
import upe.edu.demo.timeless.controller.dto.response.*;
import upe.edu.demo.timeless.model.*;
import upe.edu.demo.timeless.repository.TipoDocumentoRepository;
import upe.edu.demo.timeless.repository.TipoUsuarioRepository;
import upe.edu.demo.timeless.repository.UsuarioRepository;
import upe.edu.demo.timeless.shared.utils.enums.TipoDniEnum;
import upe.edu.demo.timeless.shared.utils.enums.TipoUsuarioEnum;

import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public Usuario findByUsername(String correo) {
        return usuarioRepository.findByCorreo(correo).orElseThrow();
    }

    public ResponseEntity<RegisterResponse> registrarUsuario(RegisterRequest registerRequest) {
        log.info("Creando usuario");

        Usuario userCreated = null;
        if (usuarioRepository.existsByCorreo(registerRequest.getCorreo())) {
            return ResponseEntity.badRequest().body(RegisterResponse.builder().message("El correo ya se encuentra registrado").build());
        }


        try {


            TipoUsuario tipoUsuario = tipoUsuarioRepository.findByDetalle(String.valueOf(TipoUsuarioEnum.GENERAL)).orElseThrow();
            TipoDocumento tipoDocumento = tipoDocumentoRepository.findByDetalle(registerRequest.getDatosPersonales().getTipoDocumento().toUpperCase()).orElseThrow();


            Usuario usuario = Usuario.builder()
                    .clave(registerRequest.getClave())
                    .correo(registerRequest.getCorreo())
                    .habilitado(true)
                    .datosPersonales(DatosPersonales.builder()
                            .apellido(registerRequest.getDatosPersonales().getApellido())
                            .nombre(registerRequest.getDatosPersonales().getNombre())
                            .fNacimiento(registerRequest.getDatosPersonales().getFNacimiento())
                            .numeroDocumento(registerRequest.getDatosPersonales().getNumeroDocumento())
                            .tipoDocumento(tipoDocumento)
                            .telefonoCelular(registerRequest.getDatosPersonales().getTelefonoCelular())
                            .domicilio(Domicilio.builder()
                                    .calle(registerRequest.getDomicilio().getCalle())
                                    .ciudad(registerRequest.getDomicilio().getCiudad())
                                    .numero(registerRequest.getDomicilio().getNumero())
                                    .pais(registerRequest.getDomicilio().getPais())
                                    .localidad(registerRequest.getDomicilio().getLocalidad())
                                    .provincia(registerRequest.getDomicilio().getProvincia())
                                    .build())
                            .build())
                    .tipoUsuario(tipoUsuario)
                    .configUsuarioGeneral(ConfigUsuarioGeneral.builder()
                            .email(true)
                            .wpp(true)
                            .sms(true)
                            .build())
                    .build();

            userCreated = usuarioRepository.save(usuario);

            return ResponseEntity.ok(RegisterResponse.builder().message("Usuario creado con exito").id(userCreated.getId()).build());





        } catch (Exception e) {
            log.error("Error al crear usuario", e);
            return ResponseEntity.badRequest().body(RegisterResponse.builder().message("Error al crear usuario").build());
        }
    }



    public ResponseEntity<MultiEntityResponse<UsuarioResponse>> getAllUsuarios() {

        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();

        return ResponseEntity.ok(
                MultiEntityResponse.<UsuarioResponse>builder().data(
                        usuarios.stream().map(this::mapToUserResponse).toList()).build()
        );








    }




    public UsuarioResponse mapToUserResponse(Usuario usuario) {

        return UsuarioResponse.builder()
                .id((long) usuario.getId())
                .correo(usuario.getCorreo())
                .activo(usuario.isHabilitado())
                .tipoUsuario(usuario.getTipoUsuario().getDetalle())
                .fhCreacion(usuario.getFhCreacion())
                .datosPersonales(DatosPersonalesResponse.builder()
                        .nombre(usuario.getDatosPersonales().getNombre())
                        .apellido(usuario.getDatosPersonales().getApellido())
                        .fNacimiento(usuario.getDatosPersonales().getFNacimiento())
                        .numeroDocumento(usuario.getDatosPersonales().getNumeroDocumento())
                        .tipoDocumento(String.valueOf(TipoDniEnum.valueOf(usuario.getDatosPersonales().getTipoDocumento().getDetalle())))
                        .telefonoCelular(usuario.getDatosPersonales().getTelefonoCelular())
                        .build())
                .domicilio(DomicilioResponse.builder()
                        .calle(usuario.getDatosPersonales().getDomicilio().getCalle())
                        .ciudad(usuario.getDatosPersonales().getDomicilio().getCiudad())
                        .numero(usuario.getDatosPersonales().getDomicilio().getNumero())
                        .pais(usuario.getDatosPersonales().getDomicilio().getPais())
                        .localidad(usuario.getDatosPersonales().getDomicilio().getLocalidad())
                        .provincia(usuario.getDatosPersonales().getDomicilio().getProvincia())
                        .build())
                .configUsuarioGeneral(ConfigUsuarioGeneralResponse.builder()
                        .email(usuario.getConfigUsuarioGeneral().isEmail())
                        .sms(usuario.getConfigUsuarioGeneral().isSms())
                        .wpp(usuario.getConfigUsuarioGeneral().isWpp())
                        .build())
                .build();



    }


    public ResponseEntity<UsuarioResponse> getUsuarioById(Long id) {


        Usuario usuario = usuarioRepository.findById(Math.toIntExact(id)).orElseThrow();

        return ResponseEntity.ok(mapToUserResponse(usuario));


    }


    public ResponseEntity<GenericResponse<UsuarioResponse>> updateUser(Long id, UsuarioRequest user) {

        log.info("{}", user);

        Usuario usuario = usuarioRepository.findById(Math.toIntExact(id)).orElseThrow();

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findByDetalle(user.getTipoUsuario()).orElseThrow(()-> new RuntimeException("Tipo de usuario no encontrado"));
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findByDetalle(user.getDatosPersonales().getTipoDocumento().toUpperCase()).orElseThrow(()-> new RuntimeException("Tipo de Documento no encontrado"));

        usuario.setTipoUsuario(tipoUsuario);

        usuario.getDatosPersonales().setNombre(user.getDatosPersonales().getNombre());
        usuario.getDatosPersonales().setApellido(user.getDatosPersonales().getApellido());
        usuario.getDatosPersonales().setFNacimiento(user.getDatosPersonales().getFNacimiento());
        usuario.getDatosPersonales().setNumeroDocumento(user.getDatosPersonales().getNumeroDocumento());
        usuario.getDatosPersonales().setTipoDocumento(tipoDocumento);
        usuario.getDatosPersonales().setTelefonoCelular(user.getDatosPersonales().getTelefonoCelular());

        usuario.getDatosPersonales().getDomicilio().setCalle(user.getDomicilio().getCalle());
        usuario.getDatosPersonales().getDomicilio().setCiudad(user.getDomicilio().getCiudad());
        usuario.getDatosPersonales().getDomicilio().setNumero(user.getDomicilio().getNumero());
        usuario.getDatosPersonales().getDomicilio().setPais(user.getDomicilio().getPais());
        usuario.getDatosPersonales().getDomicilio().setLocalidad(user.getDomicilio().getLocalidad());
        usuario.getDatosPersonales().getDomicilio().setProvincia(user.getDomicilio().getProvincia());

        usuario.getConfigUsuarioGeneral().setEmail(user.getConfigUsuarioGeneral().isEmail());
        usuario.getConfigUsuarioGeneral().setSms(user.getConfigUsuarioGeneral().isSms());
        usuario.getConfigUsuarioGeneral().setWpp(user.getConfigUsuarioGeneral().isWpp());



        usuarioRepository.save(usuario);






        return null;
    }
}

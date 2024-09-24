package upe.edu.demo.timeless.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.controller.dto.request.LocalityRequest;
import upe.edu.demo.timeless.controller.dto.response.Error;
import upe.edu.demo.timeless.controller.dto.response.GenericResponse;
import upe.edu.demo.timeless.model.Domicilio;
import upe.edu.demo.timeless.model.DomicilioFiscal;
import upe.edu.demo.timeless.model.Empresa;
import upe.edu.demo.timeless.model.Usuario;
import upe.edu.demo.timeless.repository.EmpresaRepository;
import upe.edu.demo.timeless.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class GeoLocalityService {




    private  final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;


    public ResponseEntity<GenericResponse<String>> addLocalityuser(LocalityRequest localityRequest) {
        log.info("addLocalityuser: {}", localityRequest);
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(localityRequest.getId());

            if (!usuario.isPresent()) {
                return ResponseEntity.ok(GenericResponse.<String>builder().error(Error.builder().code("400").title("Usuario no encontrado").status(HttpStatus.BAD_REQUEST).build()).build());
            }

            if(localityRequest.getTipo().equalsIgnoreCase("empresa")){

                Optional<Empresa> empresa = Optional.ofNullable(usuario.get().getEmpresas().get(0));
                if (!empresa.isPresent()) {
                    return ResponseEntity.ok(GenericResponse.<String>builder().error(Error.builder().code("400").title("Este Usuario no tiene una empresa.").status(HttpStatus.BAD_REQUEST).build()).build());
                }


                DomicilioFiscal domicilioFiscal = usuario.get().getEmpresas().get(0).getDatosFiscales().getDomicilioFiscal();
                domicilioFiscal.setLatitud(BigDecimal.valueOf(Double.parseDouble(localityRequest.getLatitud())));
                domicilioFiscal.setLongitud(BigDecimal.valueOf(Double.parseDouble(localityRequest.getLongitud())));

                usuario.get().getEmpresas().get(0).getDatosFiscales().setDomicilioFiscal(domicilioFiscal);
                usuarioRepository.save(usuario.get());

            }else if(localityRequest.getTipo().equalsIgnoreCase("usuario")){

                Domicilio domicilio= usuario.get().getDatosPersonales().getDomicilio();
                domicilio.setLatitud(BigDecimal.valueOf(Double.parseDouble(localityRequest.getLatitud())));
                domicilio.setLongitud(BigDecimal.valueOf(Double.parseDouble(localityRequest.getLongitud())));

                usuario.get().getDatosPersonales().setDomicilio(domicilio);
                usuarioRepository.save(usuario.get());
            }

            return ResponseEntity.ok(GenericResponse.<String>builder().data("Usuario actualizado").build());
        } catch (Exception e) {
            log.error("Error al actualizar usuario", e);

            return ResponseEntity.ok(GenericResponse.<String>builder().error(Error.builder().code("500").title("Error al actualizar usuario").status(HttpStatus.INTERNAL_SERVER_ERROR).build()).build());

        }
    }


}

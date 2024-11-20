package upe.edu.demo.timeless.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.controller.dto.response.AuditoriaResponse;
import upe.edu.demo.timeless.controller.dto.response.ConfirmacionTurnoResponse;
import upe.edu.demo.timeless.controller.dto.response.Error;
import upe.edu.demo.timeless.controller.dto.response.GenericResponse;
import upe.edu.demo.timeless.model.Auditoria;
import upe.edu.demo.timeless.model.DomicilioFiscal;
import upe.edu.demo.timeless.model.Usuario;
import upe.edu.demo.timeless.repository.AuditoriaRepository;
import upe.edu.demo.timeless.repository.UsuarioRepository;
import upe.edu.demo.timeless.shared.utils.Utils;
import upe.edu.demo.timeless.shared.utils.enums.TipoUsuarioEnum;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class AuditoriaService {

    private final UsuarioRepository usuarioRepository;
    private final AuditoriaRepository auditoriaRepository;





    public ResponseEntity<GenericResponse<List<AuditoriaResponse>>> auditoria(String canceledBy, Long id) {

        log.info("AuditoriaService.auditoria");

        //usuario autenticado

        String userEmail = Utils.getUserEmail();
        Optional<Usuario> usuario = usuarioRepository.findById(Math.toIntExact(id));

        if (Utils.getFirstAuthority().contains(TipoUsuarioEnum.GENERAL.name())){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(GenericResponse.<List<AuditoriaResponse>>builder()
                    .error(Error.builder()
                            .status(HttpStatus.FORBIDDEN)
                            .code("403")
                            .title("Forbidden")
                            .details(List.of("No tienes permisos para acceder a este recurso"))
                            .build())
                    .build());


      }




        List<Auditoria> auditorias = auditoriaRepository.findAllByUsuario(usuario.get().getId());

        log.info("Auditorias: "+auditorias.size());
        log.info("Auditorias: "+auditorias);
        if(canceledBy != null && !canceledBy.isEmpty()){
            auditorias = auditorias.stream().filter(auditoria -> auditoria.getCanceledBy().equals(canceledBy)).toList();
        }


        List<AuditoriaResponse> auditoriaResponses = auditorias.stream().map(auditoria -> {

            Optional<Usuario> usuarioEmpresa = usuarioRepository.findById(auditoria.getUsuarioEmpresa());


            DomicilioFiscal domicilioFiscal = usuarioEmpresa.get().getEmpresas().get(0).getDatosFiscales().getDomicilioFiscal();
            String direccion = domicilioFiscal.getCalle() + " " + domicilioFiscal.getNumero() + ",  " + domicilioFiscal.getCiudad() + ", " + domicilioFiscal.getLocalidad();



            return AuditoriaResponse.builder()
                    .idAuditoria(auditoria.getId())
                    .canceledBy(auditoria.getCanceledBy())
                    .direccion(direccion)
                    .empresa(usuarioEmpresa.get().getEmpresas().get(0).getDatosFiscales().getNombreFantasia())
                    .fhEvent(auditoria.getFhEvent())
                    .turno(auditoria.getFhTurno())
                    .usuario(usuario.get().getDatosPersonales().getNombre()+" "+usuario.get().getDatosPersonales().getApellido())
                    .telefonoEmpresa(usuarioEmpresa.get().getDatosPersonales().getTelefonoCelular())
                    .telefonoUsuario(usuario.get().getDatosPersonales().getTelefonoCelular())
                    .correo(usuario.get().getCorreo())
                    .build();
        }).toList();



        return ResponseEntity.ok(GenericResponse.<List<AuditoriaResponse>>builder()
                        .data(auditoriaResponses)
                        .build());



    }






    public ResponseEntity<GenericResponse<List<AuditoriaResponse>>> auditoriaEmpresa(String canceledBy, Long id) {

        //usuario autenticado

        String userEmail = Utils.getUserEmail();
        Optional<Usuario> usuario = usuarioRepository.findById(Math.toIntExact(id));

        if (Utils.getFirstAuthority().contains(TipoUsuarioEnum.GENERAL.name())){

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(GenericResponse.<List<AuditoriaResponse>>builder()
                    .error(Error.builder()
                            .status(HttpStatus.FORBIDDEN)
                            .code("403")
                            .title("Forbidden")
                            .details(List.of("No tienes permisos para acceder a este recurso"))
                            .build())
                    .build());


        }


        List<Auditoria> auditorias = auditoriaRepository.findAllByUsuarioEmpresa(usuario.get().getId());
        log.info("Auditorias: "+auditorias.size());
        log.info("Auditorias: "+auditorias);
        if(canceledBy != null && !canceledBy.isEmpty()){
            auditorias = auditorias.stream().filter(auditoria -> auditoria.getCanceledBy().equals(canceledBy)).toList();
        }


        List<AuditoriaResponse> auditoriaResponses = auditorias.stream().map(auditoria -> {

            Optional<Usuario> usuarioTurno = usuarioRepository.findById(auditoria.getUsuario());


            DomicilioFiscal domicilioFiscal = usuario.get().getEmpresas().get(0).getDatosFiscales().getDomicilioFiscal();
            String direccion = domicilioFiscal.getCalle() + " " + domicilioFiscal.getNumero() + ",  " + domicilioFiscal.getCiudad() + ", " + domicilioFiscal.getLocalidad();



            return AuditoriaResponse.builder()
                    .idAuditoria(auditoria.getId())
                    .canceledBy(auditoria.getCanceledBy())
                    .direccion(direccion)
                    .empresa(usuario.get().getEmpresas().get(0).getDatosFiscales().getNombreFantasia())
                    .usuarioEmpresa(usuario.get().getDatosPersonales().getNombre()+" "+usuario.get().getDatosPersonales().getApellido())
                    .fhEvent(auditoria.getFhEvent())
                    .dni(usuarioTurno.get().getDatosPersonales().getNumeroDocumento())
                    .turno(auditoria.getFhTurno())
                    .usuario(usuarioTurno.get().getDatosPersonales().getNombre()+" "+usuarioTurno.get().getDatosPersonales().getApellido())
                    .telefonoEmpresa(usuario.get().getDatosPersonales().getTelefonoCelular())
                    .telefonoUsuario(usuarioTurno.get().getDatosPersonales().getTelefonoCelular())
                    .correo(usuarioTurno.get().getCorreo())
                    .build();
        }).toList();



        return ResponseEntity.ok(GenericResponse.<List<AuditoriaResponse>>builder()
                .data(auditoriaResponses)
                .build());



    }
}

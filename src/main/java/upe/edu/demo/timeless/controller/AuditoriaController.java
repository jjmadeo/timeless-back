package upe.edu.demo.timeless.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upe.edu.demo.timeless.controller.dto.response.AuditoriaResponse;
import upe.edu.demo.timeless.controller.dto.response.GenericResponse;
import upe.edu.demo.timeless.model.Usuario;
import upe.edu.demo.timeless.service.AuditoriaService;
import upe.edu.demo.timeless.service.notification.NotificationMessage;
import upe.edu.demo.timeless.shared.utils.Utils;
import upe.edu.demo.timeless.shared.utils.enums.EmailTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("v1/timeless/")
@Slf4j
public class AuditoriaController {

    private final AuditoriaService auditoriaService;



    @GetMapping("/Audit/{id}")
    public ResponseEntity<GenericResponse<List<AuditoriaResponse>>> auditoria(@Param("canceledBy") String canceledBy, @PathVariable("id") Long id) {


        return auditoriaService.auditoria(canceledBy, id);
    }
    @GetMapping("/AuditEmpresa/{id}")
    public ResponseEntity<GenericResponse<List<AuditoriaResponse>>> auditoriaEmpresa(@Param("canceledBy") String canceledBy, @PathVariable("id") Long id) {


        return auditoriaService.auditoriaEmpresa(canceledBy, id);
    }

}

package upe.edu.demo.timeless.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upe.edu.demo.timeless.controller.dto.request.LocalityRequest;
import upe.edu.demo.timeless.controller.dto.request.UsuarioRequest;
import upe.edu.demo.timeless.controller.dto.response.GenericResponse;
import upe.edu.demo.timeless.controller.dto.response.MultiEntityResponse;
import upe.edu.demo.timeless.controller.dto.response.UsuarioResponse;
import upe.edu.demo.timeless.service.GeoLocalityService;
import upe.edu.demo.timeless.service.UsuarioService;

@RestController
@AllArgsConstructor
@RequestMapping("v1/timeless/")
@Slf4j
public class GeoLocalityController {
    
    private final GeoLocalityService geoLocalityService;





    // Actualizar un usuario existente
    @PostMapping("/locality")
    public ResponseEntity<GenericResponse<String>> addLocalityuser( @RequestBody LocalityRequest localityRequest) {

        return geoLocalityService.addLocalityuser(localityRequest);
    }





}
package upe.edu.demo.timeless.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.controller.dto.request.RegisterRequest;
import upe.edu.demo.timeless.controller.dto.request.UsuarioRequest;
import upe.edu.demo.timeless.controller.dto.response.Error;
import upe.edu.demo.timeless.controller.dto.response.*;
import upe.edu.demo.timeless.model.*;
import upe.edu.demo.timeless.repository.RubroRepository;
import upe.edu.demo.timeless.repository.TipoDocumentoRepository;
import upe.edu.demo.timeless.repository.TipoUsuarioRepository;
import upe.edu.demo.timeless.repository.UsuarioRepository;
import upe.edu.demo.timeless.shared.utils.Utils;
import upe.edu.demo.timeless.shared.utils.enums.TipoDniEnum;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StaticDataService {


    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final RubroRepository rubroRepository;

    public ResponseEntity<StaticDataResponse> getStaticData() {

        List<TipoUsuario> tipoUsuarios = (List<TipoUsuario>) tipoUsuarioRepository.findAll();
        List<TipoDocumento> tipoDocumentos = (List<TipoDocumento>) tipoDocumentoRepository.findAll();
        List<Rubro> rubros = (List<Rubro>) rubroRepository.findAll();


        return ResponseEntity.ok(StaticDataResponse.builder()
                .rubro(rubros)
                .tipoDocumentos(tipoDocumentos)
                .tipoUsuarios(tipoUsuarios)
                .build());
    }
}

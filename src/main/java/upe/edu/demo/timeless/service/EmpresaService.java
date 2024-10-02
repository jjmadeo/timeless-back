package upe.edu.demo.timeless.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.controller.dto.request.Ausencia;
import upe.edu.demo.timeless.controller.dto.request.CrearEmpresaRequest;
import upe.edu.demo.timeless.controller.dto.request.ParametroEmpresa;
import upe.edu.demo.timeless.controller.dto.response.*;
import upe.edu.demo.timeless.controller.dto.response.Error;
import upe.edu.demo.timeless.model.*;
import upe.edu.demo.timeless.repository.*;
import upe.edu.demo.timeless.shared.utils.Utils;
import upe.edu.demo.timeless.shared.utils.enums.MembresiaEnum;
import upe.edu.demo.timeless.shared.utils.enums.TipoUsuarioEnum;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final DatosFiscalesRepository datosFiscalesRepository;
    private final UsuarioRepository usuarioRepository;
    private final MembresiaRepository membresiaRepository;
    private final RubroRepository rubroRepository;


    public ResponseEntity<CrearEmpresaResponse> crearEmpresaProcess(CrearEmpresaRequest empresaRequest) {

        log.info("REQUEST Empresa: {}", empresaRequest);

        //obtener autorities del usuario autenticado x token

        String rolUser = Utils.getFirstAuthority();

        if (rolUser != null && !rolUser.contains(TipoUsuarioEnum.EMPRESA.name())) {
            log.error("No tiene permisos para crear una Empresa.");
            return ResponseEntity.badRequest().body(CrearEmpresaResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("No tiene permisos para crear una Empresa. Solo los usuarios tipo Empresa pueden hacerlo").code("400").build()).build());
        }


        try {

            if (datosFiscalesRepository.findByCuit(empresaRequest.getDatosFiscales().getCuit()).isPresent()) {
                log.error("La Empresa ya existe.");
                return ResponseEntity.badRequest().body(CrearEmpresaResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("La Empresa ya existe.").code("400").build()).build());
            }

            Optional<Usuario> usuario = usuarioRepository.findByCorreo(Utils.getUserEmail());


            if (usuario.isEmpty()) {
                log.error("El Usuario no existe.");
                return ResponseEntity.badRequest().body(CrearEmpresaResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("El Usuario no existe.").code("400").build()).build());
            }

            if (usuario.get().getEmpresas().size() > 0) {
                log.error("El Usuario ya tiene una Empresa.");
                return ResponseEntity.badRequest().body(CrearEmpresaResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("El Usuario ya tiene una Empresa.").code("400").build()).build());
            }

            log.info("Usuario: {}", usuario.get());

            Membresia membresia = membresiaRepository.findByDetalle(String.valueOf(MembresiaEnum.FREE));
            List<Rubro> rubro = (List<Rubro>) rubroRepository.findAll();
            log.info("Rubro: {}", rubro);


            Empresa empresa = Empresa.builder()
                    .usuario(usuario.get())
                    .membresia(membresia)
                    .datosFiscales(DatosFiscales.builder()
                            .cuit(empresaRequest.getDatosFiscales().getCuit())
                            .razonSocial(empresaRequest.getDatosFiscales().getRazonSocial())
                            .nombreFantasia(empresaRequest.getDatosFiscales().getNombreFantasia())
                            .domicilioFiscal(DomicilioFiscal.builder()
                                    .calle(empresaRequest.getDatosFiscales().getDomicilioFiscal().getCalle())
                                    .numero(empresaRequest.getDatosFiscales().getDomicilioFiscal().getNumero())
                                    .localidad(empresaRequest.getDatosFiscales().getDomicilioFiscal().getLocalidad())
                                    .provincia(empresaRequest.getDatosFiscales().getDomicilioFiscal().getProvincia())
                                    .pais(empresaRequest.getDatosFiscales().getDomicilioFiscal().getPais())
                                    .ciudad(empresaRequest.getDatosFiscales().getDomicilioFiscal().getCiudad())
                                    .build())
                            .build())
                    .calendario(Calendario.builder()
                            .hInicio(Time.valueOf(empresaRequest.getCalendario().getHoraApertura()))
                            .hFin(Time.valueOf(empresaRequest.getCalendario().getHoraCierre()))
                            .listaDiasLaborables(empresaRequest.getCalendario().getDiasLaborales())
                            .build())
                    .rubro(rubro.stream().filter(r -> r.getDetalle().equals(empresaRequest.getRubro())).findFirst().get())

                    .build();


            empresaRequest.getCalendario().getAusencias().forEach(ausencia -> {
                empresa.getCalendario().addAusencias(Ausencias.builder()
                        .desde(Utils.convertStringToTimestampDate(ausencia.getDesde()))
                        .hasta(Utils.convertStringToTimestampDate(ausencia.getHasta()))
                        .descripcion(ausencia.getDescripcion())
                        .build());
            });


            empresaRequest.getLineasAtencion().forEach(linea -> {
              LineaAtencion lineaAtencion =  LineaAtencion.builder()
                        .descripccion(linea.getDescripcion())
                      .habilitado(true)

                        .duracionTurno(Integer.parseInt(linea.getDuracionTurnos()))
                        .build();

              lineaAtencion.addAgenda(Agenda.builder().build());


                empresa.addLineaAtencion(lineaAtencion);

            });


            empresaRequest.getParametros().forEach(parametro -> {

                empresa.addParametro(ParametrizacionEmpresa.builder()
                        .detalle(parametro.getDescripcion())
                        .clave(parametro.getClave())
                        .valor(parametro.getValor())
                        .build());
            });


            log.info("Empresa a Guardar: {}", empresa);

            return ResponseEntity.ok(CrearEmpresaResponse.builder().id(empresaRepository.save(empresa).getId()).build());

        } catch (Exception e) {
            log.error("Error al crear la Empresa: {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(CrearEmpresaResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Error al crear la Empresa.").code("400").build()).build());
        }

    }



    public Agenda buildAgenda(LineaAtencion lineaAtencion) {
        return Agenda.builder()
                .lineaAtencion(lineaAtencion)
                .build();
    }

    public ResponseEntity<EmpresaResponse> getEmpresaById(Long id) {


        try {

           Optional<Empresa> empresa = empresaRepository.findById(Math.toIntExact(id));

            if (empresa.isEmpty()) {
                log.error("La Empresa no existe.");
                return ResponseEntity.badRequest().body(EmpresaResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("La Empresa no existe.").code("400").build()).build());
            }

            return ResponseEntity.ok(mapEmpresaToResponse(empresa.get()));

        } catch (NoSuchElementException e) {
            log.error("Error al obtener la Empresa: {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(EmpresaResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Error al obtener la Empresa.").code("400").build()).build());
        }
    }

    private EmpresaResponse mapEmpresaToResponse(Empresa empresa) {
        return EmpresaResponse.builder()
                .id((long) empresa.getId())
                .fhCreacion(empresa.getFhCreacion().toString())
                .dueñoEmpresa(UsuarioResponse.builder()
                        .id((long) empresa.getUsuario().getId())
                        .tipoUsuario(empresa.getUsuario().getTipoUsuario().getDetalle())
                        .activo(empresa.getUsuario().isHabilitado())
                        .fhCreacion(empresa.getUsuario().getFhCreacion())
                        .correo(empresa.getUsuario().getCorreo())
                        .datosPersonales(DatosPersonalesResponse.builder()
                                .nombre(empresa.getUsuario().getDatosPersonales().getNombre())
                                .apellido(empresa.getUsuario().getDatosPersonales().getApellido())
                                .telefonoCelular(empresa.getUsuario().getDatosPersonales().getTelefonoCelular())
                                .tipoDocumento(empresa.getUsuario().getDatosPersonales().getTipoDocumento().getDetalle())
                                .numeroDocumento(empresa.getUsuario().getDatosPersonales().getNumeroDocumento())
                                .fNacimiento(empresa.getUsuario().getDatosPersonales().getFNacimiento())
                                .build())
                        .domicilio(DomicilioResponse.builder()
                                .calle(empresa.getUsuario().getDatosPersonales().getDomicilio().getCalle())
                                .numero(empresa.getUsuario().getDatosPersonales().getDomicilio().getNumero())
                                .localidad(empresa.getUsuario().getDatosPersonales().getDomicilio().getLocalidad())
                                .provincia(empresa.getUsuario().getDatosPersonales().getDomicilio().getProvincia())
                                .pais(empresa.getUsuario().getDatosPersonales().getDomicilio().getPais())
                                .ciudad(empresa.getUsuario().getDatosPersonales().getDomicilio().getCiudad())
                                .latitud(empresa.getUsuario().getDatosPersonales().getDomicilio().getLatitud())
                                .longitud(empresa.getUsuario().getDatosPersonales().getDomicilio().getLongitud())
                                .build())
                        .configUsuarioGeneral(ConfigUsuarioGeneralResponse.builder()
                                .email(empresa.getUsuario().getConfigUsuarioGeneral().isEmail())
                                .sms(empresa.getUsuario().getConfigUsuarioGeneral().isSms())
                                .wpp(empresa.getUsuario().getConfigUsuarioGeneral().isWpp())
                                .build())
                        .build())
                .datosFiscales(upe.edu.demo.timeless.controller.dto.request.DatosFiscales.builder()
                        .cuit(empresa.getDatosFiscales().getCuit())
                        .razonSocial(empresa.getDatosFiscales().getRazonSocial())
                        .nombreFantasia(empresa.getDatosFiscales().getNombreFantasia())
                        .domicilioFiscal(upe.edu.demo.timeless.controller.dto.request.DomicilioFiscal.builder()
                                .calle(empresa.getDatosFiscales().getDomicilioFiscal().getCalle())
                                .numero(empresa.getDatosFiscales().getDomicilioFiscal().getNumero())
                                .localidad(empresa.getDatosFiscales().getDomicilioFiscal().getLocalidad())
                                .provincia(empresa.getDatosFiscales().getDomicilioFiscal().getProvincia())
                                .pais(empresa.getDatosFiscales().getDomicilioFiscal().getPais())
                                .ciudad(empresa.getDatosFiscales().getDomicilioFiscal().getCiudad())
                                .latitud(empresa.getDatosFiscales().getDomicilioFiscal().getLatitud())
                                .longitud(empresa.getDatosFiscales().getDomicilioFiscal().getLongitud())
                                .build())
                        .build())
                .parametros(mapParametros(empresa.getParametrizaciones()))
                .calendario(upe.edu.demo.timeless.controller.dto.request.Calendario.builder()
                        .horaApertura(String.valueOf(empresa.getCalendario().getHInicio().toLocalTime()))
                        .horaCierre(String.valueOf(empresa.getCalendario().getHFin().toLocalTime()))
                        .diasLaborales(empresa.getCalendario().getListaDiasLaborables())
                        .ausencias(mapAusencias(empresa.getCalendario().getAusencias()))
                        .build())
                .lineasAtencion(mapLineasAtencion(empresa.getLineaAtencion()))
                .rubro(empresa.getRubro().getDetalle())
                .build();
    }

    public List<ParametroEmpresa> mapParametros(List<ParametrizacionEmpresa> parametros) {
        List<ParametroEmpresa> parametrosEmpresa = new ArrayList<>();
        parametros.forEach(parametro -> {
            parametrosEmpresa.add(ParametroEmpresa.builder()
                    .clave(parametro.getClave())
                    .descripcion(parametro.getDetalle())
                    .valor(parametro.getValor())
                    .build());
        });
        return parametrosEmpresa;
    }

    public List<Ausencia> mapAusencias(List<Ausencias> ausencias) {
        List<Ausencia> ausenciasEmpresa = new ArrayList<>();
        ausencias.forEach(ausencia -> {
            ausenciasEmpresa.add(Ausencia.builder()
                    .descripcion(ausencia.getDescripcion())
                    .desde(ausencia.getDesde().toString())
                    .hasta(ausencia.getHasta().toString())
                    .build());
        });
        return ausenciasEmpresa;
    }

    public List<upe.edu.demo.timeless.controller.dto.request.LineaAtencion> mapLineasAtencion(List<LineaAtencion> lineasAtencion) {
        List<upe.edu.demo.timeless.controller.dto.request.LineaAtencion> lineasAtencionEmpresa = new ArrayList<>();
        lineasAtencion.forEach(linea -> {
            lineasAtencionEmpresa.add(upe.edu.demo.timeless.controller.dto.request.LineaAtencion.builder()
                    .id((long) linea.getId())
                    .descripcion(linea.getDescripccion())

                    .duracionTurnos(String.valueOf(linea.getDuracionTurno()))
                    .build());
        });


        return lineasAtencionEmpresa;
    }


    public ResponseEntity<MultiEntityResponse<EmpresaResponse>> getAllEmpresas() {

        try {

            List<Empresa> empresas = (List<Empresa>) empresaRepository.findAll();

            List<EmpresaResponse> empresasResponse = new ArrayList<>();

            empresas.forEach(empresa -> {
                empresasResponse.add(mapEmpresaToResponse(empresa));
            });

            return ResponseEntity.ok(MultiEntityResponse.<EmpresaResponse>builder().data(empresasResponse).build());

        } catch (Exception e) {
            log.error("Error al obtener las Empresas: {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(MultiEntityResponse.<EmpresaResponse>builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Error al obtener las Empresas.").code("400").build()).build());
        }
    }




    public ResponseEntity<GenericResponse<UsuarioResponse>> updateEmpresa(Long id, CrearEmpresaRequest user) {

        // TODO Auto-generated method stub

        return null;

    }

    public ResponseEntity<MultiEntityResponse<EmpresaResponse>> getEmpresasByLocation( String lon, String lat, String distance, String rubro) {
        log.info("Data: {}, {}, {},{}", lon, lat, distance, rubro);

        List<Empresa> empresas = (List<Empresa>) empresaRepository.findAll();


        if (rubro != null && !rubro.isEmpty()) {
            empresas = empresas.stream().filter(empresa -> empresa.getRubro().getDetalle().equalsIgnoreCase(rubro)).toList();
        }

        List <Empresa> empresasInRadius = new ArrayList<>();

        empresas.forEach(empresa -> {
            if (Utils.isInRadius(empresa.getDatosFiscales().getDomicilioFiscal().getLatitud(), empresa.getDatosFiscales().getDomicilioFiscal().getLongitud(), Double.parseDouble(lat), Double.parseDouble(lon), Double.parseDouble(distance))) {
                empresasInRadius.add(empresa);
            }
        });


        List<EmpresaResponse> empresasResponse = new ArrayList<>();

        empresasInRadius.forEach(empresa -> {
            empresasResponse.add(mapEmpresaToResponse(empresa));
        });

        return ResponseEntity.ok(MultiEntityResponse.<EmpresaResponse>builder().data(empresasResponse).build());

    }
}

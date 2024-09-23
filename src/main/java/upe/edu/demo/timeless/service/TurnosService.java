package upe.edu.demo.timeless.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.controller.dto.request.Ausencia;
import upe.edu.demo.timeless.controller.dto.request.CrearEmpresaRequest;
import upe.edu.demo.timeless.controller.dto.request.GenerarTurnosRequest;
import upe.edu.demo.timeless.controller.dto.request.ParametroEmpresa;
import upe.edu.demo.timeless.controller.dto.response.Error;
import upe.edu.demo.timeless.controller.dto.response.*;
import upe.edu.demo.timeless.model.*;
import upe.edu.demo.timeless.repository.*;
import upe.edu.demo.timeless.shared.utils.Utils;
import upe.edu.demo.timeless.shared.utils.enums.EstadoTurnoEnum;
import upe.edu.demo.timeless.shared.utils.enums.MembresiaEnum;
import upe.edu.demo.timeless.shared.utils.enums.TipoUsuarioEnum;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class TurnosService {

    private final EmpresaRepository empresaRepository;
    private final DatosFiscalesRepository datosFiscalesRepository;
    private final UsuarioRepository usuarioRepository;
    private final MembresiaRepository membresiaRepository;
    private final RubroRepository rubroRepository;
    private final AgendaRepository agendaRepository;
    private final TurnoRepository turnoRepository;
    private final EstadoTurnoRepository estadoTurnoRepository;
    private final LineaAtencionRepository lineaAtencionRepository;
    private final MediosPagosRepository mediosPagosRepository;


    public ResponseEntity<GenerarTurnosResponse> generarTurnos(GenerarTurnosRequest generarTurnosRequest) {

        log.info("Generando turnos para la linea de atención con ID: {}", generarTurnosRequest);

        Optional<LineaAtencion> lineaAtencion = lineaAtencionRepository.findById(Integer.valueOf(generarTurnosRequest.getIdlineaAtencion()));

        if (!lineaAtencion.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenerarTurnosResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Linea de atencion no existe.").code("400").build()).build());
        }

        if (!lineaAtencion.get().isHabilitado()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenerarTurnosResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Linea de atencion no habilitada.").code("400").build()).build());
        }

        String rolUser = Utils.getFirstAuthority();

        if (rolUser != null && !rolUser.contains(TipoUsuarioEnum.EMPRESA.name())) {
            log.error("No tiene permisos para esta accion.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(GenerarTurnosResponse.builder().error(Error.builder().status(HttpStatus.FORBIDDEN).title("No tiene permisos para realizar esta accion").code("403").build()).build());
        }


        String username = lineaAtencion.get().getEmpresa().getUsuario().getCorreo();

        if (!username.equalsIgnoreCase(Utils.getUserEmail())) {
            log.error("No le pertenece la linea de atencion");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(GenerarTurnosResponse.builder().error(Error.builder().status(HttpStatus.FORBIDDEN).title("No le pertenece la linea de atencion").code("403").build()).build());
        }


        log.info("Linea de atención encontrada: {}", lineaAtencion.get());


        // Validar si ya se generaron turnos para las fechas solicitadas
        LocalDate diaInicio = generarTurnosRequest.getFechaDesde().toLocalDate();
        LocalDate diaFin = generarTurnosRequest.getFechaHasta().toLocalDate();

        LocalDate finalDiaInicio1 = diaInicio;
        List<Turno> turnosExistentes = lineaAtencion.get().getAgenda().getTurnos().stream().filter(turno -> {
            LocalDate fechaTurno = turno.getFhInicio().toLocalDateTime().toLocalDate();
            return !fechaTurno.isBefore(finalDiaInicio1) && !fechaTurno.isAfter(diaFin);
        }).toList();


        if (!turnosExistentes.isEmpty()) {
            log.error("Ya se han generado turnos para las fechas solicitadas.");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(GenerarTurnosResponse.builder()
                            .error(Error.builder()
                                    .status(HttpStatus.CONFLICT)
                                    .title("Ya se han generado turnos para las fechas solicitadas.")
                                    .code("409")
                                    .build())
                            .build());
        }


        Integer duracionTurno = lineaAtencion.get().getDuracionTurno();
        Integer rangoInicioAtencion = Integer.valueOf(lineaAtencion.get().getEmpresa().getCalendario().getHInicio().toString().substring(0, 2));
        Integer rangofinAtencion = Integer.valueOf(lineaAtencion.get().getEmpresa().getCalendario().getHFin().toString().substring(0, 2));

        List<Ausencias> ausencias = lineaAtencion.get().getEmpresa().getCalendario().getAusencias();

        log.info("Ausencias: {}", ausencias);


        log.info("Duración de turno: {}", duracionTurno);
        log.info("Rango de inicio de atención: {}", rangoInicioAtencion);
        log.info("Rango de fin de atención: {}", rangofinAtencion);




        // Lista para almacenar los turnos generados
        List<Turno> turnosGenerados = new ArrayList<>();


        while (!diaInicio.isAfter(diaFin)) {
            log.info("Verificando ausencias para el día: {}", diaInicio);

            // Verificamos si el día tiene una ausencia
            LocalDate finalDiaInicio = diaInicio;
            boolean esDiaConAusencia = ausencias.stream().anyMatch(ausencia ->
                    !finalDiaInicio.isBefore(ausencia.getDesde().toLocalDateTime().toLocalDate()) &&
                            !finalDiaInicio.isAfter(ausencia.getHasta().toLocalDateTime().toLocalDate()));

            if (esDiaConAusencia) {
                log.info("Día con ausencia planificada, saltando: {}", diaInicio);
            } else {
                // Generamos turnos para el día actual de 9 AM a 5 PM
                log.info("Generando turnos para el día: {}", diaInicio);
                turnosGenerados.addAll(generarTurnosParaUnDia(diaInicio, duracionTurno, rangoInicioAtencion, rangofinAtencion));
            }

            // Avanzamos al siguiente día
            diaInicio = diaInicio.plusDays(1);
        }


        // Guardamos los turnos en la base de datos

        var estadoTurno = estadoTurnoRepository.findById(2).get();
        var mediosPago = mediosPagosRepository.findById(1).get();


        turnosGenerados.forEach(turno -> {
            turno.setAgenda(lineaAtencion.get().getAgenda());
            turno.setEstadoTurno(estadoTurno);
            turno.setMediosPago(mediosPago);
            turno.setLocked(false);
        });

        lineaAtencion.get().getAgenda().setTurnos(turnosGenerados);


        log.info("Guardando turnos en la base de datos...{}", turnosGenerados);

        lineaAtencionRepository.save(lineaAtencion.get());

        return ResponseEntity.ok(GenerarTurnosResponse.builder().idLineaAtencion(generarTurnosRequest.getIdlineaAtencion()).cantidadturnosGenerados(String.valueOf(turnosGenerados.size())).build());


    }

    // Generar turnos de 15 minutos desde 9 AM hasta 5 PM para un día dado
    private List<Turno> generarTurnosParaUnDia(LocalDate dia, Integer duracionTurno, Integer rangoInicioAtencion, Integer rangofinAtencion) {
        // Hora de inicio (9 AM)
        LocalTime horaInicio = LocalTime.of(rangoInicioAtencion, 0);
        // Hora de fin (5 PM)
        LocalTime horaFin = LocalTime.of(rangofinAtencion, 0);

        log.info("Generando turnos para el día: {}", dia);
        log.info("Hora de inicio: {}", horaInicio);
        log.info("Hora de fin: {}", horaFin);
        // Convertimos las horas a LocalDateTime usando el día proporcionado
        LocalDateTime inicio = LocalDateTime.of(dia, horaInicio);
        LocalDateTime fin = LocalDateTime.of(dia, horaFin);

        // Lista para almacenar los turnos generados para este día
        List<Turno> turnos = new ArrayList<>();

        // Iteramos desde la hora inicio hasta la hora fin, generando turnos de 15 minutos
        while (inicio.isBefore(fin)) {
            // Crear un nuevo turno
            Turno turno = new Turno();
            turno.setUuid(String.valueOf(UUID.randomUUID())); // Genera un UUID único para cada turno
            turno.setFhReserva(null); // Fecha de reserva actual
            turno.setFhInicio(Timestamp.valueOf(inicio)); // Fecha de inicio del turno
            turno.setFhFin(Timestamp.valueOf(inicio.plusMinutes(15))); // Fecha de fin del turno (15 minutos después)

            // Agregamos el turno a la lista
            turnos.add(turno);

            // Avanzamos 15 minutos para el siguiente turno
            inicio = inicio.plusMinutes(duracionTurno);
        }

        return turnos; // Retornamos los turnos generados para el día
    }


    public ResponseEntity<PreseleccionarTurnoResponse> preseleccionarTurno(String hashid) {

        String rolUser = Utils.getFirstAuthority();
        log.info("rolUser: {}", rolUser);
        log.info("ROlENUM: {}", TipoUsuarioEnum.GENERAL.name());

        if (rolUser != null && !rolUser.contains(TipoUsuarioEnum.GENERAL.name())) {
            log.error("No tiene permisos para esta accion.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(PreseleccionarTurnoResponse.builder().error(Error.builder().status(HttpStatus.FORBIDDEN).title("No tiene permisos para realizar esta accion").code("403").build()).build());

        }

        Optional<Turno> turno = turnoRepository.findByUuid(hashid);

        if (!turno.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(PreseleccionarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Turno no existe.").code("400").build()).build());
        }

        if (turno.get().getLocked()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PreseleccionarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Turno ya fue seleccionado.").code("400").build()).build());
        }

        if (turno.get().getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.OTORGADO))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PreseleccionarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("el turno ya esta confirmado.").code("400").build()).build());
        }


        turno.get().setLocked(true);
        turno.get().setLokedTime(LocalTime.now());
        turno.get().setUsuario(usuarioRepository.findByCorreo(Utils.getUserEmail()).get());
        turno.get().setEstadoTurno(estadoTurnoRepository.findById(3).get());

        // Guardar el cambio en la base de datos
        turnoRepository.save(turno.get());

        // Programar la tarea para revertir el estado en dos minutos si no es confirmado
        programarRevertirEstadoTurno(turno.get(), 2);


        return ResponseEntity.ok(PreseleccionarTurnoResponse.builder().hashid(turno.get().getUuid()).mensaje("Turno pre seleccionado, debe confirmarlo antes de los proximos 2 minutos").build());

    }

    private void programarRevertirEstadoTurno(Turno turno, int minutos) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Tarea que se ejecutará después del tiempo especificado
        Runnable tareaRevertirTurno = () -> {
            Turno turnoActual = turnoRepository.findById(turno.getId()).get();
            revertirEstadoTurno(turnoActual); // Revertir el estado del turno si sigue bloqueado
        };

        // Programamos la tarea para que se ejecute después de "minutos" minutos
        scheduler.schedule(tareaRevertirTurno, minutos, TimeUnit.MINUTES);
    }

    public void revertirEstadoTurno(Turno turno) {


        // Verificar si el turno aún está bloqueado y no ha sido confirmado
        if (turno.getLocked() && turno.getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.PRE_SELECCIONADO))) {
            turno.setLocked(false);
            turno.setLokedTime(null);
            turno.setUsuario(null); // Limpiar el campo del usuario
            turno.setEstadoTurno(estadoTurnoRepository.findById(2).get()); // Cambiar el estado a "Generado"
            // Limpiar el campo de la hora
            turnoRepository.save(turno); // Guardar el estado revertido
            log.info("El turno ha sido liberado después de 2 minutos: {}", turno.getId());
        } else {
            log.info("El turno ya fue confirmado antes de los 2 minutos: {}", turno.getId());
        }
    }

    public ResponseEntity<ConfirmacionTurnoResponse> confirmarTurno(String hashid) {

        if (hashid == null || hashid.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ConfirmacionTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Id no puede ser nulo.").code("400").build()).build());
        }
        if (!Objects.requireNonNull(Utils.getFirstAuthority()).contains(TipoUsuarioEnum.GENERAL.name())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ConfirmacionTurnoResponse.builder().error(Error.builder().status(HttpStatus.FORBIDDEN).title("No tiene permisos para realizar esta accion").code("403").build()).build());
        }
        Optional<Turno> turno = turnoRepository.findByUuid(hashid);
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(Utils.getUserEmail());
        if (!usuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConfirmacionTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Usuario no existe.").code("400").build()).build());
        }
        if (!turno.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConfirmacionTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Turno no existe.").code("400").build()).build());
        }
        if (!turno.get().getUsuario().equals(usuario.get())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ConfirmacionTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("El turno no le pertenece.").code("400").build()).build());
        }

        DomicilioFiscal domicilioFiscal = turno.get().getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getDomicilioFiscal();
        String direccion = domicilioFiscal.getCalle() + " " + domicilioFiscal.getNumero() + ",  " + domicilioFiscal.getCiudad() + ", " + domicilioFiscal.getLocalidad();

        if (turno.get().getLocked() && turno.get().getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.PRE_SELECCIONADO))) {
            turno.get().setEstadoTurno(estadoTurnoRepository.findById(1).get());
            turno.get().setFhReserva(Timestamp.valueOf(LocalDateTime.now()));
            turno.get().setLocked(false);
            turno.get().setLokedTime(null);
            turnoRepository.save(turno.get());
            return ResponseEntity.ok(ConfirmacionTurnoResponse.builder().hashid(turno.get().getUuid()).mensaje("Turno confirmado exitosamente.").fechaHora(turno.get().getFhInicio().toLocalDateTime()).direccion(direccion).build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ConfirmacionTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("El turno ya fue confirmado.").code("400").build()).build());
        }


    }

    public ResponseEntity<CancelarTurnoResponse> cancelarTurno(String hashid) {


        Optional<Turno> turno = turnoRepository.findByUuid(hashid);



        if (turno.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CancelarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Turno no existe.").code("400").build()).build());

        }

        if(!turno.get().getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.OTORGADO))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CancelarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("El turno no puede ser cancelado.").code("400").build()).build());
        }
        turno.get().setEstadoTurno(estadoTurnoRepository.findById(4).get());

        turnoRepository.save(turno.get());


        return ResponseEntity.ok(CancelarTurnoResponse.builder().mensaje("Turno cancelado exitosamente.").build());



    }

    public ResponseEntity<MultiEntityResponse<TurnosResponse>> getTurnosDisponibles() {

        List<Turno> turnos = (List<Turno>) turnoRepository.findAll();

        List<Turno> turnosDisponibles;


       turnosDisponibles = turnos.stream().filter(turno ->
               (turno.getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.GENERADO)))
               && (turno.getUsuario()== null)
               && !turno.getLocked()
               && turno.getFhReserva() == null
               && turno.getAgenda().getLineaAtencion().isHabilitado()
       ).toList();


       log.info("Turnos disponibles: {}", turnosDisponibles);

        return ResponseEntity.ok(MultiEntityResponse.<TurnosResponse>builder().data(turnosDisponibles.stream().map(this::mapTurnoToTurnosResponse).toList()).build());






    }

    public TurnosResponse mapTurnoToTurnosResponse(Turno turno){

        return TurnosResponse.builder()
                .id((long) turno.getId())
                .hashid(turno.getUuid())
                .duracion(turno.getAgenda().getLineaAtencion().getDuracionTurno())
                .mensaje(turno.getAgenda().getLineaAtencion().getDescripccion())
                .rubro(turno.getAgenda().getLineaAtencion().getEmpresa().getRubro())
                .direccion(turno.getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getDomicilioFiscal().getCalle() + " " + turno.getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getDomicilioFiscal().getNumero() + ", " + turno.getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getDomicilioFiscal().getCiudad() + ", " + turno.getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getDomicilioFiscal().getLocalidad())
                .fechaHora(String.valueOf(turno.getFhInicio().toLocalDateTime()))
                .nombreEmpresa(turno.getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getNombreFantasia())
                .build();





    }

    public ResponseEntity<MultiEntityResponse<TurnosResponse>> getTurnosDisponiblesEmpresa(Long id) {


        List<Turno> turnos = (List<Turno>) turnoRepository.findAll();

        List<Turno> turnosDisponibles;


        turnosDisponibles = turnos.stream().filter(turno ->
                (turno.getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.GENERADO)))
                        && (turno.getUsuario()== null)
                        && !turno.getLocked()
                        && turno.getFhReserva() == null
                        && turno.getAgenda().getLineaAtencion().isHabilitado()
                        && turno.getAgenda().getLineaAtencion().getEmpresa().getId()==id
        ).toList();


        log.info("Turnos disponibles: {}", turnosDisponibles);

        return ResponseEntity.ok(MultiEntityResponse.<TurnosResponse>builder().data(turnosDisponibles.stream().map(this::mapTurnoToTurnosResponse).toList()).build());


    }
}

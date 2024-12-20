package upe.edu.demo.timeless.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.controller.dto.request.GenerarTurnosRequest;
import upe.edu.demo.timeless.controller.dto.response.Error;
import upe.edu.demo.timeless.controller.dto.response.*;
import upe.edu.demo.timeless.model.*;
import upe.edu.demo.timeless.repository.*;
import upe.edu.demo.timeless.service.notification.NotificationMessage;
import upe.edu.demo.timeless.service.notification.NotificationService;
import upe.edu.demo.timeless.shared.CacheWithTTL;
import upe.edu.demo.timeless.shared.utils.Utils;
import upe.edu.demo.timeless.shared.utils.enums.DiaSemana;
import upe.edu.demo.timeless.shared.utils.enums.EmailTemplate;
import upe.edu.demo.timeless.shared.utils.enums.EstadoTurnoEnum;
import upe.edu.demo.timeless.shared.utils.enums.TipoUsuarioEnum;

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
    private final CacheWithTTL<String,Turno> cacheWithTTL;

    private final NotificationService notificationService;


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
        LocalTime rangoInicioAtencion = lineaAtencion.get().getEmpresa().getCalendario().getHInicio().toLocalTime();
        LocalTime rangofinAtencion = lineaAtencion.get().getEmpresa().getCalendario().getHFin().toLocalTime();

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
                turnosGenerados.addAll(generarTurnosParaUnDia(diaInicio, lineaAtencion.get(), rangoInicioAtencion, rangofinAtencion));
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
   /* private List<Turno> generarTurnosParaUnDia(LocalDate dia, LineaAtencion lineaAtencion, Integer rangoInicioAtencion, Integer rangofinAtencion) {
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
            turno.setAgenda(lineaAtencion.getAgenda());
            turno.setUuid(String.valueOf(UUID.randomUUID())); // Genera un UUID único para cada turno
            turno.setFhReserva(null); // Fecha de reserva actual
            turno.setFhInicio(Timestamp.valueOf(inicio)); // Fecha de inicio del turno
            turno.setFhFin(Timestamp.valueOf(inicio.plusMinutes(15))); // Fecha de fin del turno (15 minutos después)

            // Agregamos el turno a la lista
            turnos.add(turno);

            // Avanzamos 15 minutos para el siguiente turno
            inicio = inicio.plusMinutes(lineaAtencion.getDuracionTurno());
        }

        return turnos; // Retornamos los turnos generados para el día
    }*/


    private List<Turno> generarTurnosParaUnDia(LocalDate dia, LineaAtencion lineaAtencion, LocalTime horaInicio, LocalTime horaFin) {
        // Convertimos las horas a LocalDateTime usando el día proporcionado
        LocalDateTime inicio = LocalDateTime.of(dia, horaInicio);
        LocalDateTime fin = LocalDateTime.of(dia, horaFin);

        log.info("Generando turnos para el día: {}", dia);
        log.info("Hora de inicio: {}", horaInicio);
        log.info("Hora de fin: {}", horaFin);

        // Lista para almacenar los turnos generados para este día
        List<Turno> turnos = new ArrayList<>();

        // Obtenemos la duración del turno en minutos desde LineaAtencion
        int duracionTurno = lineaAtencion.getDuracionTurno();

        // Iteramos desde la hora inicio hasta la hora fin, generando turnos con la duración especificada
        while (inicio.plusMinutes(duracionTurno).isBefore(fin) || inicio.plusMinutes(duracionTurno).equals(fin)) {
            // Crear un nuevo turno
            Turno turno = new Turno();
            turno.setAgenda(lineaAtencion.getAgenda());
            turno.setUuid(String.valueOf(UUID.randomUUID())); // Genera un UUID único para cada turno
            turno.setFhReserva(null); // Fecha de reserva actual
            turno.setFhInicio(Timestamp.valueOf(inicio)); // Fecha de inicio del turno
            turno.setFhFin(Timestamp.valueOf(inicio.plusMinutes(duracionTurno))); // Fecha de fin del turno

            // Agregamos el turno a la lista
            turnos.add(turno);

            // Avanzamos la duración del turno para el siguiente turno
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

        Optional<Turno> turno = Optional.ofNullable(cacheWithTTL.get(hashid));

        if (turno.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(PreseleccionarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Turno no existe.").code("400").build()).build());
        }

        if (Boolean.TRUE.equals(turno.get().getLocked())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PreseleccionarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Turno ya fue seleccionado.").code("400").build()).build());
        }

       /* if (turno.get().getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.OTORGADO))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PreseleccionarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("el turno ya esta confirmado.").code("400").build()).build());
        }*/

        Optional<Turno> turnoExistente = turnoRepository.findByFhInicioAndAgenda(turno.get().getFhInicio(), turno.get().getAgenda());

        if (turnoExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PreseleccionarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("el turno ya esta Preseleccionado.").code("400").build()).build());
        }


        Optional<Agenda> agenda = agendaRepository.findById(turno.get().getAgenda().getId());

        if (agenda.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(PreseleccionarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Agenda no existe.").code("400").build()).build());
        }


        turno.get().setAgenda(agenda.get());
        turno.get().setLocked(true);
        turno.get().setLokedTime(LocalTime.now());
        turno.get().setUsuario(usuarioRepository.findByCorreo(Utils.getUserEmail()).get());
        turno.get().setEstadoTurno(estadoTurnoRepository.findById(3).get());
        turno.get().setMediosPago(mediosPagosRepository.findById(1).get());

        // Guardar el cambio en la base de datos
        turnoRepository.save(turno.get());

        cacheWithTTL.remove(hashid);


        // Programar la tarea para revertir el estado en dos minutos si no es confirmado
        programarRevertirEstadoTurno(turno.get(), 2);


        return ResponseEntity.ok(PreseleccionarTurnoResponse.builder().hashid(turno.get().getUuid()).mensaje("Turno pre seleccionado, debe confirmarlo antes de los proximos 2 minutos").build());

    }

    private void programarRevertirEstadoTurno(Turno turno, int minutos) {

        log.info("Programando tarea para revertir el estado del turno en {} minutos.", minutos);
        log.info("Turno a revertir: {}", turno);

            try {
                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

                // Tarea que se ejecutará después del tiempo especificado
                Runnable tareaRevertirTurno = () -> {
                    Turno turnoActual = turnoRepository.findByUuid(turno.getUuid()).get();
                    log.info("Ejecutando tarea para revertir el estado del turno: {}", turnoActual);
                    revertirEstadoTurno(turnoActual); // Revertir el estado del turno si sigue bloqueado
                };

                // Programamos la tarea para que se ejecute después de "minutos" minutos
                scheduler.schedule(tareaRevertirTurno, minutos, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error("Error al programar la tarea para revertir el estado del turno: {}", e.getMessage());
            }


    }


    public void revertirEstadoTurno(Turno turno) {
            log.info("Revertir estado del turno: {}", turno);

            try {
                // Verificar si el turno aún está bloqueado y no ha sido confirmado
                if (turno.getFhReserva() == null && turno.getLocked()) {
                    log.info("Borrando Turno no confirmado...");

                    turnoRepository.deleteByUuid(turno.getUuid()); // Guardar el estado revertido

                    log.info("El turno ha sido liberado después de 2 minutos: {}", turno.getId());
                } else {
                    log.info("El turno ya fue confirmado antes de los 2 minutos: {}", turno.getId());
                }
            }catch (Exception e){
                log.error("Error al revertir el estado del turno: {}", e.getMessage());
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
        Optional<Usuario> usuarioEmpresa = usuarioRepository.findByCorreo(turno.get().getAgenda().getLineaAtencion().getEmpresa().getUsuario().getCorreo());
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


            // Enviar notificaciones de confirmación de turno Usuario
            Map<String, String> mapUser = new HashMap<>();
            mapUser.put("fechaTurno",turno.get().getFhInicio().toLocalDateTime().toLocalDate().toString());
            mapUser.put("horaTurno", turno.get().getFhInicio().toLocalDateTime().toLocalTime().toString());
            mapUser.put("nombreEmpresa", turno.get().getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getNombreFantasia());
            mapUser.put("lineaAtencion", turno.get().getAgenda().getLineaAtencion().getDescripccion());
            mapUser.put("direccion", direccion);
            mapUser.put("urlCancelar", "http://localhost:5173/cancelarTurno?hash=" + turno.get().getUuid());

            NotificationMessage notificationMessageUsuario = new NotificationMessage(EmailTemplate.TURNO_CONFIRMADO,null ,mapUser);


// Enviar notificaciones de confirmación de turno Empresa

            Map<String, String> mapEmpresa = new HashMap<>();
            mapEmpresa.put("fechaTurno",turno.get().getFhInicio().toLocalDateTime().toLocalDate().toString());
            mapEmpresa.put("horaTurno",turno.get().getFhInicio().toLocalDateTime().toLocalTime().toString() );
            mapEmpresa.put("lineaAtencion",turno.get().getAgenda().getLineaAtencion().getDescripccion());



            NotificationMessage notificationMessageEmpresa = new NotificationMessage(EmailTemplate.TURNO_TOMADO,null ,mapEmpresa);



            notificationService.sendNotificationUser(notificationMessageUsuario, usuario.get());
            notificationService.sendNotificationUser(notificationMessageEmpresa, usuarioEmpresa.get());

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


        DomicilioFiscal domicilioFiscal = turno.get().getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getDomicilioFiscal();
        String direccion = domicilioFiscal.getCalle() + " " + domicilioFiscal.getNumero() + ",  " + domicilioFiscal.getCiudad() + ", " + domicilioFiscal.getLocalidad();

        //  turnoRepository.save(turno.get());
        turnoRepository.deleteByUuid(hashid);


        // Enviar notificaciones de confirmación de turno Usuario
        Map<String, String> mapUser = new HashMap<>();
        mapUser.put("fechaTurno",turno.get().getFhInicio().toLocalDateTime().toLocalDate().toString());
        mapUser.put("horaTurno", turno.get().getFhInicio().toLocalDateTime().toLocalTime().toString());
        mapUser.put("nombreEmpresa", turno.get().getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getNombreFantasia());
        mapUser.put("lineaAtencion", turno.get().getAgenda().getLineaAtencion().getDescripccion());
        mapUser.put("direccion", direccion);

        NotificationMessage notificationMessageUsuario = new NotificationMessage(EmailTemplate.TURNO_CANCELADO,null ,mapUser);



        notificationService.sendNotificationUser(notificationMessageUsuario,  turno.get().getUsuario());
        notificationService.sendNotificationUser(notificationMessageUsuario, turno.get().getAgenda().getLineaAtencion().getEmpresa().getUsuario());

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
                .fechafin(String.valueOf(turno.getFhFin().toLocalDateTime()))
                .cordenadas(Cordenadas.builder().latitud(turno.getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getDomicilioFiscal().getLatitud().toString()).longitud(turno.getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getDomicilioFiscal().getLongitud().toString()).build())
                .nombreEmpresa(turno.getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getNombreFantasia())
                .usuarioTurnoOwner(turno.getUsuario()!=null?UsuarioTurnoOwner.builder().nombre(turno.getUsuario().getDatosPersonales().getNombre()).apellido(turno.getUsuario().getDatosPersonales().getApellido()).email(turno.getUsuario().getCorreo()).telefono(turno.getUsuario().getDatosPersonales().getTelefonoCelular()).build():null)
                .build();





    }

    public ResponseEntity<MultiEntityResponse<TurnosResponse>> getTurnosDisponiblesEmpresa(Long id, String fecha) {

        log.info("Buscando turnos disponibles para la empresa con ID: {}, fecha {}", id, fecha);

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


        if(fecha != null && !fecha.isEmpty()){

            log.info("Filtrando por fecha: {}", fecha);
            LocalDate fechaTurno = LocalDate.parse(fecha);
            turnosDisponibles = turnosDisponibles.stream().filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isEqual(fechaTurno)).toList();

        }


        log.info("Turnos disponibles: {}", turnosDisponibles);

        return ResponseEntity.ok(MultiEntityResponse.<TurnosResponse>builder().data(turnosDisponibles.stream().map(this::mapTurnoToTurnosResponse).toList()).build());


    }


    /*public ResponseEntity<TurnosResponseUser> getTurnosDisponiblesUser() {


        if (!Objects.requireNonNull(Utils.getFirstAuthority()).contains(TipoUsuarioEnum.GENERAL.name())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(TurnosResponseUser.builder().error(Error.builder().status(HttpStatus.FORBIDDEN).title("No tiene permisos para realizar esta accion").code("403").build()).build());
        }

        Optional<Usuario> usuario = usuarioRepository.findByCorreo(Utils.getUserEmail());

        if (usuario.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(TurnosResponseUser.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Usuario no existe.").code("400").build()).build());
        }

        List<Turno> turnos = (List<Turno>) usuario.get().getTurnos();




        LocalDate today = LocalDate.now();

        log.info("hora actual: {}", today);

// Filtrar turnos de hoy
        List<Turno> hoy = turnos.stream()
                .filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isEqual(today) && turno.getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.OTORGADO)))
                .toList();

// Filtrar turnos futuros
        List<Turno> futuros = turnos.stream()
                .filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isAfter(today))
                .toList();

// Filtrar turnos pasados
        List<Turno> pasados = turnos.stream()
                .filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isBefore(today))
                .toList();

        log.info("Turnos hoy: {}", hoy);
        log.info("Turnos futuros: {}", futuros);
        log.info("Turnos pasados: {}", pasados);


        return ResponseEntity.ok(TurnosResponseUser.builder().hoy(hoy.stream().map(this::mapTurnoToTurnosResponse).toList()).futuros(futuros.stream().map(this::mapTurnoToTurnosResponse).toList()).pasados(pasados.stream().map(this::mapTurnoToTurnosResponse).toList()).build());





    }*/

    public ResponseEntity<TurnosResponseUser> getTurnosDisponiblesUser() {

        if (!Objects.requireNonNull(Utils.getFirstAuthority()).contains(TipoUsuarioEnum.GENERAL.name())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(TurnosResponseUser.builder()
                            .error(Error.builder()
                                    .status(HttpStatus.FORBIDDEN)
                                    .title("No tiene permisos para realizar esta acción")
                                    .code("403")
                                    .build())
                            .build());
        }

        Optional<Usuario> usuario = usuarioRepository.findByCorreo(Utils.getUserEmail());

        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(TurnosResponseUser.builder()
                            .error(Error.builder()
                                    .status(HttpStatus.BAD_REQUEST)
                                    .title("Usuario no existe.")
                                    .code("400")
                                    .build())
                            .build());
        }

        List<Turno> turnos = (List<Turno>) usuario.get().getTurnos();
        LocalDate today = LocalDate.now();
        log.info("Fecha actual: {}", today);

        // Filtrar y ordenar turnos de hoy
        List<Turno> hoy = turnos.stream()
                .filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isEqual(today)
                        && turno.getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.OTORGADO)))
                .sorted(Comparator.comparing(turno -> turno.getFhInicio().toLocalDateTime().toLocalTime()))
                .toList();

        // Filtrar y ordenar turnos futuros
        List<Turno> futuros = turnos.stream()
                .filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isAfter(today))
                .sorted(Comparator.comparing((Turno turno) -> turno.getFhInicio().toLocalDateTime().toLocalDate())
                        .thenComparing(turno -> turno.getFhInicio().toLocalDateTime().toLocalTime()))
                .toList();

        // Filtrar y ordenar turnos pasados
        List<Turno> pasados = turnos.stream()
                .filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isBefore(today))
                .sorted(Comparator.comparing((Turno turno) -> turno.getFhInicio().toLocalDateTime().toLocalDate()).reversed()
                        .thenComparing(Comparator.comparing((Turno turno) -> turno.getFhInicio().toLocalDateTime().toLocalTime()).reversed()))
                .toList();

        log.info("Turnos hoy: {}", hoy);
        log.info("Turnos futuros: {}", futuros);
        log.info("Turnos pasados: {}", pasados);

        return ResponseEntity.ok(TurnosResponseUser.builder()
                .hoy(hoy.stream().map(this::mapTurnoToTurnosResponse).toList())
                .futuros(futuros.stream().map(this::mapTurnoToTurnosResponse).toList())
                .pasados(pasados.stream().map(this::mapTurnoToTurnosResponse).toList())
                .build());
    }



    public ResponseEntity<MultiEntityResponse<TurnosResponse>>  getTurnosDisponiblesLineaAtencion(Integer id, String fecha) {

        log.info("Buscando turnos disponibles para la Linea de atencion con ID: {}, fecha {}", id, fecha);

        List<Turno> turnos = (List<Turno>) turnoRepository.findAll();

        List<Turno> turnosDisponibles;


        turnosDisponibles = turnos.stream().filter(turno ->
                (turno.getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.GENERADO)))
                        && (turno.getUsuario()== null)
                        && !turno.getLocked()
                        && turno.getFhReserva() == null
                        && turno.getAgenda().getLineaAtencion().isHabilitado()
                        && turno.getAgenda().getLineaAtencion().getId()==id
        ).toList();


        if(fecha != null && !fecha.isEmpty()){

            log.info("Filtrando por fecha: {}", fecha);
            LocalDate fechaTurno = LocalDate.parse(fecha);
            turnosDisponibles = turnosDisponibles.stream().filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isEqual(fechaTurno)).toList();

        }


        log.info("Turnos disponibles: {}", turnosDisponibles);

        return ResponseEntity.ok(MultiEntityResponse.<TurnosResponse>builder().data(turnosDisponibles.stream().map(this::mapTurnoToTurnosResponse).toList()).build());



    }

    public ResponseEntity<MultiEntityResponse<TurnosResponse>> getTurnosDisponiblesLineaAtencionAndFecha(Integer id, String fecha, String fechahasta) {

        Optional<LineaAtencion> lineaAtencion = lineaAtencionRepository.findById(id);

        // Verificar la existencia de la linea de atención
        if (lineaAtencion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MultiEntityResponse.<TurnosResponse>builder()
                            .error(Error.builder()
                                    .status(HttpStatus.NOT_FOUND)
                                    .title("Linea de atención no existe.")
                                    .code("404")
                                    .build())
                            .build());
        }

        // Verificar si la linea está habilitada
        if (!lineaAtencion.get().isHabilitado()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(MultiEntityResponse.<TurnosResponse>builder()
                            .error(Error.builder()
                                    .status(HttpStatus.BAD_REQUEST)
                                    .title("Linea de atención no habilitada.")
                                    .code("400")
                                    .build())
                            .build());
        }

        List<Ausencias> ausencias = lineaAtencion.get().getEmpresa().getCalendario().getAusencias();

        // Validar los días de atención
        String diasSemanaAtencion = lineaAtencion.get().getEmpresa().getCalendario().getListaDiasLaborables();
        List<Integer> diasAtencion = Arrays.stream(diasSemanaAtencion.split(";"))
                .map(Integer::parseInt)
                .toList();

        log.info("Fecja inicio:{}" , fecha);
        log.info("Fecja hasta:{}" , fechahasta);
        // Obtener el rango de fechas desde y hasta
        LocalDate fechaInicio = LocalDate.parse(fecha);
        LocalDate fechaFin = LocalDate.parse(fechahasta);

        // Obtener el rango de horarios de atención
        LocalTime horaInicio = lineaAtencion.get().getEmpresa().getCalendario().getHInicio().toLocalTime();
        LocalTime horaFin = lineaAtencion.get().getEmpresa().getCalendario().getHFin().toLocalTime();

        // Lista de turnos disponibles final
        List<TurnosResponse> finalTurnosDisponibles = new ArrayList<>();

        // Iterar sobre cada día en el rango
        for (LocalDate currentDate = fechaInicio; !currentDate.isAfter(fechaFin); currentDate = currentDate.plusDays(1)) {

            // Validar si el día actual es un día laborable para la empresa
            int diaSemana = currentDate.getDayOfWeek().getValue(); // 1 = Lunes, 7 = Domingo
            if (!diasAtencion.contains(diaSemana)) {
                continue; // Si no es día laborable, pasar al siguiente día
            }

            // Validar que la fecha actual no esté en la lista de ausencias
            LocalDate finalCurrentDate = currentDate;
            boolean fechaEnAusencia = ausencias.stream()
                    .anyMatch(ausencia -> ausencia.getDesde().toLocalDateTime().toLocalDate().isEqual(finalCurrentDate));
            if (fechaEnAusencia) {
                continue; // Si la fecha está en ausencias, pasar al siguiente día
            }

            // Obtener los turnos ya otorgados para la fecha actual
            LocalDate finalCurrentDate1 = currentDate;
            List<Turno> turnosYaOtorgados = lineaAtencion.get().getAgenda().getTurnos().stream()
                    .filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isEqual(finalCurrentDate1))
                    .toList();

            // Generar turnos para el día actual
            List<Turno> turnosDisponibles = generarTurnosParaUnDia(
                    currentDate,
                    lineaAtencion.get(),
                    horaInicio,
                    horaFin);

            // Filtrar los turnos disponibles quitando los ya otorgados
            turnosDisponibles = turnosDisponibles.stream()
                    .filter(turno -> turnosYaOtorgados.stream()
                            .noneMatch(turnoYaOtorgado -> turnoYaOtorgado.getFhInicio().toLocalDateTime()
                                    .equals(turno.getFhInicio().toLocalDateTime())))
                    .toList();

            // Construir la respuesta para los turnos disponibles de ese día
            turnosDisponibles.forEach(turno -> {

                cacheWithTTL.put(turno.getUuid(), turno);

                finalTurnosDisponibles.add(TurnosResponse.builder()
                        .hashid(turno.getUuid())
                        .duracion(lineaAtencion.get().getDuracionTurno())
                        .mensaje(lineaAtencion.get().getDescripccion())
                        .rubro(lineaAtencion.get().getEmpresa().getRubro())
                        .direccion(lineaAtencion.get().getEmpresa().getDatosFiscales().getDomicilioFiscal().getCalle() + " " +
                                lineaAtencion.get().getEmpresa().getDatosFiscales().getDomicilioFiscal().getNumero() + ", " +
                                lineaAtencion.get().getEmpresa().getDatosFiscales().getDomicilioFiscal().getCiudad() + ", " +
                                lineaAtencion.get().getEmpresa().getDatosFiscales().getDomicilioFiscal().getLocalidad())
                        .fechaHora(String.valueOf(turno.getFhInicio().toLocalDateTime()))
                        .nombreEmpresa(lineaAtencion.get().getEmpresa().getDatosFiscales().getNombreFantasia())
                        .build());
            });
        }

        // Devolver la lista de turnos disponibles en el rango solicitado
        return ResponseEntity.ok(MultiEntityResponse.<TurnosResponse>builder().data(finalTurnosDisponibles).build());
    }



    public ResponseEntity<TurnosLineaAtencionResponse> getTurnosLineaAtencion(Long id) {

        if (!Objects.requireNonNull(Utils.getFirstAuthority()).contains(TipoUsuarioEnum.EMPRESA.name())) {


        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(TurnosLineaAtencionResponse.builder().error(Error.builder().status(HttpStatus.FORBIDDEN).title("No tiene permisos para realizar esta accion").code("403").build()).build());
        }

        Optional<LineaAtencion> lineaAtencion = lineaAtencionRepository.findById(Math.toIntExact(id));


        Empresa empresa  = usuarioRepository.findByCorreo(Utils.getUserEmail()).get().getEmpresas().get(0);

        if (lineaAtencion.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(TurnosLineaAtencionResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Linea de atencion no existe.").code("400").build()).build());
        }


        if (!empresa.equals(lineaAtencion.get().getEmpresa())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TurnosLineaAtencionResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Linea de atencion no le pertenece.").code("400").build()).build());
        }



        List<Turno> turnos = (List<Turno>) lineaAtencion.get().getAgenda().getTurnos();

        turnos = turnos.stream().filter(turno -> turno.getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.OTORGADO))).toList();




        LocalDate today = LocalDate.now();

        log.info("hora actual: {}", today);

// Filtrar turnos de hoy
        List<Turno> hoy = turnos.stream()
                .filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isEqual(today))
                .toList();

// Filtrar turnos futuros
        List<Turno> futuros = turnos.stream()
                .filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isAfter(today))
                .toList();

// Filtrar turnos pasados
        List<Turno> pasados = turnos.stream()
                .filter(turno -> turno.getFhInicio().toLocalDateTime().toLocalDate().isBefore(today))
                .toList();

        log.info("Turnos hoy: {}", hoy);
        log.info("Turnos futuros: {}", futuros);
        log.info("Turnos pasados: {}", pasados);


       return ResponseEntity.ok(TurnosLineaAtencionResponse.builder().hoy(hoy.stream().map(this::mapTurnoToTurnosResponse).toList()).futuros(futuros.stream().map(this::mapTurnoToTurnosResponse).toList()).pasados(pasados.stream().map(this::mapTurnoToTurnosResponse).toList()).build());




    }

    public ResponseEntity<ConfirmacionTurnoResponse> cacelarPreseleccion(String hashid) {

        Optional<Turno> turno = turnoRepository.findByUuid(hashid);

        if (turno.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConfirmacionTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Turno no existe.").code("400").build()).build());
        }

        revertirEstadoTurno(turno.get());

        return ResponseEntity.ok(ConfirmacionTurnoResponse.builder().mensaje("Turno preseleccionado cancelado exitosamente.").build());



    }


    //metodo que obtenga todos los turnos, proximos apartir de la hora actual y envie una notificacion de recordatorio  6 hs antes de la cita

    @Scheduled(cron = "0 0 * * * *") // Ejecutar cada hora en punto
    public void recordatorioTurnosProximos() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime dentroDe59Minutos = ahora.plusMinutes(59);
        LocalDateTime dentroDe61Minutos = ahora.plusMinutes(61);

        // Filtrar los turnos que comenzarán dentro del rango de 59 a 61 minutos
        List<Turno> turnosProximos = (List<Turno>) turnoRepository.findAll();

                turnosProximos = turnosProximos.stream().filter(turno -> {
                    LocalDateTime inicioTurno = turno.getFhInicio().toLocalDateTime();
                    return inicioTurno.isAfter(dentroDe59Minutos) && inicioTurno.isBefore(dentroDe61Minutos);
                })
                .toList();

        log.info("Turnos próximos para notificar: {}", turnosProximos);

        // Enviar notificaciones a los usuarios de los turnos
        turnosProximos.forEach(turno -> {


            DomicilioFiscal domicilioFiscal = turno.getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getDomicilioFiscal();
            String direccion = domicilioFiscal.getCalle() + " " + domicilioFiscal.getNumero() + ",  " + domicilioFiscal.getCiudad() + ", " + domicilioFiscal.getLocalidad();

            // Enviar notificaciones de confirmación de turno Usuario
            Map<String, String> mapUser = new HashMap<>();
            mapUser.put("fechaTurno",turno.getFhInicio().toLocalDateTime().toLocalDate().toString());
            mapUser.put("horaTurno", turno.getFhInicio().toLocalDateTime().toLocalTime().toString());
            mapUser.put("nombreEmpresa", turno.getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getNombreFantasia());
            mapUser.put("lineaAtencion", turno.getAgenda().getLineaAtencion().getDescripccion());
            mapUser.put("direccion", direccion);
            mapUser.put("urlCancelar", "http://localhost:5173/cancelarTurno?hash=" + turno.getUuid());

            NotificationMessage notificationMessageUsuario = new NotificationMessage(EmailTemplate.RECORDATORIO_TURNO,"Recordatio - "+turno.getFhInicio().toLocalDateTime().toLocalDate().toString() ,mapUser);



            notificationService.sendNotificationUser(notificationMessageUsuario,  turno.getUsuario());



        });
    }


    public ResponseEntity<CancelarTurnoResponse> cancelarTurnoUsuario(String hashid) {

        Optional<Turno> turno = turnoRepository.findByUuid(hashid);

        Usuario usuario = usuarioRepository.findByCorreo(Utils.getUserEmail()).get();

        if(usuario.getEmpresas().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CancelarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Usuario no tiene empresas asociadas.").code("400").build()).build());



        if (turno.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CancelarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("Turno no existe.").code("400").build()).build());

        }

        if(!turno.get().getEstadoTurno().getDetalle().equalsIgnoreCase(String.valueOf(EstadoTurnoEnum.OTORGADO))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CancelarTurnoResponse.builder().error(Error.builder().status(HttpStatus.BAD_REQUEST).title("El turno no puede ser cancelado.").code("400").build()).build());
        }
        turno.get().setEstadoTurno(estadoTurnoRepository.findById(4).get());


        DomicilioFiscal domicilioFiscal = turno.get().getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getDomicilioFiscal();
        String direccion = domicilioFiscal.getCalle() + " " + domicilioFiscal.getNumero() + ",  " + domicilioFiscal.getCiudad() + ", " + domicilioFiscal.getLocalidad();

        //  turnoRepository.save(turno.get());
        turnoRepository.deleteByUuid(hashid);


        // Enviar notificaciones de confirmación de turno Usuario
        Map<String, String> mapUser = new HashMap<>();
        mapUser.put("fechaTurno",turno.get().getFhInicio().toLocalDateTime().toLocalDate().toString());
        mapUser.put("horaTurno", turno.get().getFhInicio().toLocalDateTime().toLocalTime().toString());
        mapUser.put("nombreEmpresa", turno.get().getAgenda().getLineaAtencion().getEmpresa().getDatosFiscales().getNombreFantasia());
        mapUser.put("lineaAtencion", turno.get().getAgenda().getLineaAtencion().getDescripccion());
        mapUser.put("direccion", direccion);

        NotificationMessage notificationMessageUsuario = new NotificationMessage(EmailTemplate.TURNO_CANCELADO,null ,mapUser);



        notificationService.sendNotificationUser(notificationMessageUsuario,  turno.get().getUsuario());


        return ResponseEntity.ok(CancelarTurnoResponse.builder().mensaje("Turno cancelado exitosamente.").build());





    }
}

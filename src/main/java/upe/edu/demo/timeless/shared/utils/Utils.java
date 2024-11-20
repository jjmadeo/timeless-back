package upe.edu.demo.timeless.shared.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.FastMath;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import upe.edu.demo.timeless.model.Auditoria;
import upe.edu.demo.timeless.model.Turno;
import upe.edu.demo.timeless.model.Usuario;
import upe.edu.demo.timeless.repository.AuditoriaRepository;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
@Slf4j
public class Utils {



    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }


    public static Timestamp convertLocalTimeToTimestamp(LocalTime localTime) {
        // Obtener la fecha actual
        LocalDate currentDate = LocalDate.now();
        // Combinar LocalDate y LocalTime en un LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(currentDate, localTime);
        // Convertir LocalDateTime a Timestamp
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp convertStringToTimestamp(String timeString) {
        // Parsear el string a LocalTime
        LocalTime localTime = LocalTime.parse(timeString);

        // Obtener la fecha actual
        LocalDate currentDate = LocalDate.now();

        // Combinar LocalDate y LocalTime en un LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(currentDate, localTime);

        // Convertir LocalDateTime a Timestamp
        return Timestamp.valueOf(localDateTime);
    }

    public static Timestamp convertStringToTimestampDate(String dateString) {
        try {
            // Definir el formato esperado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Parsear el string a LocalDate
            LocalDate localDate = LocalDate.parse(dateString, formatter);

            // Convertir LocalDate a Timestamp (a medianoche del día especificado)
            return Timestamp.valueOf(localDate.atStartOfDay());
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear la fecha: " + e.getMessage());
            throw e; // Re-lanzar la excepción si es necesario
        }
    }


    public static String getFirstAuthority() {
        // Obtener las autoridades desde el SecurityContext
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si la autenticación y las autoridades existen
        if (authentication != null && authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            // Obtener la primera autoridad y guardarla en un string
            GrantedAuthority firstAuthority = authentication.getAuthorities().iterator().next();
            return firstAuthority.getAuthority(); // Devolver el nombre de la autoridad
        }

        // Si no hay autoridades, retornar null o un valor por defecto
        return null;
    }


    public static String getUserEmail() {
        // Obtener las autoridades desde el SecurityContext
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si la autenticación y las autoridades existen
        if (authentication != null && authentication.getPrincipal() != null ) {
            // Obtener la primera autoridad y guardarla en un string

            return ((User) authentication.getPrincipal()).getUsername(); // Devolver el nombre de la autoridad


        }

        // Si no hay autoridades, retornar null o un valor por defecto
        return null;
    }

    public static boolean isInRadius(BigDecimal latitud, BigDecimal longitud, double lat, double lon, double radio) {

        double distance = haversine(latitud.doubleValue(), longitud.doubleValue(), lat, lon);

        //redondear distancia a 1 decimal.
        distance = Math.round(distance * 10.0) / 10.0;

        distance= distance*1.50;
       log.info("Distance: " + distance + " km");
        return distance <= radio;

    }

    private static final double EARTH_RADIUS = 6371.0; // Radio de la Tierra en km

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = FastMath.sin(dLat / 2) * FastMath.sin(dLat / 2) +
                FastMath.cos(Math.toRadians(lat1)) * FastMath.cos(Math.toRadians(lat2)) *
                        FastMath.sin(dLon / 2) * FastMath.sin(dLon / 2);
        double c = 2 * FastMath.atan2(FastMath.sqrt(a), FastMath.sqrt(1 - a));
        return EARTH_RADIUS * c; // Distancia en km
    }


    public static int obtenerDiaSemanaHoy() {
        // Obtiene la fecha actual
        LocalDate fechaHoy = LocalDate.now();
        // Obtiene el día de la semana como número (1 = Lunes, 7 = Domingo)
        DayOfWeek dayOfWeek = fechaHoy.getDayOfWeek();
        return dayOfWeek.getValue();  // getValue() devuelve 1 para lunes y 7 para domingo
    }



    public static void isertAuditoria(Turno turno, String canceledby, AuditoriaRepository auditoriaRepository) {

        Usuario usuario = turno.getUsuario();
        Usuario usuarioEmpresa = turno.getAgenda().getLineaAtencion().getEmpresa().getUsuario();

        auditoriaRepository.save( Auditoria.builder()
                .fhTurno(turno.getFhInicio().toLocalDateTime())
                .usuario(usuario.getId())
                .usuarioEmpresa(usuarioEmpresa.getId())
                .canceledBy(canceledby)
                .fhEvent(LocalDateTime.now())
                .build());



    }


}

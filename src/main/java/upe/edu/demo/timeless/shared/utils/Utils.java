package upe.edu.demo.timeless.shared.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {




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
}

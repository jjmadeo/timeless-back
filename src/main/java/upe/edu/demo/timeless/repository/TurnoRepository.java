package upe.edu.demo.timeless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import upe.edu.demo.timeless.model.Agenda;
import upe.edu.demo.timeless.model.LineaAtencion;
import upe.edu.demo.timeless.model.TipoUsuario;
import upe.edu.demo.timeless.model.Turno;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TurnoRepository extends CrudRepository<Turno, Integer> {

    Optional<Turno> findByUuid(String uuid);


    Optional<Turno> findByFhInicioAndAgenda(Timestamp fhInicio, Agenda agenda);

    @Transactional
    @Modifying
    @Query("delete from Turno t where t.uuid = ?1")
    void deleteByUuid(String uuid);


}

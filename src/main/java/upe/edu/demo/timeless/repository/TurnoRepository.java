package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.TipoUsuario;
import upe.edu.demo.timeless.model.Turno;

import java.util.Optional;

@Repository
public interface TurnoRepository extends CrudRepository<Turno, Integer> {

    Optional<Turno> findByUuid(String uuid);

}

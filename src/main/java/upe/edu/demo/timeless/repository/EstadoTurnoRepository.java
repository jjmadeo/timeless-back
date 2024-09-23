package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.EstadoTurno;
import upe.edu.demo.timeless.model.LineaAtencion;


@Repository
public interface EstadoTurnoRepository extends CrudRepository<EstadoTurno, Integer> {
}

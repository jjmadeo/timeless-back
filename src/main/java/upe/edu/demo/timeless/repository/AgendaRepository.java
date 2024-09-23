package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.Agenda;
import upe.edu.demo.timeless.model.TipoUsuario;

@Repository
public interface AgendaRepository extends CrudRepository<Agenda, Integer> {
}

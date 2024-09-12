package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.Ausencias;

@Repository
public interface AusenciasRepository extends CrudRepository<Ausencias, Integer> {
}

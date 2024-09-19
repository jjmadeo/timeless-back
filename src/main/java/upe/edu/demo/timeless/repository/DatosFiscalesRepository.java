package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.ConfigUsuarioGeneral;
import upe.edu.demo.timeless.model.DatosFiscales;

import java.util.Optional;

@Repository
public interface DatosFiscalesRepository extends CrudRepository<DatosFiscales, Integer> {

    Optional<DatosFiscales> findByCuit(String cuit);
}

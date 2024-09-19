package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import upe.edu.demo.timeless.model.ConfigUsuarioGeneral;
import upe.edu.demo.timeless.model.MediosPagos;

public interface MediosPagosRepository extends CrudRepository<MediosPagos, Integer> {
}

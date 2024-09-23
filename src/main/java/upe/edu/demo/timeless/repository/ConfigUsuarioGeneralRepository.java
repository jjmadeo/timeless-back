package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.ConfigUsuarioGeneral;
import upe.edu.demo.timeless.model.TipoUsuario;

@Repository
public interface ConfigUsuarioGeneralRepository extends CrudRepository<ConfigUsuarioGeneral, Integer> {
}

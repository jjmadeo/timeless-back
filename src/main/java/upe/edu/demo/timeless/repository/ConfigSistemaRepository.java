package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.ConfigSistema;
import upe.edu.demo.timeless.model.TipoUsuario;

@Repository
public interface ConfigSistemaRepository extends CrudRepository<ConfigSistema, Integer> {
}

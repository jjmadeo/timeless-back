package upe.edu.demo.timeless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import upe.edu.demo.timeless.model.ConfigUsuarioGeneral;
import upe.edu.demo.timeless.model.TipoUsuario;

@Repository
public interface ConfigUsuarioGeneralRepository extends CrudRepository<ConfigUsuarioGeneral, Integer> {

    @Transactional
    @Modifying
    @Query("delete from ConfigUsuarioGeneral t where t.id = ?1")
    void deleteByIdnative(String id);
}

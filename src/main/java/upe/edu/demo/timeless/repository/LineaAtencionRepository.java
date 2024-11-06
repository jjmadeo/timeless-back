package upe.edu.demo.timeless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import upe.edu.demo.timeless.model.LineaAtencion;

public interface LineaAtencionRepository extends CrudRepository<LineaAtencion, Integer> {

    @Transactional
    @Modifying
    @Query("delete from LineaAtencion t where t.id = ?1")
    void deleteByIdnative(String uuid);

}

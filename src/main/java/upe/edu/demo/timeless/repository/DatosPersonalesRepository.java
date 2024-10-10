package upe.edu.demo.timeless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import upe.edu.demo.timeless.model.Agenda;
import upe.edu.demo.timeless.model.DatosFiscales;
import upe.edu.demo.timeless.model.DatosPersonales;

@Repository
public interface DatosPersonalesRepository extends CrudRepository<DatosPersonales, Integer> {

    @Transactional
    @Modifying
    @Query("delete from DatosPersonales t where t.id = ?1")
    void deleteByIdnative(String id);
}

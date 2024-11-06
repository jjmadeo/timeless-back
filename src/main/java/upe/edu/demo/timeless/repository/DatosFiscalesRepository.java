package upe.edu.demo.timeless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import upe.edu.demo.timeless.model.ConfigUsuarioGeneral;
import upe.edu.demo.timeless.model.DatosFiscales;

import java.util.Optional;

@Repository
public interface DatosFiscalesRepository extends CrudRepository<DatosFiscales, Integer> {

    @Transactional
    @Modifying
    @Query("delete from DatosFiscales t where t.id = ?1")
    void deleteByIdnative(String id);

    Optional<DatosFiscales> findByCuit(String cuit);
}

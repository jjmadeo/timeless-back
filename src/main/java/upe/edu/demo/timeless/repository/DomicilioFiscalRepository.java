package upe.edu.demo.timeless.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import upe.edu.demo.timeless.model.Agenda;
import upe.edu.demo.timeless.model.DomicilioFiscal;

@Repository
public interface DomicilioFiscalRepository extends CrudRepository<DomicilioFiscal, Integer> {

    @Transactional
    @Modifying
    @Query("delete from DomicilioFiscal t where t.id = ?1")
    void deleteByIdnative(String id);
}

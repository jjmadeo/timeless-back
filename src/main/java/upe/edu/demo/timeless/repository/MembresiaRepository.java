package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.Membresia;
import upe.edu.demo.timeless.model.TipoDocumento;

import java.util.Optional;

@Repository
public interface MembresiaRepository extends CrudRepository<Membresia, Integer> {


    Membresia findByDetalle(String detalle);
}

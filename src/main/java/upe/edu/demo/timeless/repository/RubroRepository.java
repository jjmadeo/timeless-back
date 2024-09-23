package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.Rubro;
import upe.edu.demo.timeless.model.TipoDocumento;

@Repository
public interface RubroRepository extends CrudRepository<Rubro, Integer> {
    Rubro findByDetalle(String detalle);
}

package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.TipoDocumento;
import upe.edu.demo.timeless.model.TipoUsuario;
import upe.edu.demo.timeless.model.Usuario;

import java.util.Optional;


@Repository
public interface TipoDocumentoRepository  extends CrudRepository<TipoDocumento, Integer> {
    Optional<TipoDocumento> findByDetalle(String detalle);
}

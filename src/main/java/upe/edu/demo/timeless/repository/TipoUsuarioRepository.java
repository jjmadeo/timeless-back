package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.TipoUsuario;
import upe.edu.demo.timeless.model.Usuario;

import java.util.Optional;

@Repository
public interface TipoUsuarioRepository  extends CrudRepository<TipoUsuario, Integer> {
    Optional<TipoUsuario> findByDetalle(String detalle);
}

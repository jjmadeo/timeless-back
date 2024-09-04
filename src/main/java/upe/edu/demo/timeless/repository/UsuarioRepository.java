package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

    Optional<Usuario> findByUsername(String username);
}

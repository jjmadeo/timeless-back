package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.Empresa;
import upe.edu.demo.timeless.model.TipoUsuario;

import java.util.List;

@Repository
public interface EmpresaRepository extends CrudRepository<Empresa, Integer> {
}

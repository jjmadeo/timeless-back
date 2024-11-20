package upe.edu.demo.timeless.repository;

import org.springframework.data.repository.CrudRepository;
import upe.edu.demo.timeless.model.Agenda;
import upe.edu.demo.timeless.model.Auditoria;

import java.util.List;

public interface AuditoriaRepository extends CrudRepository<Auditoria, Integer> {

    List<Auditoria> findAllByCanceledBy(String canceledBy);
    List<Auditoria> findAllByUsuario(Integer usuario);
    List<Auditoria> findAllByUsuarioEmpresa(Integer usuarioEmpresa);
}

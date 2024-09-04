package upe.edu.demo.timeless.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import upe.edu.demo.timeless.model.Empleado;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends CrudRepository<Empleado, Integer> {
    Optional<Empleado> findById(Integer id);



}

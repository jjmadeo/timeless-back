package upe.edu.demo.timeless.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import upe.edu.demo.timeless.model.Empleado;
import upe.edu.demo.timeless.repository.EmpleadoRepository;


import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class EmpleadoService {


    private final EmpleadoRepository empleadoRepository;




    public Integer deleteEmpleado(Integer id) {

        if (!empleadoRepository.existsById(id)) {
            throw new IllegalArgumentException("Empleado no encontrado con ID: " + id);
        }

        empleadoRepository.deleteById(id);

        return id;

    }


    public Empleado findEmpleadoById(Integer id) {
        return empleadoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Empleado not found"));
    }

    public List<Empleado> findAll() {
        return (List<Empleado>) empleadoRepository.findAll();
    }



    public Empleado saveEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    public Empleado updateEmpleado(Empleado empleado) {

        if (!empleadoRepository.existsById(empleado.getId())) {
            throw new IllegalArgumentException("Empleado no encontrado con ID: " + empleado.getId());
        }


        return empleadoRepository.save(empleado);
    }






}

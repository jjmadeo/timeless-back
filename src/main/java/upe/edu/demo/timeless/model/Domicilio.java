package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Objects;
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Builder
@Entity
public class Domicilio {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "calle", nullable = false, length = 50)
    private String calle;
    @Basic
    @Column(name = "numero", nullable = false, length = 50)
    private String numero;
    @Basic
    @Column(name = "ciudad", nullable = false, length = 50)
    private String ciudad;
    @Basic
    @Column(name = "localidad", nullable = false, length = 50)
    private String localidad;
    @Basic
    @Column(name = "provincia", nullable = false, length = 50)
    private String provincia;
    @Basic
    @Column(name = "pais", nullable = false, length = 50)
    private String pais;



}

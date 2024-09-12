package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Time;
import java.util.Collection;
import java.util.Objects;
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
public class Calendario {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "h_inicio", nullable = false)
    private Time hInicio;
    @Basic
    @Column(name = "h_fin", nullable = false)
    private Time hFin;
    @Basic
    @Column(name = "lista_dias_laborables", nullable = false, length = 50)
    private String listaDiasLaborables;

    @OneToMany(mappedBy = "fkCalendario",cascade = CascadeType.PERSIST)
    private Collection<Ausencias> ausencias;

}

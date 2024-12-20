package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
@Builder
public class Agenda {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "fk_linea_atencion", referencedColumnName = "id")
    private LineaAtencion lineaAtencion;
    @OneToMany(mappedBy = "agenda" ,cascade = CascadeType.ALL)
    private Collection<Turno> turnos;






}

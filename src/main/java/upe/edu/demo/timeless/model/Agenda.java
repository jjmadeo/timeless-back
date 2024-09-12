package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
public class Agenda {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne
    @JoinColumn(name = "fk_linea_atencion", referencedColumnName = "id")
    private LineaAtencion lineaAtencion;
    @OneToMany(mappedBy = "agenda" ,cascade = CascadeType.PERSIST)
    private Collection<Turno> turnos;

}

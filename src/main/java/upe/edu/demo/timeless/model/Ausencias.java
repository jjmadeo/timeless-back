package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Builder
public class Ausencias {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "desde", nullable = false)
    private Timestamp desde;
    @Column(name = "hasta", nullable = false)
    private Timestamp hasta;
    @Column(name = "descripcion", nullable = false)
    private String descripcion;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "fk_calendario")
    private Calendario calendario;

}

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
@Table(name = "Estado_Turno", schema = "TimeLess", catalog = "")
public class EstadoTurno {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "detalle", nullable = false, length = 50)
    private String detalle;


}

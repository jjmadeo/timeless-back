package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
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
    private int descripcion;
    @Column(name = "fk_calendario", nullable = false)
    private int fkCalendario;

}

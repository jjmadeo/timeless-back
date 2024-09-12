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
public class Turno {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;


    @Basic
    @Column(name = "fh_reserva", nullable = false)
    private Timestamp fhReserva;
    @Basic
    @Column(name = "fh_inicio", nullable = false)
    private Timestamp fhInicio;
    @Basic
    @Column(name = "fh_fin", nullable = false)
    private Timestamp fhFin;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_agenda", referencedColumnName = "id", nullable = false)
    private Agenda agenda;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_estado_turno", referencedColumnName = "id", nullable = false)
    private EstadoTurno estadoTurno;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_medio_págos", referencedColumnName = "id", nullable = false)
    private MediosPagos mediosPago;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_usuario", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    @Basic
    @Column(name = "locked")
    private Boolean locked;

}

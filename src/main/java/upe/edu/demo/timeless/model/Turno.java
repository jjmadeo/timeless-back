package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "Turnos", schema = "TimeLess", catalog = "")
public class Turno {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;



    // Campo `uuid` de tipo BINARY(16) en la base de datos
    // Mapeo del UUID como un String (VARCHAR(36) en la base de datos)
    @Column(name = "uuid", length = 36, nullable = false, unique = true)
    private String uuid;

    @Basic
    @Column(name = "fh_reserva")
    private Timestamp fhReserva;
    @Basic
    @Column(name = "fh_inicio", nullable = false)
    private Timestamp fhInicio;
    @Basic
    @Column(name = "fh_fin", nullable = false)
    private Timestamp fhFin;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_agenda", referencedColumnName = "id", nullable = false)
    private Agenda agenda;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_estado_turno", referencedColumnName = "id", nullable = false)
    private EstadoTurno estadoTurno;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_medio_pagos", referencedColumnName = "id", nullable = false)
    private MediosPagos mediosPago;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_usuario", referencedColumnName = "id")
    private Usuario usuario;


    @Basic
    @Column(name = "lokedtime")
    private LocalTime lokedTime;
    @Basic
    @Column(name = "locked")
    private Boolean locked;




}

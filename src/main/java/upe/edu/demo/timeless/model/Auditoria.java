package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer usuario;

    @Column(nullable = false)
    private Integer usuarioEmpresa;

    @Column(name = "fh_turno")
    private LocalDateTime fhTurno;

    @Column(name = "fh_event", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fhEvent;

    @Column(name = "canceledBy", length = 45)
    private String canceledBy;



}

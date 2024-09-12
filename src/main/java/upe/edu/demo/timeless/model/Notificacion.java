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
public class Notificacion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "origen", nullable = false, length = 50)
    private String origen;
    @Basic
    @Column(name = "destino", nullable = false, length = 50)
    private String destino;
    @Basic
    @Column(name = "template_id", nullable = false)
    private int templateId;
    @Basic
    @Column(name = "fh_envio", nullable = false)
    private Timestamp fhEnvio;
    @Basic
    @Column(name = "fh_creacion", nullable = false)
    private Timestamp fhCreacion;

    @ManyToOne
    @JoinColumn(name = "fk_estado", referencedColumnName = "id", nullable = false)
    private EstadoNotificacion estadoNotificacion;
    @ManyToOne
    @JoinColumn(name = "fk_tipo_notificacion", referencedColumnName = "id", nullable = false)
    private TipoNotificacion tipoNotificacion;


}

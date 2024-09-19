package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Data

@ToString
@NoArgsConstructor
@Builder
@Entity

public class Usuario {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "correo", nullable = false, length = 50)
    private String correo;
    @Basic
    @Column(name = "clave", nullable = false, length = 50)
    private String clave;
    @Basic
    @CreationTimestamp
    @Column(name = "fh_creacion", nullable = false)
    private Timestamp fhCreacion;
    @Basic
    @Column(name = "habilitado", nullable = false)
    private boolean habilitado;


    @OneToMany(mappedBy = "usuario",cascade = CascadeType.PERSIST)
    private List<Empresa> empresas;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_datos_personales", referencedColumnName = "id", nullable = false)
    private DatosPersonales datosPersonales;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_config_usuario_general", referencedColumnName = "id", nullable = false)
    private ConfigUsuarioGeneral configUsuarioGeneral;

    @OneToMany(mappedBy = "usuario",cascade = CascadeType.PERSIST)
    private Collection<Turno> turnos;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_tipo_usuario", referencedColumnName = "id", nullable = false)
    private TipoUsuario tipoUsuario;

}

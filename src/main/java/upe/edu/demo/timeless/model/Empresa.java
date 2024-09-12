package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Collection;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
public class Empresa {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "fh_creacion", nullable = false)
    private Timestamp fhCreacion;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_usuario_propietario", referencedColumnName = "id", nullable = false)
    private Usuario usuario;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_datos_empresa", referencedColumnName = "id", nullable = false)
    private DatosFiscales datosFiscales;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_membresia", referencedColumnName = "id", nullable = false)
    private Membresia membresia;
    @OneToOne(mappedBy = "empresa",cascade = CascadeType.PERSIST)
    private LineaAtencion lineaAtencion;
    @OneToMany(mappedBy = "fkEmpresa",cascade = CascadeType.PERSIST)
    private Collection<ParametrizacionEmpresa> parametrizaciones;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_calendario", referencedColumnName = "id", nullable = false)
    private Calendario calendario;


}

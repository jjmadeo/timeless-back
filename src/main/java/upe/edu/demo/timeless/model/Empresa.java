package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
@Builder
public class Empresa {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @CreationTimestamp
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
    @OneToMany(mappedBy = "empresa",cascade = CascadeType.PERSIST)
    private List<LineaAtencion> lineaAtencion;

    @OneToMany(mappedBy = "empresa",cascade = CascadeType.PERSIST)
    private List<ParametrizacionEmpresa> parametrizaciones;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_calendario", referencedColumnName = "id", nullable = false)
    private Calendario calendario;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_rubro", referencedColumnName = "id", nullable = false)
    private Rubro rubro;



    public void addParametro(ParametrizacionEmpresa param){

        if (this.parametrizaciones == null) {
            this.parametrizaciones = new ArrayList<>();
        }


        this.parametrizaciones.add(param);
        param.setEmpresa(this);
    }

    public void addLineaAtencion(LineaAtencion lineaAtencion) {

        if (this.lineaAtencion == null) {
            this.lineaAtencion = new ArrayList<>();
        }

        this.lineaAtencion.add(lineaAtencion);
        lineaAtencion.setEmpresa(this);
    }



}

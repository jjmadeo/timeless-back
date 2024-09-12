package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "Linea_Atencion", schema = "TimeLess", catalog = "")
public class LineaAtencion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "descripccion", nullable = false, length = 50)
    private String descripccion;
    @Basic
    @Column(name = "duracion_turno", nullable = false)
    private int duracionTurno;
    @Basic
    @Column(name = "habilitado", nullable = false)
    private boolean habilitado;
    @OneToOne(mappedBy = "lineaAtencion",cascade = CascadeType.PERSIST)
    private Agenda agenda;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_empresa", referencedColumnName = "id", nullable = false)
    private Empresa empresa;


    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_rubro", referencedColumnName = "id", nullable = false)
    private Rubro rubro;

}

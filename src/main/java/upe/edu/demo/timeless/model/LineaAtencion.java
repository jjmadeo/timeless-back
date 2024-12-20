package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Linea_Atencion", schema = "TimeLess", catalog = "")
public class LineaAtencion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "descripccion", nullable = false, length = 50)
    private String descripccion;
    @Basic
    @Column(name = "duracion_turno", nullable = false)
    private int duracionTurno;
    @Basic
    @Column(name = "habilitado", nullable = false)
    private boolean habilitado;
    @OneToOne(mappedBy = "lineaAtencion",cascade = CascadeType.ALL)
    private Agenda agenda;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_empresa", referencedColumnName = "id", nullable = false)
    private Empresa empresa;




    public void addAgenda(Agenda agenda) {
        this.agenda = agenda;
        agenda.setLineaAtencion(this);
    }

}

package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
@Entity
@Builder
public class Calendario {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "h_inicio", nullable = false)
    private Time hInicio;
    @Basic
    @Column(name = "h_fin", nullable = false)
    private Time hFin;
    @Basic
    @Column(name = "lista_dias_laborables", nullable = false, length = 50)
    private String listaDiasLaborables;

    @OneToMany(mappedBy = "calendario",cascade = CascadeType.PERSIST)
    private List<Ausencias> ausencias;



    public void addAusencias(Ausencias ausencias){

        if (this.ausencias == null) {
            this.ausencias = new ArrayList<>();
        }


        this.ausencias.add(ausencias);
        ausencias.setCalendario(this);
    }

}

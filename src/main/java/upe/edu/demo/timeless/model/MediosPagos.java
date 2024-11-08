package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Medios_Pagos", schema = "TimeLess", catalog = "")
public class MediosPagos {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "detalle", nullable = true, length = 50)
    private String detalle;
    @Basic
    @Column(name = "habilitado", nullable = true)
    private Boolean habilitado;


}

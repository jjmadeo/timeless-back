package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Objects;
@AllArgsConstructor
@Data
@ToString
@Builder
@NoArgsConstructor
@Entity
@Table(name = "Tipo_Documento", schema = "TimeLess", catalog = "")
public class TipoDocumento {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "detalle", nullable = true, length = 50)
    private String detalle;

}

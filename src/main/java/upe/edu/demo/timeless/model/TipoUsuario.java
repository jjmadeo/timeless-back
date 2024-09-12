package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@Data
@ToString
@Builder
@NoArgsConstructor
@Entity
@Table(name = "Tipo_Usuario", schema = "TimeLess", catalog = "")
public class TipoUsuario {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "detalle", nullable = false, length = 50)
    private String detalle;


}

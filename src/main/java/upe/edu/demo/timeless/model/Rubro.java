package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@Data
@ToString
@Builder
@NoArgsConstructor
@Entity
@Table(name = "Rubro", schema = "TimeLess", catalog = "")
public class Rubro {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "detalle", nullable = false, length = 50)
    private String detalle;


}

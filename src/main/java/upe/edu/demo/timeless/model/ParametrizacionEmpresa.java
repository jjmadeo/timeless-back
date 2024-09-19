package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Parametrizacion_Empresa", schema = "TimeLess", catalog = "")
public class ParametrizacionEmpresa {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "fk_empresa")
    private Empresa empresa;
    @Basic
    @Column(name = "detalle", nullable = false, length = 50)
    private String detalle;
    @Basic
    @Column(name = "clave", nullable = false, length = 50)
    private String clave;
    @Basic
    @Column(name = "valor", nullable = false, length = 500)
    private String valor;

}

package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "Parametrizacion_Empresa", schema = "TimeLess", catalog = "")
public class ParametrizacionEmpresa {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "fk_empresa", nullable = false)
    private int fkEmpresa;
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

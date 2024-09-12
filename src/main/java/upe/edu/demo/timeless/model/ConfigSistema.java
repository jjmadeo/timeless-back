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
@Table(name = "Config_Sistema", schema = "TimeLess", catalog = "")
public class ConfigSistema {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "detalle", nullable = true, length = 50)
    private String detalle;
    @Basic
    @Column(name = "clave", nullable = true, length = 50)
    private String clave;
    @Basic
    @Column(name = "valor", nullable = true, length = 500)
    private String valor;


}

package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.Objects;
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "Tipo_Notificacion", schema = "TimeLess", catalog = "")
public class TipoNotificacion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "detalle", nullable = true, length = 50)
    private String detalle;

}

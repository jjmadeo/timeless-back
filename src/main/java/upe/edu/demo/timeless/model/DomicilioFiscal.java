package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Objects;
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Domicilio_Fiscal", schema = "TimeLess", catalog = "")
public class DomicilioFiscal {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "calle", nullable = true, length = 50)
    private String calle;
    @Basic
    @Column(name = "numero", nullable = true, length = 50)
    private String numero;
    @Basic
    @Column(name = "ciudad", nullable = true, length = 50)
    private String ciudad;
    @Basic
    @Column(name = "localidad", nullable = true, length = 50)
    private String localidad;
    @Basic
    @Column(name = "provincia", nullable = true, length = 50)
    private String provincia;
    @Basic
    @Column(name = "pais", nullable = true, length = 50)
    private String pais;


}

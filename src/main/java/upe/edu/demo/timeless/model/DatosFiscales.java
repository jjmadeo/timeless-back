package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Builder
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "Datos_Fiscales", schema = "TimeLess", catalog = "")
public class DatosFiscales {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "razon_social", nullable = true, length = 50)
    private String razonSocial;
    @Basic
    @Column(name = "nombre_fantasia", nullable = true, length = 50)
    private String nombreFantasia;
    @Basic
    @Column(name = "cuit", nullable = true, length = 50)
    private String cuit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_direccion_empresa", referencedColumnName = "id")
    private DomicilioFiscal domicilioFiscal;

}

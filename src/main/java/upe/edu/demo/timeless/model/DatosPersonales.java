package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Builder
@Table(name = "Datos_Personales", schema = "TimeLess", catalog = "")
public class DatosPersonales {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "fk_usuario", nullable = true)
    private Integer fkUsuario;
    @Basic
    @Column(name = "nombre", nullable = true, length = 50)
    private String nombre;
    @Basic
    @Column(name = "apellido", nullable = true, length = 50)
    private String apellido;
    @Basic
    @Column(name = "numero_documento", nullable = true, length = 50)
    private String numeroDocumento;
    @Basic
    @Column(name = "telefono_celular", nullable = true, length = 50)
    private String telefonoCelular;
    @Basic
    @Column(name = "f_nacimiento", nullable = true, length = 10)
    private String fNacimiento;
    @ManyToOne()
    @JoinColumn(name = "fk_tipo_documento", referencedColumnName = "id")
    private TipoDocumento tipoDocumento;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_domicilio", referencedColumnName = "id")
    private Domicilio domicilio;

}

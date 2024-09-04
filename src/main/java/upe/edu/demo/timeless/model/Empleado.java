package upe.edu.demo.timeless.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@ToString
@Getter
@Setter
@Entity
@Table(name = "empleado")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLID")
    private Integer id;

    @Column(name = "EMPLNOMB", nullable = false, length = 50)
    private String nombre;

    @Column(name = "EMPLAPLL", nullable = false, length = 50)
    private String apellido;

    @Column(name = "EMPLTURN", nullable = false, length = 2)
    private String turno;

    @Column(name = "EMPLUSUA", nullable = false, length = 8)
    private String usuario;

    @Column(name = "EMPLCLAV", nullable = false, length = 100)
    private String clave;

    @Column(name = "ROLEID")
    private Integer roleId;

    @Column(name = "BAJA")
    private Date baja;

    @Column(name = "token")
    private String token;
}

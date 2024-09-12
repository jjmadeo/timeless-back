package upe.edu.demo.timeless.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
@Entity
@Builder
@Table(name = "Config_usuario_general", schema = "TimeLess", catalog = "")
public class ConfigUsuarioGeneral {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "sms", nullable = false)
    private boolean sms;
    @Basic
    @Column(name = "wpp", nullable = false)
    private boolean wpp;
    @Basic
    @Column(name = "email", nullable = false)
    private boolean email;



}

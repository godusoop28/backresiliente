package com.resiliente.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rolId", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Rol rol;

    @Column(name = "numeroEmpleado", nullable = false, unique = true)
    private Integer numeroEmpleado;

    @Column(name = "area", nullable = false, length = 100)
    private String area;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Boolean status = true;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    // Nueva columna para contraseña encriptada
    @JsonIgnore
    @Column(name = "password", nullable = false, length = 255)
    private String password;
}

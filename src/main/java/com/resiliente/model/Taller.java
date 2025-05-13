package com.resiliente.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "taller")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Taller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTaller")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(name = "imagen", columnDefinition = "TEXT")
    private String imagen; // Cambiado de byte[] a String para URL

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Boolean status = true;
}
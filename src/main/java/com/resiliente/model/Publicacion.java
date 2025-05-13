package com.resiliente.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "publicacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPublicacion")
    private Integer id;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "imagen", columnDefinition = "TEXT")
    private String imagen; // Cambiado de byte[] a String para URL

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDateTime fechaPublicacion;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Boolean status = true;
}
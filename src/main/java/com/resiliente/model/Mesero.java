package com.resiliente.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "meseros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mesero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String presentacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "condicion_id", nullable = false)
    private Condicion condicion;

    @Column(nullable = false)
    private Integer edad;

    @Column(columnDefinition = "TEXT")  // Cambiado de LONGTEXT a TEXT para URL
    private String foto;  // Ya está como String, mantener así para URL

    @Column(nullable = false)
    private Boolean status = true;
}
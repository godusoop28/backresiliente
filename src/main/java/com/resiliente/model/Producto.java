package com.resiliente.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Column(name = "foto", columnDefinition = "TEXT")
    private String foto;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Boolean status = true;

    // Nueva relación directa con Sena
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idSena", referencedColumnName = "idSena")
    private Sena sena;
}
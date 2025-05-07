package com.resiliente.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mesero")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mesero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMesero")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "presentacion", nullable = false, columnDefinition = "TEXT")
    private String presentacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "condicionId", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Condicion condicion;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Lob
    @Column(name = "foto")
    private byte[] foto;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Boolean status = true;
}
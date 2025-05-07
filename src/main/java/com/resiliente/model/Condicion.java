package com.resiliente.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Entity
@Table(name = "condicion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Condicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCondicion")
    private Integer id;

    @Column(name = "condicion", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Boolean status = true;

    @OneToMany(mappedBy = "condicion", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Mesero> meseros;
}
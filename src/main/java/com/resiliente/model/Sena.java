package com.resiliente.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "sena")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSena")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Lob
    @Column(name = "video", columnDefinition = "LONGBLOB")
    private byte[] video;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Boolean status = true;

    @OneToMany(mappedBy = "sena", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Indicacion> indicaciones;
}

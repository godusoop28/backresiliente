package com.resiliente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TallerDto {

    private Integer id;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(groups = {Crear.class, Actualizar.class}, message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(groups = {Crear.class, Actualizar.class}, message = "La fecha de fin es obligatoria")
    private LocalDateTime fechaFin;

    private String imagen; // Cambiado de byte[] a String para URL

    private Boolean status;

    // Interfaces para validación
    public interface Crear {}
    public interface Actualizar {}
}
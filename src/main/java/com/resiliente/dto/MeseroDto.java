package com.resiliente.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeseroDto {

    private Integer id;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "La presentación es obligatoria")
    private String presentacion;

    @NotNull(groups = {Crear.class, Actualizar.class}, message = "El ID de la condición es obligatorio")
    private Integer condicionId;

    @NotNull(groups = {Crear.class, Actualizar.class}, message = "La edad es obligatoria")
    @Min(value = 1, message = "La edad debe ser mayor que cero")
    private Integer edad;

    private String foto;  // Ya está como String, mantener así para URL

    private Boolean status;

    // Interfaces para validación
    public interface Crear {}
    public interface Actualizar {}
}
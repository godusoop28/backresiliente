package com.resiliente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CondicionDto {

    private Integer id;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "El nombre de la condición es obligatorio")
    @Size(max = 50, message = "El nombre de la condición no puede exceder los 50 caracteres")
    private String nombre;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "La descripción es obligatoria")
    private String descripcion;

    private Boolean status;

    // Interfaces para validación
    public interface Crear {}
    public interface Actualizar {}
}
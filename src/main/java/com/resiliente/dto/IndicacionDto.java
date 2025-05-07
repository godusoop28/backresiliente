package com.resiliente.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndicacionDto {

    private Integer id;

    @NotNull(groups = {Crear.class, Actualizar.class}, message = "El ID de la seña es obligatorio")
    private Integer senaId;

    @NotNull(groups = {Crear.class, Actualizar.class}, message = "El ID del producto es obligatorio")
    private Integer productoId;

    private Boolean status;

    // Interfaces para validación
    public interface Crear {}
    public interface Actualizar {}
}
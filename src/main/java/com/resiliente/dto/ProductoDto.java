package com.resiliente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {

    private Integer id;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @NotNull(groups = {Crear.class, Actualizar.class}, message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private BigDecimal precio;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "La descripción es obligatoria")
    private String descripcion;

    private String foto;

    @NotBlank(groups = {Crear.class}, message = "El código es obligatorio")
    @Size(max = 50, message = "El código no puede exceder los 50 caracteres")
    private String codigo;

    private String categoria;

    private Boolean status;

    // Nuevo campo para la seña
    private Integer idSena;

    // Interfaces para validación
    public interface Crear {}
    public interface Actualizar {}
}
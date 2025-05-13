package com.resiliente.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoTiendaDto {

    private Integer id;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "La descripción es obligatoria")
    private String descripcion;

    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Size(max = 50, message = "La categoría no puede exceder los 50 caracteres")
    private String categoria;

    private String imagen; // Cambiado de byte[] a String para URL

    private BigDecimal descuento;

    private LocalDateTime fechaCreacion;

    private Boolean status;

    private String caracteristicas;

    // Interfaces para validación
    public interface Crear {}
    public interface Actualizar {}
}
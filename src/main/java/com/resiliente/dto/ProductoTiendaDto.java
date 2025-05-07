package com.resiliente.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private byte[] imagen;

    @NotBlank(groups = {Crear.class}, message = "El SKU es obligatorio")
    @Size(max = 30, message = "El SKU no puede exceder los 30 caracteres")
    private String sku;

    private BigDecimal descuento;

    private Boolean destacado;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    private Boolean status;

    private String caracteristicas;

    private BigDecimal peso;

    private String dimensiones;

    // Interfaces para validación
    public interface Crear {}
    public interface Actualizar {}
}
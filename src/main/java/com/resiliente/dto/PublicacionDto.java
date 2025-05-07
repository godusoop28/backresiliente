package com.resiliente.dto;

import jakarta.validation.constraints.NotBlank;
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
public class PublicacionDto {

    private Integer id;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede exceder los 200 caracteres")
    private String titulo;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "El contenido es obligatorio")
    private String contenido;

    private byte[] imagen;

    private LocalDateTime fechaPublicacion;

    private Boolean status;

    // Interfaces para validación
    public interface Crear {}
    public interface Actualizar {}
}
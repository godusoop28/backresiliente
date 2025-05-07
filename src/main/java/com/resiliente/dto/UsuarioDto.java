package com.resiliente.dto;

import jakarta.validation.constraints.Email;
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
public class UsuarioDto {

    private Integer id;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    private String apellido;

    @NotNull(groups = {Crear.class, Actualizar.class}, message = "El ID del rol es obligatorio")
    private Integer rolId;

    @NotNull(groups = {Crear.class}, message = "El número de empleado es obligatorio")
    private Integer numeroEmpleado;

    @NotBlank(groups = {Crear.class, Actualizar.class}, message = "El área es obligatoria")
    @Size(max = 100, message = "El área no puede exceder los 100 caracteres")
    private String area;

    private Boolean status;

    @NotBlank(groups = {Crear.class}, message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    private String email;

    // Interfaces para validación
    public interface Crear {}
    public interface Actualizar {}
}
package com.resiliente.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String tipo = "Bearer";
    private Integer userId;
    private String email;
    private String nombre;
    private String apellido;
    private String rol;
    private String mensaje;

    public AuthResponse(String token, Integer userId, String email, String nombre, String apellido, String rol) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
        this.mensaje = "Autenticación exitosa";
    }
}

package com.resiliente.security;

import com.resiliente.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre().toUpperCase())
        );
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.getStatus();
    }

    // Métodos adicionales para obtener información del usuario
    public Integer getUserId() {
        return usuario.getId();
    }

    public String getRol() {
        return usuario.getRol().getNombre();
    }

    public String getNombre() {
        return usuario.getNombre();
    }

    public String getApellido() {
        return usuario.getApellido();
    }

    public Usuario getUsuario() {
        return usuario;
    }
}

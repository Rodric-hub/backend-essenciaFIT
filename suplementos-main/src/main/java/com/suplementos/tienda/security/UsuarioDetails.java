package com.suplementos.tienda.security;

import com.suplementos.tienda.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class UsuarioDetails implements UserDetails {

    private final Usuario usuario;

    public UsuarioDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Obtiene los roles del usuario, asigna "USER" si es null o vacío
        String roles = usuario.getRol();
        if (roles == null || roles.isBlank()) {
            roles = "USER"; // rol por defecto
        }

        // Si hay múltiples roles separados por coma, los mapea a GrantedAuthority
        return Arrays.stream(roles.split(","))
                     .map(String::trim)
                     .filter(r -> !r.isEmpty())
                     .map(SimpleGrantedAuthority::new)
                     .toList();
    }

    @Override
    public String getPassword() {
        return usuario.getContrasena();
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
        return true;
    }

    // Permite acceder al objeto Usuario desde templates o controllers
    public Usuario getUsuario() {
        return usuario;
    }
}

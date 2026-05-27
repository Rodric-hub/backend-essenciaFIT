package com.suplementos.tienda.security;

import com.suplementos.tienda.model.Usuario;
import com.suplementos.tienda.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    public CustomUserDetailsService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        String role = usuario.getRol() != null ? usuario.getRol() : "ROLE_USER";

        return new User(
                usuario.getEmail(),
                usuario.getContrasena(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}

package com.suplementos.tienda.controller;

import com.suplementos.tienda.model.Usuario;
import com.suplementos.tienda.repository.UsuarioRepository;
import com.suplementos.tienda.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil,
                          UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.get("email"), body.get("password"))
            );

            Usuario usuario = usuarioRepo.findByEmail(body.get("email"))
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol());

            return ResponseEntity.ok(Map.of(
                "token", token,
                "nombre", usuario.getNombre(),
                "email", usuario.getEmail(),
                "rol", usuario.getRol()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }
    }

    // POST /api/auth/registro
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        if (usuarioRepo.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ya existe una cuenta con ese email"));
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(body.get("nombre"));
        usuario.setEmail(email);
        usuario.setContrasena(passwordEncoder.encode(body.get("password")));
        usuario.setRol("ROLE_USER");
        usuarioRepo.save(usuario);

        return ResponseEntity.ok(Map.of("mensaje", "Registro exitoso"));
    }
}

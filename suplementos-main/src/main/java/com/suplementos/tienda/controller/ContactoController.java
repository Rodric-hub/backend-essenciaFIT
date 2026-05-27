package com.suplementos.tienda.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contacto")
public class ContactoController {

    // POST /api/contacto
    @PostMapping
    public ResponseEntity<?> enviar(@RequestBody Map<String, String> body) {
        // Por ahora solo imprime. Aquí puedes conectar JavaMail o similar.
        System.out.println("Contacto de: " + body.get("nombre") + " <" + body.get("email") + ">");
        System.out.println("Asunto: " + body.get("asunto"));
        System.out.println("Mensaje: " + body.get("mensaje"));

        return ResponseEntity.ok(Map.of("mensaje", "Mensaje recibido correctamente"));
    }
}

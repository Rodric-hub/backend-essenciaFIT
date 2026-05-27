package com.suplementos.tienda.controller;

import com.suplementos.tienda.model.Carrito;
import com.suplementos.tienda.model.Producto;
import com.suplementos.tienda.model.Usuario;
import com.suplementos.tienda.repository.CarritoRepository;
import com.suplementos.tienda.repository.ProductoRepository;
import com.suplementos.tienda.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoRepository carritoRepo;
    private final ProductoRepository productoRepo;
    private final UsuarioRepository usuarioRepo;

    public CarritoController(CarritoRepository carritoRepo,
                             ProductoRepository productoRepo,
                             UsuarioRepository usuarioRepo) {
        this.carritoRepo = carritoRepo;
        this.productoRepo = productoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    // GET /api/carrito
    @GetMapping
    public ResponseEntity<?> verCarrito(Authentication auth) {
        Usuario usuario = getUsuario(auth);
        List<Carrito> items = carritoRepo.findByUsuario(usuario);
        double total = items.stream()
                .mapToDouble(c -> c.getProducto().getPrecio() * c.getCantidad())
                .sum();
        return ResponseEntity.ok(Map.of("items", items, "total", total));
    }

    // POST /api/carrito/agregar
    @PostMapping("/agregar")
    public ResponseEntity<?> agregar(@RequestBody Map<String, Integer> body, Authentication auth) {
        Usuario usuario = getUsuario(auth);
        Long productoId = Long.valueOf(body.get("productoId"));
        int cantidad = body.getOrDefault("cantidad", 1);

        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Carrito existente = carritoRepo.findByUsuarioAndProducto(usuario, producto);
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
            carritoRepo.save(existente);
        } else {
            Carrito nuevo = new Carrito();
            nuevo.setUsuario(usuario);
            nuevo.setProducto(producto);
            nuevo.setCantidad(cantidad);
            carritoRepo.save(nuevo);
        }

        return ResponseEntity.ok(Map.of("mensaje", "Producto agregado al carrito"));
    }

    // DELETE /api/carrito/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, Authentication auth) {
        Usuario usuario = getUsuario(auth);
        Carrito item = carritoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        if (!item.getUsuario().getId().equals(usuario.getId())) {
            return ResponseEntity.status(403).body(Map.of("error", "No autorizado"));
        }

        carritoRepo.delete(item);
        return ResponseEntity.ok(Map.of("mensaje", "Item eliminado"));
    }

    private Usuario getUsuario(Authentication auth) {
        return usuarioRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}

package com.suplementos.tienda.controller;

import com.suplementos.tienda.model.Carrito;
import com.suplementos.tienda.model.Pedido;
import com.suplementos.tienda.model.Usuario;
import com.suplementos.tienda.repository.CarritoRepository;
import com.suplementos.tienda.repository.PedidoRepository;
import com.suplementos.tienda.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepo;
    private final CarritoRepository carritoRepo;
    private final UsuarioRepository usuarioRepo;

    public PedidoController(PedidoRepository pedidoRepo,
                            CarritoRepository carritoRepo,
                            UsuarioRepository usuarioRepo) {
        this.pedidoRepo = pedidoRepo;
        this.carritoRepo = carritoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    // POST /api/pedidos/finalizar
    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizar(Authentication auth) {
        Usuario usuario = usuarioRepo.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Carrito> carrito = carritoRepo.findByUsuario(usuario);
        if (carrito.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El carrito está vacío"));
        }

        double total = carrito.stream()
                .mapToDouble(c -> c.getProducto().getPrecio() * c.getCantidad())
                .sum();

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setTotal(total);
        pedidoRepo.save(pedido);

        carritoRepo.deleteAll(carrito);

        return ResponseEntity.ok(Map.of("mensaje", "Compra realizada con éxito", "total", total));
    }
}

// ---- ARCHIVO SEPARADO: CategoriaController.java ----
// package com.suplementos.tienda.controller;
//
// import com.suplementos.tienda.model.Categoria;
// import com.suplementos.tienda.repository.CategoriaRepository;
// import org.springframework.web.bind.annotation.*;
// import java.util.List;
//
// @RestController
// @RequestMapping("/api/categorias")
// public class CategoriaController {
//     private final CategoriaRepository categoriaRepo;
//     public CategoriaController(CategoriaRepository categoriaRepo) {
//         this.categoriaRepo = categoriaRepo;
//     }
//     @GetMapping
//     public List<Categoria> listar() {
//         return categoriaRepo.findAll();
//     }
// }

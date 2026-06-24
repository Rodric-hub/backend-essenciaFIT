package com.suplementos.tienda.controller;

import com.suplementos.tienda.model.Producto;
import com.suplementos.tienda.repository.ProductoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository productoRepo;

    public ProductoController(ProductoRepository productoRepo) {
        this.productoRepo = productoRepo;
    }

    @GetMapping
    public List<Producto> listar() {
        return productoRepo.findAll();
    }

    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Long id) {
        return productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @GetMapping("/top")
    public List<Producto> top8() {
        return productoRepo.findTop8ByOrderByIdAsc();
    }

    @GetMapping("/categoria/{id}")
    public List<Producto> porCategoria(@PathVariable Long id) {
        return productoRepo.findByCategoriaId(id);
    }
}

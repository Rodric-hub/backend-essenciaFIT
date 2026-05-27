package com.suplementos.tienda.repository;

import com.suplementos.tienda.model.Carrito;
import com.suplementos.tienda.model.Usuario;
import com.suplementos.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    List<Carrito> findByUsuario(Usuario usuario);
    Carrito findByUsuarioAndProducto(Usuario usuario, Producto producto);
}

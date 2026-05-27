package com.suplementos.tienda.repository;

import com.suplementos.tienda.model.Producto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @EntityGraph(attributePaths = {"categoria"})
    List<Producto> findTop8ByOrderByIdAsc();  

    @EntityGraph(attributePaths = {"categoria"})
    List<Producto> findByCategoriaId(Long categoriaId); 
}

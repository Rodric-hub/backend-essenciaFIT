package com.suplementos.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.suplementos.tienda.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}
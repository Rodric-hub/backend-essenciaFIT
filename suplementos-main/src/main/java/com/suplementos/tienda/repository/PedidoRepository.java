package com.suplementos.tienda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.suplementos.tienda.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {}
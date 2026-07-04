package com.techlab.repository;

import com.techlab.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Para las alertas de stock minimo
    List<Producto> findByStockLessThan(int umbral);
}

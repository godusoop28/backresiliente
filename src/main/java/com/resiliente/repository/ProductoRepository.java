package com.resiliente.repository;

import com.resiliente.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByNombre(String nombre);
    Optional<Producto> findByCodigo(String codigo); // Método que falta
    boolean existsByCodigo(String codigo);
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByStatus(Boolean status);

    // Corregir el tipo de datos: BigDecimal en lugar de Double
    List<Producto> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);
}
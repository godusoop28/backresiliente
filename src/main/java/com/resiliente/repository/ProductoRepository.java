package com.resiliente.repository;

import com.resiliente.model.Producto;
import com.resiliente.model.Sena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByNombre(String nombre);
    Optional<Producto> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByStatus(Boolean status);
    List<Producto> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);

    // Nuevos métodos para buscar por seña
    List<Producto> findBySena(Sena sena);
    List<Producto> findBySenaId(Integer idSena);
    List<Producto> findByStatusAndSenaId(Boolean status, Integer idSena);
}
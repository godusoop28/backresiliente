package com.resiliente.repository;

import com.resiliente.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByNombre(String nombre);
    boolean existsByCodigo(String codigo);
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByStatus(Boolean status);
    List<Producto> findByPrecioBetween(Double precioMin, Double precioMax);
}

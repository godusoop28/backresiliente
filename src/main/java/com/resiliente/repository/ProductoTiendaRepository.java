package com.resiliente.repository;

import com.resiliente.model.ProductoTienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoTiendaRepository extends JpaRepository<ProductoTienda, Integer> {
    Optional<ProductoTienda> findBySku(String sku);
    List<ProductoTienda> findByNombreContaining(String nombre);
    List<ProductoTienda> findByCategoria(String categoria);
    List<ProductoTienda> findByStatus(Boolean status);
    List<ProductoTienda> findByDestacado(Boolean destacado);
    List<ProductoTienda> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);
    List<ProductoTienda> findByStockLessThan(Integer stockMinimo);
    List<ProductoTienda> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
}
package com.resiliente.repository;

import com.resiliente.model.Indicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IndicacionRepository extends JpaRepository<Indicacion, Integer> {
    List<Indicacion> findBySenaId(Integer senaId);
    List<Indicacion> findByProductoId(Integer productoId);
    List<Indicacion> findByStatus(Boolean status);

    // Método que falta para verificar si ya existe una indicación con la misma seña y producto
    Optional<Indicacion> findBySenaIdAndProductoId(Integer senaId, Integer productoId);
}
package com.resiliente.repository;

import com.resiliente.model.Indicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicacionRepository extends JpaRepository<Indicacion, Integer> {
    List<Indicacion> findBySenaId(Integer senaId);
    List<Indicacion> findByProductoId(Integer productoId);
    List<Indicacion> findByStatus(Boolean status);

    @Modifying
    @Query("DELETE FROM Indicacion i WHERE i.producto.id = :productoId")
    void deleteByProductoId(Integer productoId);
}
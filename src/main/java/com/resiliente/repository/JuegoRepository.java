package com.resiliente.repository;

import com.resiliente.model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Integer> {
    Optional<Juego> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<Juego> findByStatus(Boolean status);
    List<Juego> findByNombreContainingIgnoreCase(String nombre);
}

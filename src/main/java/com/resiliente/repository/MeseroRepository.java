package com.resiliente.repository;

import com.resiliente.model.Mesero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeseroRepository extends JpaRepository<Mesero, Integer> {
    List<Mesero> findByCondicionId(Integer condicionId);
    List<Mesero> findByStatus(Boolean status);

    // Método que falta para buscar por nombre
    Optional<Mesero> findByNombre(String nombre);
}
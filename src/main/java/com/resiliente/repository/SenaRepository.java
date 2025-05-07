package com.resiliente.repository;

import com.resiliente.model.Sena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SenaRepository extends JpaRepository<Sena, Integer> {
    Optional<Sena> findByNombre(String nombre);
    List<Sena> findByStatus(Boolean status);
}
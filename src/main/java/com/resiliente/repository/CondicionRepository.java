package com.resiliente.repository;

import com.resiliente.model.Condicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CondicionRepository extends JpaRepository<Condicion, Integer> {
    Optional<Condicion> findByNombre(String nombre);
    List<Condicion> findByStatus(Boolean status);
}
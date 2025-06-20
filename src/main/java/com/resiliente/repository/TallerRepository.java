package com.resiliente.repository;

import com.resiliente.model.Taller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TallerRepository extends JpaRepository<Taller, Integer> {
    Optional<Taller> findByNombre(String nombre);
    List<Taller> findByStatus(Boolean status);
    List<Taller> findByFechaInicioBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Taller> findByFechaFinAfter(LocalDateTime fecha);
    List<Taller> findByStatusOrderByFechaInicioDesc(Boolean status);
    List<Taller> findByStatusAndFechaInicioAfter(Boolean status, LocalDateTime fecha);
}
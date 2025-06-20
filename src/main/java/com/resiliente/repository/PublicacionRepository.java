package com.resiliente.repository;

import com.resiliente.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {
    Optional<Publicacion> findByTitulo(String titulo);
    List<Publicacion> findByStatus(Boolean status);
    List<Publicacion> findByFechaPublicacionBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Publicacion> findByTituloContaining(String titulo);

    List<Publicacion> findByStatusOrderByFechaPublicacionDesc(Boolean status);
    List<Publicacion> findByStatusAndTituloContainingIgnoreCase(Boolean status, String titulo);
}
package com.resiliente.repository;

import com.resiliente.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNumeroEmpleado(Integer numeroEmpleado);
    List<Usuario> findByRolId(Integer rolId);
    boolean existsByEmail(String email);
    boolean existsByNumeroEmpleado(Integer numeroEmpleado);
    List<Usuario> findByStatus(Boolean status);
    List<Usuario> findByStatusOrderByNombreAsc(Boolean status);
    List<Usuario> findByStatusAndRolId(Boolean status, Integer rolId);
    List<Usuario> findByStatusAndAreaContainingIgnoreCase(Boolean status, String area);
}
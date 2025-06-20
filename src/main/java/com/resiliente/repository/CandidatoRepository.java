package com.resiliente.repository;

import com.resiliente.model.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Integer> {
    Optional<Candidato> findByEmail(String email);
    List<Candidato> findByStatus(Boolean status);

    List<Candidato> findByFechaEnvioBetween(LocalDateTime inicio, LocalDateTime fin);


}
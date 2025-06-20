package com.resiliente.service;

import com.resiliente.dto.CandidatoDto;
import com.resiliente.model.Candidato;
import com.resiliente.repository.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CandidatoService {

    private final CandidatoRepository candidatoRepository;

    @Autowired
    public CandidatoService(CandidatoRepository candidatoRepository) {
        this.candidatoRepository = candidatoRepository;
    }

    @Transactional
    public ResponseEntity<Object> crearCandidato(CandidatoDto candidatoDto) {
        // Verificar si ya existe un candidato con el mismo email
        Optional<Candidato> candidatoExistente = candidatoRepository.findByEmail(candidatoDto.getEmail());
        if (candidatoExistente.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un candidato con el email: " + candidatoDto.getEmail());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar el nuevo candidato
        Candidato candidato = new Candidato();
        candidato.setNombre(candidatoDto.getNombre());
        candidato.setEmail(candidatoDto.getEmail());
        candidato.setTelefono(candidatoDto.getTelefono());
        candidato.setCurriculum(candidatoDto.getCurriculum());
        candidato.setFechaEnvio(LocalDateTime.now());
        candidato.setStatus(true); // Por defecto, el candidato está activo

        Candidato candidatoGuardado = candidatoRepository.save(candidato);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", candidatoGuardado);
        response.put("mensaje", "Candidato registrado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerCandidatoPorId(Integer id) {
        Optional<Candidato> candidatoOptional = candidatoRepository.findById(id);

        if (candidatoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Candidato no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", candidatoOptional.get());
        response.put("mensaje", "Candidato encontrado");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTodosLosCandidatos() {
        List<Candidato> candidatos = candidatoRepository.findAll();

        if (candidatos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay candidatos registrados");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", candidatos);
        response.put("mensaje", "Lista de candidatos");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Método específico para obtener candidatos activos
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerCandidatosActivos() {
        List<Candidato> candidatos = candidatoRepository.findByStatus(true);

        if (candidatos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay candidatos activos");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", candidatos);
        response.put("mensaje", "Lista de candidatos activos");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Método específico para obtener candidatos inactivos
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerCandidatosInactivos() {
        List<Candidato> candidatos = candidatoRepository.findByStatus(false);

        if (candidatos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay candidatos inactivos");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", candidatos);
        response.put("mensaje", "Lista de candidatos inactivos");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerCandidatosPorEstado(Boolean status) {
        List<Candidato> candidatos = candidatoRepository.findByStatus(status);

        if (candidatos.isEmpty()) {
            String mensaje = status ? "No hay candidatos activos" : "No hay candidatos inactivos";
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", mensaje);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String mensaje = status ? "Lista de candidatos activos" : "Lista de candidatos inactivos";
        Map<String, Object> response = new HashMap<>();
        response.put("datos", candidatos);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarCandidato(Integer id, CandidatoDto candidatoDto) {
        Optional<Candidato> candidatoOptional = candidatoRepository.findById(id);

        if (candidatoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Candidato no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Candidato candidato = candidatoOptional.get();

        // Verificar si ya existe otro candidato con el mismo email
        if (candidatoDto.getEmail() != null && !candidato.getEmail().equals(candidatoDto.getEmail())) {
            Optional<Candidato> candidatoExistente = candidatoRepository.findByEmail(candidatoDto.getEmail());
            if (candidatoExistente.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Ya existe un candidato con el email: " + candidatoDto.getEmail());
                response.put("tipo", "ERROR");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        // Actualizar el candidato
        if (candidatoDto.getNombre() != null) candidato.setNombre(candidatoDto.getNombre());
        if (candidatoDto.getEmail() != null) candidato.setEmail(candidatoDto.getEmail());
        if (candidatoDto.getTelefono() != null) candidato.setTelefono(candidatoDto.getTelefono());
        if (candidatoDto.getCurriculum() != null) candidato.setCurriculum(candidatoDto.getCurriculum());
        if (candidatoDto.getStatus() != null) candidato.setStatus(candidatoDto.getStatus());

        Candidato candidatoActualizado = candidatoRepository.save(candidato);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", candidatoActualizado);
        response.put("mensaje", "Candidato actualizado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> cambiarEstadoCandidato(Integer id, Boolean status) {
        Optional<Candidato> candidatoOptional = candidatoRepository.findById(id);

        if (candidatoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Candidato no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Candidato candidato = candidatoOptional.get();
        candidato.setStatus(status);

        Candidato candidatoActualizado = candidatoRepository.save(candidato);

        String mensaje = status ? "Candidato activado exitosamente" : "Candidato desactivado exitosamente";

        Map<String, Object> response = new HashMap<>();
        response.put("datos", candidatoActualizado);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> eliminarCandidato(Integer id) {
        if (!candidatoRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Candidato no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        candidatoRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Candidato eliminado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
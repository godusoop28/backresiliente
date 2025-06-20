package com.resiliente.service;

import com.resiliente.dto.SenaDto;
import com.resiliente.model.Sena;
import com.resiliente.repository.SenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SenaService {

    private final SenaRepository senaRepository;

    @Autowired
    public SenaService(SenaRepository senaRepository) {
        this.senaRepository = senaRepository;
    }

    @Transactional
    public ResponseEntity<Object> crearSena(SenaDto senaDto) {
        // Verificar si ya existe una seña con el mismo nombre
        Optional<Sena> senaExistente = senaRepository.findByNombre(senaDto.getNombre());
        if (senaExistente.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe una seña con el nombre: " + senaDto.getNombre());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar la nueva seña
        Sena sena = new Sena();
        sena.setNombre(senaDto.getNombre());
        sena.setVideo(senaDto.getVideo());
        sena.setStatus(true); // Por defecto, la seña está activa

        Sena senaGuardada = senaRepository.save(sena);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", senaGuardada);
        response.put("mensaje", "Seña creada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerSenaPorId(Integer id) {
        Optional<Sena> senaOptional = senaRepository.findById(id);

        if (senaOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Seña no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", senaOptional.get());
        response.put("mensaje", "Seña encontrada");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTodasLasSenas() {
        List<Sena> senas = senaRepository.findAll();

        if (senas.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay señas registradas");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", senas);
        response.put("mensaje", "Lista de señas");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerSenasPorEstado(Boolean status) {
        List<Sena> senas = senaRepository.findByStatus(status);

        if (senas.isEmpty()) {
            String mensaje = status ? "No hay señas activas" : "No hay señas inactivas";
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", mensaje);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String mensaje = status ? "Lista de señas activas" : "Lista de señas inactivas";
        Map<String, Object> response = new HashMap<>();
        response.put("datos", senas);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarSena(Integer id, SenaDto senaDto) {
        Optional<Sena> senaOptional = senaRepository.findById(id);

        if (senaOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Seña no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Sena sena = senaOptional.get();

        // Verificar si ya existe otra seña con el mismo nombre
        if (senaDto.getNombre() != null && !sena.getNombre().equals(senaDto.getNombre())) {
            Optional<Sena> senaExistente = senaRepository.findByNombre(senaDto.getNombre());
            if (senaExistente.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Ya existe una seña con el nombre: " + senaDto.getNombre());
                response.put("tipo", "ERROR");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        // Actualizar la seña
        if (senaDto.getNombre() != null) sena.setNombre(senaDto.getNombre());
        if (senaDto.getVideo() != null) sena.setVideo(senaDto.getVideo());
        if (senaDto.getStatus() != null) sena.setStatus(senaDto.getStatus());

        Sena senaActualizada = senaRepository.save(sena);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", senaActualizada);
        response.put("mensaje", "Seña actualizada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> cambiarEstadoSena(Integer id, Boolean status) {
        Optional<Sena> senaOptional = senaRepository.findById(id);

        if (senaOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Seña no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Sena sena = senaOptional.get();
        sena.setStatus(status);

        Sena senaActualizada = senaRepository.save(sena);

        String mensaje = status ? "Seña activada exitosamente" : "Seña desactivada exitosamente";

        Map<String, Object> response = new HashMap<>();
        response.put("datos", senaActualizada);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> eliminarSena(Integer id) {
        if (!senaRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Seña no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        senaRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Seña eliminada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerSenasActivas() {
        List<Sena> senas = senaRepository.findByStatus(true);

        if (senas.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay señas activas");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", senas);
        response.put("mensaje", "Lista de señas activas");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerSenasInactivas() {
        List<Sena> senas = senaRepository.findByStatus(false);

        if (senas.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay señas inactivas");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", senas);
        response.put("mensaje", "Lista de señas inactivas");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
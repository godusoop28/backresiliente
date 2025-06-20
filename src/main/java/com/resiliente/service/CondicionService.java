package com.resiliente.service;

import com.resiliente.dto.CondicionDto;
import com.resiliente.model.Condicion;
import com.resiliente.repository.CondicionRepository;
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
public class CondicionService {

    private final CondicionRepository condicionRepository;

    @Autowired
    public CondicionService(CondicionRepository condicionRepository) {
        this.condicionRepository = condicionRepository;
    }

    @Transactional
    public ResponseEntity<Object> crearCondicion(CondicionDto condicionDto) {
        // Verificar si ya existe una condición con el mismo nombre
        Optional<Condicion> condicionExistente = condicionRepository.findByNombre(condicionDto.getNombre());
        if (condicionExistente.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe una condición con el nombre: " + condicionDto.getNombre());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar la nueva condición
        Condicion condicion = new Condicion();
        condicion.setNombre(condicionDto.getNombre());
        condicion.setDescripcion(condicionDto.getDescripcion());
        condicion.setStatus(true); // Por defecto, la condición está activa

        Condicion condicionGuardada = condicionRepository.save(condicion);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", condicionGuardada);
        response.put("mensaje", "Condición creada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerCondicionPorId(Integer id) {
        Optional<Condicion> condicionOptional = condicionRepository.findById(id);

        if (condicionOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Condición no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", condicionOptional.get());
        response.put("mensaje", "Condición encontrada");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTodasLasCondiciones() {
        List<Condicion> condiciones = condicionRepository.findAll();

        if (condiciones.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay condiciones registradas");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", condiciones);
        response.put("mensaje", "Lista de condiciones");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerCondicionesPorEstado(Boolean status) {
        List<Condicion> condiciones = condicionRepository.findByStatus(status);

        if (condiciones.isEmpty()) {
            String mensaje = status ? "No hay condiciones activas" : "No hay condiciones inactivas";
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", mensaje);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String mensaje = status ? "Lista de condiciones activas" : "Lista de condiciones inactivas";
        Map<String, Object> response = new HashMap<>();
        response.put("datos", condiciones);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarCondicion(Integer id, CondicionDto condicionDto) {
        Optional<Condicion> condicionOptional = condicionRepository.findById(id);

        if (condicionOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Condición no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Condicion condicion = condicionOptional.get();

        // Verificar si ya existe otra condición con el mismo nombre
        if (condicionDto.getNombre() != null && !condicion.getNombre().equals(condicionDto.getNombre())) {
            Optional<Condicion> condicionExistente = condicionRepository.findByNombre(condicionDto.getNombre());
            if (condicionExistente.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Ya existe una condición con el nombre: " + condicionDto.getNombre());
                response.put("tipo", "ERROR");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        // Actualizar la condición
        if (condicionDto.getNombre() != null) condicion.setNombre(condicionDto.getNombre());
        if (condicionDto.getDescripcion() != null) condicion.setDescripcion(condicionDto.getDescripcion());
        if (condicionDto.getStatus() != null) condicion.setStatus(condicionDto.getStatus());

        Condicion condicionActualizada = condicionRepository.save(condicion);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", condicionActualizada);
        response.put("mensaje", "Condición actualizada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> cambiarEstadoCondicion(Integer id, Boolean status) {
        Optional<Condicion> condicionOptional = condicionRepository.findById(id);

        if (condicionOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Condición no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Condicion condicion = condicionOptional.get();
        condicion.setStatus(status);

        Condicion condicionActualizada = condicionRepository.save(condicion);

        String mensaje = status ? "Condición activada exitosamente" : "Condición desactivada exitosamente";

        Map<String, Object> response = new HashMap<>();
        response.put("datos", condicionActualizada);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> eliminarCondicion(Integer id) {
        if (!condicionRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Condición no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        condicionRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Condición eliminada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerCondicionesActivas() {
        List<Condicion> condiciones = condicionRepository.findByStatus(true);

        if (condiciones.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay condiciones activas");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", condiciones);
        response.put("mensaje", "Lista de condiciones activas");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerCondicionesInactivas() {
        List<Condicion> condiciones = condicionRepository.findByStatus(false);

        if (condiciones.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay condiciones inactivas");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", condiciones);
        response.put("mensaje", "Lista de condiciones inactivas");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
package com.resiliente.service;

import com.resiliente.dto.JuegoDto;
import com.resiliente.model.Juego;
import com.resiliente.repository.JuegoRepository;
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
public class JuegoService {

    private final JuegoRepository juegoRepository;

    @Autowired
    public JuegoService(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }

    @Transactional
    public ResponseEntity<Object> crearJuego(JuegoDto juegoDto) {
        // Verificar si ya existe un juego con el mismo nombre
        if (juegoRepository.existsByNombre(juegoDto.getNombre())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un juego con el nombre: " + juegoDto.getNombre());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar el nuevo juego
        Juego juego = new Juego();
        juego.setNombre(juegoDto.getNombre());
        juego.setFoto(juegoDto.getFoto());
        juego.setStatus(true);

        Juego juegoGuardado = juegoRepository.save(juego);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", juegoGuardado);
        response.put("mensaje", "Juego creado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerJuegoPorId(Integer id) {
        Optional<Juego> juegoOptional = juegoRepository.findById(id);

        if (juegoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Juego no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", juegoOptional.get());
        response.put("mensaje", "Juego encontrado");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTodosLosJuegos() {
        List<Juego> juegos = juegoRepository.findAll();

        if (juegos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay juegos registrados");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", juegos);
        response.put("mensaje", "Lista de juegos");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerJuegosPorEstado(Boolean status) {
        List<Juego> juegos = juegoRepository.findByStatus(status);

        if (juegos.isEmpty()) {
            String mensaje = status ? "No hay juegos activos" : "No hay juegos inactivos";
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", mensaje);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String mensaje = status ? "Lista de juegos activos" : "Lista de juegos inactivos";
        Map<String, Object> response = new HashMap<>();
        response.put("datos", juegos);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> buscarJuegosPorNombre(String nombre) {
        List<Juego> juegos = juegoRepository.findByNombreContainingIgnoreCase(nombre);

        if (juegos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No se encontraron juegos con el nombre: " + nombre);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", juegos);
        response.put("mensaje", "Juegos encontrados");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarJuego(Integer id, JuegoDto juegoDto) {
        Optional<Juego> juegoOptional = juegoRepository.findById(id);

        if (juegoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Juego no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Juego juego = juegoOptional.get();

        // Verificar si ya existe otro juego con el mismo nombre
        if (juegoDto.getNombre() != null && !juego.getNombre().equals(juegoDto.getNombre()) &&
                juegoRepository.existsByNombre(juegoDto.getNombre())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un juego con el nombre: " + juegoDto.getNombre());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Actualizar el juego
        if (juegoDto.getNombre() != null) juego.setNombre(juegoDto.getNombre());
        if (juegoDto.getFoto() != null) juego.setFoto(juegoDto.getFoto());
        if (juegoDto.getStatus() != null) juego.setStatus(juegoDto.getStatus());

        Juego juegoActualizado = juegoRepository.save(juego);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", juegoActualizado);
        response.put("mensaje", "Juego actualizado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> cambiarEstadoJuego(Integer id, Boolean status) {
        Optional<Juego> juegoOptional = juegoRepository.findById(id);

        if (juegoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Juego no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Juego juego = juegoOptional.get();
        juego.setStatus(status);

        Juego juegoActualizado = juegoRepository.save(juego);

        String mensaje = status ? "Juego activado exitosamente" : "Juego desactivado exitosamente";

        Map<String, Object> response = new HashMap<>();
        response.put("datos", juegoActualizado);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> eliminarJuego(Integer id) {
        if (!juegoRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Juego no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        juegoRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Juego eliminado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerJuegosActivos() {
        List<Juego> juegos = juegoRepository.findByStatus(true);

        if (juegos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay juegos activos");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", juegos);
        response.put("mensaje", "Lista de juegos activos");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerJuegosInactivos() {
        List<Juego> juegos = juegoRepository.findByStatus(false);

        if (juegos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay juegos inactivos");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", juegos);
        response.put("mensaje", "Lista de juegos inactivos");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

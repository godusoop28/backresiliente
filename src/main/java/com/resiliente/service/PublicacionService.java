package com.resiliente.service;

import com.resiliente.dto.PublicacionDto;
import com.resiliente.model.Publicacion;
import com.resiliente.repository.PublicacionRepository;
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
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;

    @Autowired
    public PublicacionService(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    @Transactional
    public ResponseEntity<Object> crearPublicacion(PublicacionDto publicacionDto) {
        // Verificar si ya existe una publicación con el mismo título
        Optional<Publicacion> publicacionExistente = publicacionRepository.findByTitulo(publicacionDto.getTitulo());
        if (publicacionExistente.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe una publicación con el título: " + publicacionDto.getTitulo());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar la nueva publicación
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(publicacionDto.getTitulo());
        publicacion.setContenido(publicacionDto.getContenido());
        publicacion.setImagen(publicacionDto.getImagen());
        publicacion.setFechaPublicacion(LocalDateTime.now());
        publicacion.setStatus(true); // Por defecto, la publicación está activa

        Publicacion publicacionGuardada = publicacionRepository.save(publicacion);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", publicacionGuardada);
        response.put("mensaje", "Publicación creada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerPublicacionPorId(Integer id) {
        Optional<Publicacion> publicacionOptional = publicacionRepository.findById(id);

        if (publicacionOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Publicación no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", publicacionOptional.get());
        response.put("mensaje", "Publicación encontrada");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTodasLasPublicaciones() {
        List<Publicacion> publicaciones = publicacionRepository.findAll();

        if (publicaciones.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay publicaciones registradas");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", publicaciones);
        response.put("mensaje", "Lista de publicaciones");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerPublicacionesPorEstado(Boolean status) {
        List<Publicacion> publicaciones = publicacionRepository.findByStatus(status);

        if (publicaciones.isEmpty()) {
            String mensaje = status ? "No hay publicaciones activas" : "No hay publicaciones inactivas";
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", mensaje);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String mensaje = status ? "Lista de publicaciones activas" : "Lista de publicaciones inactivas";
        Map<String, Object> response = new HashMap<>();
        response.put("datos", publicaciones);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarPublicacion(Integer id, PublicacionDto publicacionDto) {
        Optional<Publicacion> publicacionOptional = publicacionRepository.findById(id);

        if (publicacionOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Publicación no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Publicacion publicacion = publicacionOptional.get();

        // Verificar si ya existe otra publicación con el mismo título
        if (publicacionDto.getTitulo() != null && !publicacion.getTitulo().equals(publicacionDto.getTitulo())) {
            Optional<Publicacion> publicacionExistente = publicacionRepository.findByTitulo(publicacionDto.getTitulo());
            if (publicacionExistente.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Ya existe una publicación con el título: " + publicacionDto.getTitulo());
                response.put("tipo", "ERROR");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        // Actualizar la publicación
        if (publicacionDto.getTitulo() != null) publicacion.setTitulo(publicacionDto.getTitulo());
        if (publicacionDto.getContenido() != null) publicacion.setContenido(publicacionDto.getContenido());
        if (publicacionDto.getImagen() != null) publicacion.setImagen(publicacionDto.getImagen());
        if (publicacionDto.getStatus() != null) publicacion.setStatus(publicacionDto.getStatus());

        Publicacion publicacionActualizada = publicacionRepository.save(publicacion);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", publicacionActualizada);
        response.put("mensaje", "Publicación actualizada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> cambiarEstadoPublicacion(Integer id, Boolean status) {
        Optional<Publicacion> publicacionOptional = publicacionRepository.findById(id);

        if (publicacionOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Publicación no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Publicacion publicacion = publicacionOptional.get();
        publicacion.setStatus(status);

        Publicacion publicacionActualizada = publicacionRepository.save(publicacion);

        String mensaje = status ? "Publicación activada exitosamente" : "Publicación desactivada exitosamente";

        Map<String, Object> response = new HashMap<>();
        response.put("datos", publicacionActualizada);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> eliminarPublicacion(Integer id) {
        if (!publicacionRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Publicación no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        publicacionRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Publicación eliminada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerPublicacionesActivas() {
        List<Publicacion> publicaciones = publicacionRepository.findByStatus(true);

        if (publicaciones.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay publicaciones activas");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", publicaciones);
        response.put("mensaje", "Lista de publicaciones activas");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerPublicacionesInactivas() {
        List<Publicacion> publicaciones = publicacionRepository.findByStatus(false);

        if (publicaciones.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay publicaciones inactivas");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", publicaciones);
        response.put("mensaje", "Lista de publicaciones inactivas");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
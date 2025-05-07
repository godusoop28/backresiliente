package com.resiliente.service;

import com.resiliente.dto.TallerDto;
import com.resiliente.model.Taller;
import com.resiliente.repository.TallerRepository;
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
public class TallerService {

    private final TallerRepository tallerRepository;

    @Autowired
    public TallerService(TallerRepository tallerRepository) {
        this.tallerRepository = tallerRepository;
    }

    @Transactional
    public ResponseEntity<Object> crearTaller(TallerDto tallerDto) {
        // Verificar si ya existe un taller con el mismo nombre
        Optional<Taller> tallerExistente = tallerRepository.findByNombre(tallerDto.getNombre());
        if (tallerExistente.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un taller con el nombre: " + tallerDto.getNombre());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar el nuevo taller
        Taller taller = new Taller();
        taller.setNombre(tallerDto.getNombre());
        taller.setDescripcion(tallerDto.getDescripcion());
        taller.setFechaInicio(tallerDto.getFechaInicio());
        taller.setFechaFin(tallerDto.getFechaFin());
        taller.setImagen(tallerDto.getImagen());
        taller.setStatus(true); // Por defecto, el taller está activo

        Taller tallerGuardado = tallerRepository.save(taller);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", tallerGuardado);
        response.put("mensaje", "Taller creado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTallerPorId(Integer id) {
        Optional<Taller> tallerOptional = tallerRepository.findById(id);

        if (tallerOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Taller no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", tallerOptional.get());
        response.put("mensaje", "Taller encontrado");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTodosLosTalleres() {
        List<Taller> talleres = tallerRepository.findAll();

        if (talleres.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay talleres registrados");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", talleres);
        response.put("mensaje", "Lista de talleres");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTalleresPorEstado(Boolean status) {
        List<Taller> talleres = tallerRepository.findByStatus(status);

        if (talleres.isEmpty()) {
            String mensaje = status ? "No hay talleres activos" : "No hay talleres inactivos";
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", mensaje);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String mensaje = status ? "Lista de talleres activos" : "Lista de talleres inactivos";
        Map<String, Object> response = new HashMap<>();
        response.put("datos", talleres);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarTaller(Integer id, TallerDto tallerDto) {
        Optional<Taller> tallerOptional = tallerRepository.findById(id);

        if (tallerOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Taller no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Taller taller = tallerOptional.get();

        // Verificar si ya existe otro taller con el mismo nombre
        if (tallerDto.getNombre() != null && !taller.getNombre().equals(tallerDto.getNombre())) {
            Optional<Taller> tallerExistente = tallerRepository.findByNombre(tallerDto.getNombre());
            if (tallerExistente.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Ya existe un taller con el nombre: " + tallerDto.getNombre());
                response.put("tipo", "ERROR");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        // Actualizar el taller
        if (tallerDto.getNombre() != null) taller.setNombre(tallerDto.getNombre());
        if (tallerDto.getDescripcion() != null) taller.setDescripcion(tallerDto.getDescripcion());
        if (tallerDto.getFechaInicio() != null) taller.setFechaInicio(tallerDto.getFechaInicio());
        if (tallerDto.getFechaFin() != null) taller.setFechaFin(tallerDto.getFechaFin());
        if (tallerDto.getImagen() != null) taller.setImagen(tallerDto.getImagen());
        if (tallerDto.getStatus() != null) taller.setStatus(tallerDto.getStatus());

        Taller tallerActualizado = tallerRepository.save(taller);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", tallerActualizado);
        response.put("mensaje", "Taller actualizado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> cambiarEstadoTaller(Integer id, Boolean status) {
        Optional<Taller> tallerOptional = tallerRepository.findById(id);

        if (tallerOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Taller no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Taller taller = tallerOptional.get();
        taller.setStatus(status);

        Taller tallerActualizado = tallerRepository.save(taller);

        String mensaje = status ? "Taller activado exitosamente" : "Taller desactivado exitosamente";

        Map<String, Object> response = new HashMap<>();
        response.put("datos", tallerActualizado);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> eliminarTaller(Integer id) {
        if (!tallerRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Taller no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        tallerRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Taller eliminado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
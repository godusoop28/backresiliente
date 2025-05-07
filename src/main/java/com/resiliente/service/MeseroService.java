package com.resiliente.service;

import com.resiliente.dto.MeseroDto;
import com.resiliente.model.Condicion;
import com.resiliente.model.Mesero;
import com.resiliente.repository.CondicionRepository;
import com.resiliente.repository.MeseroRepository;
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
public class MeseroService {

    private final MeseroRepository meseroRepository;
    private final CondicionRepository condicionRepository;

    @Autowired
    public MeseroService(MeseroRepository meseroRepository, CondicionRepository condicionRepository) {
        this.meseroRepository = meseroRepository;
        this.condicionRepository = condicionRepository;
    }

    @Transactional
    public ResponseEntity<Object> crearMesero(MeseroDto meseroDto) {
        // Verificar si existe la condición
        Optional<Condicion> condicionOptional = condicionRepository.findById(meseroDto.getCondicionId());
        if (condicionOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Condición no encontrada con ID: " + meseroDto.getCondicionId());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar el nuevo mesero
        Mesero mesero = new Mesero();
        mesero.setNombre(meseroDto.getNombre());
        mesero.setPresentacion(meseroDto.getPresentacion());
        mesero.setCondicion(condicionOptional.get());
        mesero.setEdad(meseroDto.getEdad());
        mesero.setFoto(meseroDto.getFoto());
        mesero.setStatus(true); // Por defecto, el mesero está activo

        Mesero meseroGuardado = meseroRepository.save(mesero);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", meseroGuardado);
        response.put("mensaje", "Mesero creado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerMeseroPorId(Integer id) {
        Optional<Mesero> meseroOptional = meseroRepository.findById(id);

        if (meseroOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Mesero no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", meseroOptional.get());
        response.put("mensaje", "Mesero encontrado");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTodosLosMeseros() {
        List<Mesero> meseros = meseroRepository.findAll();

        if (meseros.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay meseros registrados");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", meseros);
        response.put("mensaje", "Lista de meseros");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerMeserosPorCondicion(Integer condicionId) {
        if (!condicionRepository.existsById(condicionId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Condición no encontrada con ID: " + condicionId);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        List<Mesero> meseros = meseroRepository.findByCondicionId(condicionId);

        if (meseros.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay meseros con la condición especificada");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", meseros);
        response.put("mensaje", "Lista de meseros por condición");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerMeserosPorEstado(Boolean status) {
        List<Mesero> meseros = meseroRepository.findByStatus(status);

        if (meseros.isEmpty()) {
            String mensaje = status ? "No hay meseros activos" : "No hay meseros inactivos";
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", mensaje);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String mensaje = status ? "Lista de meseros activos" : "Lista de meseros inactivos";
        Map<String, Object> response = new HashMap<>();
        response.put("datos", meseros);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarMesero(Integer id, MeseroDto meseroDto) {
        Optional<Mesero> meseroOptional = meseroRepository.findById(id);

        if (meseroOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Mesero no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Mesero mesero = meseroOptional.get();

        // Verificar si existe la condición
        if (meseroDto.getCondicionId() != null) {
            Optional<Condicion> condicionOptional = condicionRepository.findById(meseroDto.getCondicionId());
            if (condicionOptional.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Condición no encontrada con ID: " + meseroDto.getCondicionId());
                response.put("tipo", "ERROR");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            mesero.setCondicion(condicionOptional.get());
        }

        // Actualizar el mesero
        if (meseroDto.getNombre() != null) mesero.setNombre(meseroDto.getNombre());
        if (meseroDto.getPresentacion() != null) mesero.setPresentacion(meseroDto.getPresentacion());
        if (meseroDto.getEdad() != null) mesero.setEdad(meseroDto.getEdad());
        if (meseroDto.getFoto() != null) mesero.setFoto(meseroDto.getFoto());
        if (meseroDto.getStatus() != null) mesero.setStatus(meseroDto.getStatus());

        Mesero meseroActualizado = meseroRepository.save(mesero);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", meseroActualizado);
        response.put("mensaje", "Mesero actualizado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<Object> cambiarEstadoMesero(Integer id, Boolean status) {
        Optional<Mesero> meseroOptional = meseroRepository.findById(id);

        if (meseroOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Mesero no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Mesero mesero = meseroOptional.get();
        mesero.setStatus(status);

        Mesero meseroActualizado = meseroRepository.save(mesero);

        String mensaje = status ? "Mesero activado exitosamente" : "Mesero desactivado exitosamente";

        Map<String, Object> response = new HashMap<>();
        response.put("datos", meseroActualizado);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> eliminarMesero(Integer id) {
        if (!meseroRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Mesero no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        meseroRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Mesero eliminado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
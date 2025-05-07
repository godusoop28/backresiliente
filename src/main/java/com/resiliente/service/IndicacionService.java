package com.resiliente.service;

import com.resiliente.dto.IndicacionDto;
import com.resiliente.model.Indicacion;
import com.resiliente.model.Producto;
import com.resiliente.model.Sena;
import com.resiliente.repository.IndicacionRepository;
import com.resiliente.repository.ProductoRepository;
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
public class IndicacionService {

    private final IndicacionRepository indicacionRepository;
    private final SenaRepository senaRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public IndicacionService(IndicacionRepository indicacionRepository,
                             SenaRepository senaRepository,
                             ProductoRepository productoRepository) {
        this.indicacionRepository = indicacionRepository;
        this.senaRepository = senaRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public ResponseEntity<Object> crearIndicacion(IndicacionDto indicacionDto) {
        // Verificar si existe la seña
        Optional<Sena> senaOptional = senaRepository.findById(indicacionDto.getSenaId());
        if (senaOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Seña no encontrada con ID: " + indicacionDto.getSenaId());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Verificar si existe el producto
        Optional<Producto> productoOptional = productoRepository.findById(indicacionDto.getProductoId());
        if (productoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + indicacionDto.getProductoId());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar la nueva indicación
        Indicacion indicacion = new Indicacion();
        indicacion.setSena(senaOptional.get());
        indicacion.setProducto(productoOptional.get());
        indicacion.setStatus(true); // Por defecto, la indicación está activa

        Indicacion indicacionGuardada = indicacionRepository.save(indicacion);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", indicacionGuardada);
        response.put("mensaje", "Indicación creada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerIndicacionPorId(Integer id) {
        Optional<Indicacion> indicacionOptional = indicacionRepository.findById(id);

        if (indicacionOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Indicación no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", indicacionOptional.get());
        response.put("mensaje", "Indicación encontrada");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTodasLasIndicaciones() {
        List<Indicacion> indicaciones = indicacionRepository.findAll();

        if (indicaciones.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay indicaciones registradas");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", indicaciones);
        response.put("mensaje", "Lista de indicaciones");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerIndicacionesPorSena(Integer senaId) {
        if (!senaRepository.existsById(senaId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Seña no encontrada con ID: " + senaId);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        List<Indicacion> indicaciones = indicacionRepository.findBySenaId(senaId);

        if (indicaciones.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay indicaciones para la seña especificada");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", indicaciones);
        response.put("mensaje", "Lista de indicaciones por seña");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerIndicacionesPorProducto(Integer productoId) {
        if (!productoRepository.existsById(productoId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + productoId);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        List<Indicacion> indicaciones = indicacionRepository.findByProductoId(productoId);

        if (indicaciones.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay indicaciones para el producto especificado");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", indicaciones);
        response.put("mensaje", "Lista de indicaciones por producto");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerIndicacionesPorEstado(Boolean status) {
        List<Indicacion> indicaciones = indicacionRepository.findByStatus(status);

        if (indicaciones.isEmpty()) {
            String mensaje = status ? "No hay indicaciones activas" : "No hay indicaciones inactivas";
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", mensaje);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String mensaje = status ? "Lista de indicaciones activas" : "Lista de indicaciones inactivas";
        Map<String, Object> response = new HashMap<>();
        response.put("datos", indicaciones);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarIndicacion(Integer id, IndicacionDto indicacionDto) {
        Optional<Indicacion> indicacionOptional = indicacionRepository.findById(id);

        if (indicacionOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Indicación no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Indicacion indicacion = indicacionOptional.get();

        // Verificar si existe la seña
        if (indicacionDto.getSenaId() != null) {
            Optional<Sena> senaOptional = senaRepository.findById(indicacionDto.getSenaId());
            if (senaOptional.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Seña no encontrada con ID: " + indicacionDto.getSenaId());
                response.put("tipo", "ERROR");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            indicacion.setSena(senaOptional.get());
        }

        // Verificar si existe el producto
        if (indicacionDto.getProductoId() != null) {
            Optional<Producto> productoOptional = productoRepository.findById(indicacionDto.getProductoId());
            if (productoOptional.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Producto no encontrado con ID: " + indicacionDto.getProductoId());
                response.put("tipo", "ERROR");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            indicacion.setProducto(productoOptional.get());
        }

        // Actualizar el estado si se proporciona
        if (indicacionDto.getStatus() != null) {
            indicacion.setStatus(indicacionDto.getStatus());
        }

        Indicacion indicacionActualizada = indicacionRepository.save(indicacion);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", indicacionActualizada);
        response.put("mensaje", "Indicación actualizada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> cambiarEstadoIndicacion(Integer id, Boolean status) {
        Optional<Indicacion> indicacionOptional = indicacionRepository.findById(id);

        if (indicacionOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Indicación no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Indicacion indicacion = indicacionOptional.get();
        indicacion.setStatus(status);

        Indicacion indicacionActualizada = indicacionRepository.save(indicacion);

        String mensaje = status ? "Indicación activada exitosamente" : "Indicación desactivada exitosamente";

        Map<String, Object> response = new HashMap<>();
        response.put("datos", indicacionActualizada);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> eliminarIndicacion(Integer id) {
        if (!indicacionRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Indicación no encontrada con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        indicacionRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Indicación eliminada exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
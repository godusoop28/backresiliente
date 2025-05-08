package com.resiliente.utils;

import com.resiliente.dto.MeseroDto;
import com.resiliente.model.Condicion;
import com.resiliente.model.Mesero;
import com.resiliente.repository.CondicionRepository;
import com.resiliente.repository.MeseroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/debug")
@CrossOrigin(origins = "*")
public class DebugController {

    private final CondicionRepository condicionRepository;
    private final MeseroRepository meseroRepository;

    @Autowired
    public DebugController(CondicionRepository condicionRepository, MeseroRepository meseroRepository) {
        this.condicionRepository = condicionRepository;
        this.meseroRepository = meseroRepository;
    }

    @GetMapping("/condiciones")
    public ResponseEntity<Object> testCondiciones() {
        try {
            List<Condicion> condiciones = condicionRepository.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Test de condiciones exitoso");
            response.put("cantidad", condiciones.size());
            response.put("condiciones", condiciones);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error en test de condiciones: " + e.getMessage());
            response.put("error", e.getClass().getName());
            response.put("stackTrace", e.getStackTrace());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/meseros/tabla")
    public ResponseEntity<Object> testTablaMeseros() {
        try {
            List<Mesero> meseros = meseroRepository.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Test de tabla meseros exitoso");
            response.put("cantidad", meseros.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error en test de tabla meseros: " + e.getMessage());
            response.put("error", e.getClass().getName());
            response.put("stackTrace", e.getStackTrace());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/meseros/test")
    public ResponseEntity<Object> testCrearMesero(@RequestBody MeseroDto meseroDto) {
        try {
            Map<String, Object> response = new HashMap<>();

            // Paso 1: Verificar si existe la condición
            response.put("paso1", "Verificando condición");
            Optional<Condicion> condicionOptional = condicionRepository.findById(meseroDto.getCondicionId());
            if (condicionOptional.isEmpty()) {
                response.put("error", "Condición no encontrada con ID: " + meseroDto.getCondicionId());
                return ResponseEntity.badRequest().body(response);
            }
            response.put("condicion", condicionOptional.get());

            // Paso 2: Crear objeto Mesero
            response.put("paso2", "Creando objeto Mesero");
            Mesero mesero = new Mesero();
            mesero.setNombre(meseroDto.getNombre());
            mesero.setPresentacion(meseroDto.getPresentacion());
            mesero.setCondicion(condicionOptional.get());
            mesero.setEdad(meseroDto.getEdad());
            mesero.setFoto(meseroDto.getFoto());
            mesero.setStatus(meseroDto.getStatus() != null ? meseroDto.getStatus() : true);
            response.put("meseroCreado", mesero);

            // No guardamos en la base de datos para evitar errores
            response.put("mensaje", "Test de creación de mesero exitoso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error en test de creación de mesero: " + e.getMessage());
            response.put("error", e.getClass().getName());
            response.put("stackTrace", e.getStackTrace());
            return ResponseEntity.status(500).body(response);
        }
    }
}
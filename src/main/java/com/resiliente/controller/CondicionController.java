package com.resiliente.controller;

import com.resiliente.dto.CondicionDto;
import com.resiliente.service.CondicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/condiciones")
@CrossOrigin(origins = "*")
public class CondicionController {

    private final CondicionService condicionService;

    @Autowired
    public CondicionController(CondicionService condicionService) {
        this.condicionService = condicionService;
    }

    @PostMapping
    public ResponseEntity<Object> crearCondicion(@RequestBody CondicionDto condicionDto) {
        return condicionService.crearCondicion(condicionDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerCondicionPorId(@PathVariable Integer id) {
        return condicionService.obtenerCondicionPorId(id);
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodasLasCondiciones() {
        return condicionService.obtenerTodasLasCondiciones();
    }

    @GetMapping("/estado/{status}")
    public ResponseEntity<Object> obtenerCondicionesPorEstado(@PathVariable Boolean status) {
        return condicionService.obtenerCondicionesPorEstado(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarCondicion(@PathVariable Integer id, @RequestBody CondicionDto condicionDto) {
        return condicionService.actualizarCondicion(id, condicionDto);
    }

    @PatchMapping("/{id}/estado/{status}")
    public ResponseEntity<Object> cambiarEstadoCondicion(@PathVariable Integer id, @PathVariable Boolean status) {
        return condicionService.cambiarEstadoCondicion(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarCondicion(@PathVariable Integer id) {
        return condicionService.eliminarCondicion(id);
    }
}
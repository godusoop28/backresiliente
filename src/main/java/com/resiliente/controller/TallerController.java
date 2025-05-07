package com.resiliente.controller;

import com.resiliente.dto.TallerDto;
import com.resiliente.service.TallerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/talleres")
@CrossOrigin(origins = "*")
public class TallerController {

    private final TallerService tallerService;

    @Autowired
    public TallerController(TallerService tallerService) {
        this.tallerService = tallerService;
    }

    @PostMapping
    public ResponseEntity<Object> crearTaller(@RequestBody TallerDto tallerDto) {
        return tallerService.crearTaller(tallerDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerTallerPorId(@PathVariable Integer id) {
        return tallerService.obtenerTallerPorId(id);
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodosLosTalleres() {
        return tallerService.obtenerTodosLosTalleres();
    }

    @GetMapping("/estado/{status}")
    public ResponseEntity<Object> obtenerTalleresPorEstado(@PathVariable Boolean status) {
        return tallerService.obtenerTalleresPorEstado(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarTaller(@PathVariable Integer id, @RequestBody TallerDto tallerDto) {
        return tallerService.actualizarTaller(id, tallerDto);
    }

    @PatchMapping("/{id}/estado/{status}")
    public ResponseEntity<Object> cambiarEstadoTaller(@PathVariable Integer id, @PathVariable Boolean status) {
        return tallerService.cambiarEstadoTaller(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarTaller(@PathVariable Integer id) {
        return tallerService.eliminarTaller(id);
    }
}
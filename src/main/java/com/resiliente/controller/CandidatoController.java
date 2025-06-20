package com.resiliente.controller;

import com.resiliente.dto.CandidatoDto;
import com.resiliente.service.CandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidatos")
@CrossOrigin(origins = "*")
public class CandidatoController {

    private final CandidatoService candidatoService;

    @Autowired
    public CandidatoController(CandidatoService candidatoService) {
        this.candidatoService = candidatoService;
    }

    @PostMapping
    public ResponseEntity<Object> crearCandidato(@RequestBody CandidatoDto candidatoDto) {
        return candidatoService.crearCandidato(candidatoDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerCandidatoPorId(@PathVariable Integer id) {
        return candidatoService.obtenerCandidatoPorId(id);
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodosLosCandidatos() {
        return candidatoService.obtenerTodosLosCandidatos();
    }

    // Endpoint específico para candidatos activos
    @GetMapping("/activos")
    public ResponseEntity<Object> obtenerCandidatosActivos() {
        return candidatoService.obtenerCandidatosActivos();
    }

    // Endpoint específico para candidatos inactivos
    @GetMapping("/inactivos")
    public ResponseEntity<Object> obtenerCandidatosInactivos() {
        return candidatoService.obtenerCandidatosInactivos();
    }

    @GetMapping("/estado/{status}")
    public ResponseEntity<Object> obtenerCandidatosPorEstado(@PathVariable Boolean status) {
        return candidatoService.obtenerCandidatosPorEstado(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarCandidato(@PathVariable Integer id, @RequestBody CandidatoDto candidatoDto) {
        return candidatoService.actualizarCandidato(id, candidatoDto);
    }

    @PatchMapping("/{id}/estado/{status}")
    public ResponseEntity<Object> cambiarEstadoCandidato(@PathVariable Integer id, @PathVariable Boolean status) {
        return candidatoService.cambiarEstadoCandidato(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarCandidato(@PathVariable Integer id) {
        return candidatoService.eliminarCandidato(id);
    }
}
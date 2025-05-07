package com.resiliente.controller;

import com.resiliente.dto.SenaDto;
import com.resiliente.service.SenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/senas")
@CrossOrigin(origins = "*")
public class SenaController {

    private final SenaService senaService;

    @Autowired
    public SenaController(SenaService senaService) {
        this.senaService = senaService;
    }

    @PostMapping
    public ResponseEntity<Object> crearSena(@RequestBody SenaDto senaDto) {
        return senaService.crearSena(senaDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerSenaPorId(@PathVariable Integer id) {
        return senaService.obtenerSenaPorId(id);
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodasLasSenas() {
        return senaService.obtenerTodasLasSenas();
    }

    @GetMapping("/estado/{status}")
    public ResponseEntity<Object> obtenerSenasPorEstado(@PathVariable Boolean status) {
        return senaService.obtenerSenasPorEstado(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarSena(@PathVariable Integer id, @RequestBody SenaDto senaDto) {
        return senaService.actualizarSena(id, senaDto);
    }

    @PatchMapping("/{id}/estado/{status}")
    public ResponseEntity<Object> cambiarEstadoSena(@PathVariable Integer id, @PathVariable Boolean status) {
        return senaService.cambiarEstadoSena(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarSena(@PathVariable Integer id) {
        return senaService.eliminarSena(id);
    }
}
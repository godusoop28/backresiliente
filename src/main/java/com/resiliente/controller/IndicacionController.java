package com.resiliente.controller;

import com.resiliente.dto.IndicacionDto;
import com.resiliente.service.IndicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/indicaciones")
@CrossOrigin(origins = "*")
public class IndicacionController {

    private final IndicacionService indicacionService;

    @Autowired
    public IndicacionController(IndicacionService indicacionService) {
        this.indicacionService = indicacionService;
    }

    @PostMapping
    public ResponseEntity<Object> crearIndicacion(@RequestBody IndicacionDto indicacionDto) {
        return indicacionService.crearIndicacion(indicacionDto);
    }
    @DeleteMapping("/producto/{productoId}")
    public ResponseEntity<Object> eliminarIndicacionesPorProducto(@PathVariable Integer productoId) {
        return indicacionService.eliminarIndicacionesPorProducto(productoId);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerIndicacionPorId(@PathVariable Integer id) {
        return indicacionService.obtenerIndicacionPorId(id);
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodasLasIndicaciones() {
        return indicacionService.obtenerTodasLasIndicaciones();
    }

    @GetMapping("/sena/{senaId}")
    public ResponseEntity<Object> obtenerIndicacionesPorSena(@PathVariable Integer senaId) {
        return indicacionService.obtenerIndicacionesPorSena(senaId);
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Object> obtenerIndicacionesPorProducto(@PathVariable Integer productoId) {
        return indicacionService.obtenerIndicacionesPorProducto(productoId);
    }

    @GetMapping("/estado/{status}")
    public ResponseEntity<Object> obtenerIndicacionesPorEstado(@PathVariable Boolean status) {
        return indicacionService.obtenerIndicacionesPorEstado(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarIndicacion(@PathVariable Integer id, @RequestBody IndicacionDto indicacionDto) {
        return indicacionService.actualizarIndicacion(id, indicacionDto);
    }

    @PatchMapping("/{id}/estado/{status}")
    public ResponseEntity<Object> cambiarEstadoIndicacion(@PathVariable Integer id, @PathVariable Boolean status) {
        return indicacionService.cambiarEstadoIndicacion(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarIndicacion(@PathVariable Integer id) {
        return indicacionService.eliminarIndicacion(id);
    }
}
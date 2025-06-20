package com.resiliente.controller;

import com.resiliente.dto.JuegoDto;
import com.resiliente.service.JuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/juegos")
@CrossOrigin(origins = "*")
public class JuegoController {

    private final JuegoService juegoService;

    @Autowired
    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @PostMapping
    public ResponseEntity<Object> crearJuego(@RequestBody JuegoDto juegoDto) {
        return juegoService.crearJuego(juegoDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerJuegoPorId(@PathVariable Integer id) {
        return juegoService.obtenerJuegoPorId(id);
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodosLosJuegos() {
        return juegoService.obtenerTodosLosJuegos();
    }

    @GetMapping("/estado/{status}")
    public ResponseEntity<Object> obtenerJuegosPorEstado(@PathVariable Boolean status) {
        return juegoService.obtenerJuegosPorEstado(status);
    }

    @GetMapping("/buscar/{nombre}")
    public ResponseEntity<Object> buscarJuegosPorNombre(@PathVariable String nombre) {
        return juegoService.buscarJuegosPorNombre(nombre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarJuego(@PathVariable Integer id, @RequestBody JuegoDto juegoDto) {
        return juegoService.actualizarJuego(id, juegoDto);
    }

    @PatchMapping("/{id}/estado/{status}")
    public ResponseEntity<Object> cambiarEstadoJuego(@PathVariable Integer id, @PathVariable Boolean status) {
        return juegoService.cambiarEstadoJuego(id, status);
    }
    @GetMapping("/activos")
    public ResponseEntity<Object> obtenerJuegosActivos() {
        return juegoService.obtenerJuegosActivos();
    }

    @GetMapping("/inactivos")
    public ResponseEntity<Object> obtenerJuegosInactivos() {
        return juegoService.obtenerJuegosInactivos();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarJuego(@PathVariable Integer id) {
        return juegoService.eliminarJuego(id);
    }
}

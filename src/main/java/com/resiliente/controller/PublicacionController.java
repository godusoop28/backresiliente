package com.resiliente.controller;

import com.resiliente.dto.PublicacionDto;
import com.resiliente.service.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publicaciones")
@CrossOrigin(origins = "*")
public class PublicacionController {

    private final PublicacionService publicacionService;

    @Autowired
    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping
    public ResponseEntity<Object> crearPublicacion(@RequestBody PublicacionDto publicacionDto) {
        return publicacionService.crearPublicacion(publicacionDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPublicacionPorId(@PathVariable Integer id) {
        return publicacionService.obtenerPublicacionPorId(id);
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodasLasPublicaciones() {
        return publicacionService.obtenerTodasLasPublicaciones();
    }

    @GetMapping("/estado/{status}")
    public ResponseEntity<Object> obtenerPublicacionesPorEstado(@PathVariable Boolean status) {
        return publicacionService.obtenerPublicacionesPorEstado(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarPublicacion(@PathVariable Integer id, @RequestBody PublicacionDto publicacionDto) {
        return publicacionService.actualizarPublicacion(id, publicacionDto);
    }

    @PatchMapping("/{id}/estado/{status}")
    public ResponseEntity<Object> cambiarEstadoPublicacion(@PathVariable Integer id, @PathVariable Boolean status) {
        return publicacionService.cambiarEstadoPublicacion(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarPublicacion(@PathVariable Integer id) {
        return publicacionService.eliminarPublicacion(id);
    }
}
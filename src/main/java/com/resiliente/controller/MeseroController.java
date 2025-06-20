package com.resiliente.controller;

import com.resiliente.dto.MeseroDto;
import com.resiliente.service.MeseroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meseros")
@CrossOrigin(origins = "*")
public class MeseroController {

    private final MeseroService meseroService;

    @Autowired
    public MeseroController(MeseroService meseroService) {
        this.meseroService = meseroService;
    }

    @PostMapping
    public ResponseEntity<Object> crearMesero(@RequestBody MeseroDto meseroDto) {
        return meseroService.crearMesero(meseroDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerMeseroPorId(@PathVariable Integer id) {
        return meseroService.obtenerMeseroPorId(id);
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodosLosMeseros() {
        return meseroService.obtenerTodosLosMeseros();
    }

    @GetMapping("/condicion/{condicionId}")
    public ResponseEntity<Object> obtenerMeserosPorCondicion(@PathVariable Integer condicionId) {
        return meseroService.obtenerMeserosPorCondicion(condicionId);
    }

    @GetMapping("/estado/{status}")
    public ResponseEntity<Object> obtenerMeserosPorEstado(@PathVariable Boolean status) {
        return meseroService.obtenerMeserosPorEstado(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarMesero(@PathVariable Integer id, @RequestBody MeseroDto meseroDto) {
        return meseroService.actualizarMesero(id, meseroDto);
    }

    @PatchMapping("/{id}/estado/{status}")
    public ResponseEntity<Object> cambiarEstadoMesero(@PathVariable Integer id, @PathVariable Boolean status) {
        return meseroService.cambiarEstadoMesero(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarMesero(@PathVariable Integer id) {
        return meseroService.eliminarMesero(id);
    }
    @GetMapping("/activos")
    public ResponseEntity<Object> obtenerMeserosActivos() {
        return meseroService.obtenerMeserosActivos();
    }

    @GetMapping("/inactivos")
    public ResponseEntity<Object> obtenerMeserosInactivos() {
        return meseroService.obtenerMeserosInactivos();
    }


}
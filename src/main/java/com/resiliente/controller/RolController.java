package com.resiliente.controller;

import com.resiliente.dto.RolDto;
import com.resiliente.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "*")
public class RolController {

    private final RolService rolService;

    @Autowired
    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @PostMapping
    public ResponseEntity<Object> crearRol(@RequestBody RolDto rolDto) {
        return rolService.crearRol(rolDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerRolPorId(@PathVariable Integer id) {
        return rolService.obtenerRolPorId(id);
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodosLosRoles() {
        return rolService.obtenerTodosLosRoles();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarRol(@PathVariable Integer id, @RequestBody RolDto rolDto) {
        return rolService.actualizarRol(id, rolDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarRol(@PathVariable Integer id) {
        return rolService.eliminarRol(id);
    }
}
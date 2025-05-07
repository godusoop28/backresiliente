package com.resiliente.service;

import com.resiliente.dto.RolDto;
import com.resiliente.model.Rol;
import com.resiliente.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RolService {

    private final RolRepository rolRepository;

    @Autowired
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional
    public ResponseEntity<Object> crearRol(RolDto rolDto) {
        // Verificar si ya existe un rol con el mismo nombre
        if (rolRepository.existsByNombre(rolDto.getNombre())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un rol con el nombre: " + rolDto.getNombre());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar el nuevo rol
        Rol rol = new Rol();
        rol.setNombre(rolDto.getNombre());

        Rol rolGuardado = rolRepository.save(rol);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", rolGuardado);
        response.put("mensaje", "Rol creado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerRolPorId(Integer id) {
        Optional<Rol> rolOptional = rolRepository.findById(id);

        if (rolOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Rol no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", rolOptional.get());
        response.put("mensaje", "Rol encontrado");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTodosLosRoles() {
        List<Rol> roles = rolRepository.findAll();

        if (roles.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay roles registrados");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", roles);
        response.put("mensaje", "Lista de roles");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarRol(Integer id, RolDto rolDto) {
        Optional<Rol> rolOptional = rolRepository.findById(id);

        if (rolOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Rol no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Rol rol = rolOptional.get();

        // Verificar si ya existe otro rol con el mismo nombre
        if (!rol.getNombre().equals(rolDto.getNombre()) &&
                rolRepository.existsByNombre(rolDto.getNombre())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un rol con el nombre: " + rolDto.getNombre());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Actualizar el rol
        rol.setNombre(rolDto.getNombre());
        Rol rolActualizado = rolRepository.save(rol);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", rolActualizado);
        response.put("mensaje", "Rol actualizado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> eliminarRol(Integer id) {
        if (!rolRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Rol no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        rolRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Rol eliminado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
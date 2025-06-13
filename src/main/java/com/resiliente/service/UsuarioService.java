package com.resiliente.service;

import com.resiliente.dto.UsuarioDto;
import com.resiliente.model.Rol;
import com.resiliente.model.Usuario;
import com.resiliente.repository.RolRepository;
import com.resiliente.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<Object> crearUsuario(UsuarioDto usuarioDto) {
        // Verificar si ya existe un usuario con el mismo email
        if (usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un usuario con el email: " + usuarioDto.getEmail());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Verificar si ya existe un usuario con el mismo número de empleado
        if (usuarioRepository.existsByNumeroEmpleado(usuarioDto.getNumeroEmpleado())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un usuario con el número de empleado: " + usuarioDto.getNumeroEmpleado());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Verificar si existe el rol
        Optional<Rol> rolOptional = rolRepository.findById(usuarioDto.getRolId());
        if (rolOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Rol no encontrado con ID: " + usuarioDto.getRolId());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar el nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDto.getNombre());
        usuario.setApellido(usuarioDto.getApellido());
        usuario.setRol(rolOptional.get());
        usuario.setNumeroEmpleado(usuarioDto.getNumeroEmpleado());
        usuario.setArea(usuarioDto.getArea());
        usuario.setStatus(true); // Por defecto, el usuario está activo
        usuario.setEmail(usuarioDto.getEmail());
        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", usuarioGuardado);
        response.put("mensaje", "Usuario creado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerUsuarioPorId(Integer id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", usuarioOptional.get());
        response.put("mensaje", "Usuario encontrado");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay usuarios registrados");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", usuarios);
        response.put("mensaje", "Lista de usuarios");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerUsuariosPorRol(Integer rolId) {
        if (!rolRepository.existsById(rolId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Rol no encontrado con ID: " + rolId);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        List<Usuario> usuarios = usuarioRepository.findByRolId(rolId);

        if (usuarios.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay usuarios con el rol especificado");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", usuarios);
        response.put("mensaje", "Lista de usuarios por rol");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarUsuario(Integer id, UsuarioDto usuarioDto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();

        // Verificar si ya existe otro usuario con el mismo email
        if (usuarioDto.getEmail() != null && !usuario.getEmail().equals(usuarioDto.getEmail()) &&
                usuarioRepository.existsByEmail(usuarioDto.getEmail())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un usuario con el email: " + usuarioDto.getEmail());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Verificar si ya existe otro usuario con el mismo número de empleado
        if (usuarioDto.getNumeroEmpleado() != null && !usuario.getNumeroEmpleado().equals(usuarioDto.getNumeroEmpleado()) &&
                usuarioRepository.existsByNumeroEmpleado(usuarioDto.getNumeroEmpleado())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un usuario con el número de empleado: " + usuarioDto.getNumeroEmpleado());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Verificar si existe el rol
        if (usuarioDto.getRolId() != null) {
            Optional<Rol> rolOptional = rolRepository.findById(usuarioDto.getRolId());
            if (rolOptional.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Rol no encontrado con ID: " + usuarioDto.getRolId());
                response.put("tipo", "ERROR");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            usuario.setRol(rolOptional.get());
        }

        // Actualizar el usuario
        if (usuarioDto.getNombre() != null) usuario.setNombre(usuarioDto.getNombre());
        if (usuarioDto.getApellido() != null) usuario.setApellido(usuarioDto.getApellido());
        if (usuarioDto.getNumeroEmpleado() != null) usuario.setNumeroEmpleado(usuarioDto.getNumeroEmpleado());
        if (usuarioDto.getArea() != null) usuario.setArea(usuarioDto.getArea());
        if (usuarioDto.getStatus() != null) usuario.setStatus(usuarioDto.getStatus());
        if (usuarioDto.getEmail() != null) usuario.setEmail(usuarioDto.getEmail());
        // Actualizar contraseña si se proporciona
        if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", usuarioActualizado);
        response.put("mensaje", "Usuario actualizado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> cambiarEstadoUsuario(Integer id, Boolean status) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();
        usuario.setStatus(status);

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        String mensaje = status ? "Usuario activado exitosamente" : "Usuario desactivado exitosamente";

        Map<String, Object> response = new HashMap<>();
        response.put("datos", usuarioActualizado);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> eliminarUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        usuarioRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Usuario eliminado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

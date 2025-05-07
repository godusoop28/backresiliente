package com.resiliente.utils;

import com.resiliente.model.Rol;
import com.resiliente.model.Usuario;
import com.resiliente.repository.RolRepository;
import com.resiliente.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RolRepository rolRepository, UsuarioRepository usuarioRepository) {
        return args -> {
            // Crear los roles
            Rol adminRole = new Rol("ADMIN");
            Rol empleadoRole = new Rol("EMPLEADO");

            if (rolRepository.findByNombre("ADMIN").isEmpty()) {
                rolRepository.saveAndFlush(adminRole);
            }
            if (rolRepository.findByNombre("EMPLEADO").isEmpty()) {
                rolRepository.saveAndFlush(empleadoRole);
            }

            // Crear el usuario Admin
            if (usuarioRepository.findByEmail("admin@resiliente.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellido("Resiliente");
                admin.setRol(rolRepository.findByNombre("ADMIN").orElse(adminRole));
                admin.setNumeroEmpleado(1);
                admin.setArea("Administración");
                admin.setEmail("admin@resiliente.com");
                usuarioRepository.saveAndFlush(admin);
            }

            // Crear el usuario Empleado
            if (usuarioRepository.findByEmail("empleado@resiliente.com").isEmpty()) {
                Usuario empleado = new Usuario();
                empleado.setNombre("Empleado");
                empleado.setApellido("Resiliente");
                empleado.setRol(rolRepository.findByNombre("EMPLEADO").orElse(empleadoRole));
                empleado.setNumeroEmpleado(2);
                empleado.setArea("Operaciones");
                empleado.setEmail("empleado@resiliente.com");
                usuarioRepository.saveAndFlush(empleado);
            }
        };
    }
}

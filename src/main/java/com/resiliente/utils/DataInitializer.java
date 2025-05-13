package com.resiliente.utils;

import com.resiliente.model.*;
import com.resiliente.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            CondicionRepository condicionRepository,
            SenaRepository senaRepository,
            ProductoRepository productoRepository,
            MeseroRepository meseroRepository,
            IndicacionRepository indicacionRepository,
            CandidatoRepository candidatoRepository,
            PublicacionRepository publicacionRepository,
            TallerRepository tallerRepository,
            ProductoTiendaRepository productoTiendaRepository
    ) {
        return args -> {
            // ==================== ROLES ====================
            Rol adminRole = new Rol("ADMIN");
            Rol empleadoRole = new Rol("EMPLEADO");


            if (rolRepository.findByNombre("ADMIN").isEmpty()) {
                rolRepository.saveAndFlush(adminRole);
            } else {
                adminRole = rolRepository.findByNombre("ADMIN").get();
            }

            if (rolRepository.findByNombre("EMPLEADO").isEmpty()) {
                rolRepository.saveAndFlush(empleadoRole);
            } else {
                empleadoRole = rolRepository.findByNombre("EMPLEADO").get();
            }



            // ==================== USUARIOS ====================
            if (usuarioRepository.findByEmail("admin@resiliente.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellido("Resiliente");
                admin.setRol(adminRole);
                admin.setNumeroEmpleado(1001);
                admin.setArea("Administración");
                admin.setEmail("admin@resiliente.com");
                usuarioRepository.saveAndFlush(admin);
            }

            if (usuarioRepository.findByEmail("empleado@resiliente.com").isEmpty()) {
                Usuario empleado = new Usuario();
                empleado.setNombre("Empleado");
                empleado.setApellido("Resiliente");
                empleado.setRol(empleadoRole);
                empleado.setNumeroEmpleado(2001);
                empleado.setArea("Operaciones");
                empleado.setEmail("empleado@resiliente.com");
                usuarioRepository.saveAndFlush(empleado);
            }





            // ==================== CONDICIONES ====================
            Condicion sordomudo = new Condicion();
            sordomudo.setNombre("Sordomudo");
            sordomudo.setDescripcion("Persona con discapacidad auditiva y del habla");

            Condicion discapacidadAuditiva = new Condicion();
            discapacidadAuditiva.setNombre("Discapacidad Auditiva");
            discapacidadAuditiva.setDescripcion("Persona con dificultad para escuchar");

            Condicion discapacidadIntelectual = new Condicion();
            discapacidadIntelectual.setNombre("Discapacidad Intelectual");
            discapacidadIntelectual.setDescripcion("Persona con discapacidad intelectual");

            if (condicionRepository.findByNombre("Sordomudo").isEmpty()) {
                condicionRepository.saveAndFlush(sordomudo);
            } else {
                sordomudo = condicionRepository.findByNombre("Sordomudo").get();
            }

            if (condicionRepository.findByNombre("Discapacidad Auditiva").isEmpty()) {
                condicionRepository.saveAndFlush(discapacidadAuditiva);
            } else {
                discapacidadAuditiva = condicionRepository.findByNombre("Discapacidad Auditiva").get();
            }

            if (condicionRepository.findByNombre("Discapacidad Intelectual").isEmpty()) {
                condicionRepository.saveAndFlush(discapacidadIntelectual);
            } else {
                discapacidadIntelectual = condicionRepository.findByNombre("Discapacidad Intelectual").get();
            }

            // ==================== SEÑAS ====================
            Sena senaCafe = new Sena();
            senaCafe.setNombre("Café");

            Sena senaTe = new Sena();
            senaTe.setNombre("Té");

            Sena senaAgua = new Sena();
            senaAgua.setNombre("Agua");

            Sena senaPastel = new Sena();
            senaPastel.setNombre("Pastel");

            Sena senaSandwich = new Sena();
            senaSandwich.setNombre("Sandwich");

            if (senaRepository.findByNombre("Café").isEmpty()) {
                senaRepository.saveAndFlush(senaCafe);
            } else {
                senaCafe = senaRepository.findByNombre("Café").get();
            }

            if (senaRepository.findByNombre("Té").isEmpty()) {
                senaRepository.saveAndFlush(senaTe);
            } else {
                senaTe = senaRepository.findByNombre("Té").get();
            }

            if (senaRepository.findByNombre("Agua").isEmpty()) {
                senaRepository.saveAndFlush(senaAgua);
            } else {
                senaAgua = senaRepository.findByNombre("Agua").get();
            }

            if (senaRepository.findByNombre("Pastel").isEmpty()) {
                senaRepository.saveAndFlush(senaPastel);
            } else {
                senaPastel = senaRepository.findByNombre("Pastel").get();
            }

            if (senaRepository.findByNombre("Sandwich").isEmpty()) {
                senaRepository.saveAndFlush(senaSandwich);
            } else {
                senaSandwich = senaRepository.findByNombre("Sandwich").get();
            }

            // ==================== PRODUCTOS ====================
            Producto cafeAmericano = new Producto();
            cafeAmericano.setNombre("Café Americano");
            cafeAmericano.setPrecio(new BigDecimal("2.50"));
            cafeAmericano.setDescripcion("Café negro tradicional");
            cafeAmericano.setCategoria("Bebidas calientes");
            cafeAmericano.setCodigo("CAF-001");

            Producto cafeConLeche = new Producto();
            cafeConLeche.setNombre("Café con Leche");
            cafeConLeche.setPrecio(new BigDecimal("3.00"));
            cafeConLeche.setDescripcion("Café con leche cremosa");
            cafeConLeche.setCategoria("Bebidas calientes");
            cafeConLeche.setCodigo("CAF-002");

            Producto teVerde = new Producto();
            teVerde.setNombre("Té Verde");
            teVerde.setPrecio(new BigDecimal("2.00"));
            teVerde.setDescripcion("Té verde natural");
            teVerde.setCategoria("Bebidas calientes");
            teVerde.setCodigo("TE-001");

            Producto pastelChocolate = new Producto();
            pastelChocolate.setNombre("Pastel de Chocolate");
            pastelChocolate.setPrecio(new BigDecimal("4.50"));
            pastelChocolate.setDescripcion("Delicioso pastel de chocolate");
            pastelChocolate.setCategoria("Postres");
            pastelChocolate.setCodigo("POS-001");

            Producto sandwichJamon = new Producto();
            sandwichJamon.setNombre("Sandwich de Jamón y Queso");
            sandwichJamon.setPrecio(new BigDecimal("5.00"));
            sandwichJamon.setDescripcion("Sandwich con jamón y queso");
            sandwichJamon.setCategoria("Alimentos");
            sandwichJamon.setCodigo("ALI-001");

            if (!productoRepository.existsByCodigo("CAF-001")) {
                productoRepository.saveAndFlush(cafeAmericano);
            } else {
                cafeAmericano = productoRepository.findByCodigo("CAF-001").get();
            }

            if (!productoRepository.existsByCodigo("CAF-002")) {
                productoRepository.saveAndFlush(cafeConLeche);
            } else {
                cafeConLeche = productoRepository.findByCodigo("CAF-002").get();
            }

            if (!productoRepository.existsByCodigo("TE-001")) {
                productoRepository.saveAndFlush(teVerde);
            } else {
                teVerde = productoRepository.findByCodigo("TE-001").get();
            }

            if (!productoRepository.existsByCodigo("POS-001")) {
                productoRepository.saveAndFlush(pastelChocolate);
            } else {
                pastelChocolate = productoRepository.findByCodigo("POS-001").get();
            }

            if (!productoRepository.existsByCodigo("ALI-001")) {
                productoRepository.saveAndFlush(sandwichJamon);
            } else {
                sandwichJamon = productoRepository.findByCodigo("ALI-001").get();
            }

            // ==================== MESEROS ====================
            Mesero mesero1 = new Mesero();
            mesero1.setNombre("Carlos Rodríguez");
            mesero1.setPresentacion("Mesero con experiencia en atención al cliente");
            mesero1.setCondicion(sordomudo);
            mesero1.setEdad(25);

            Mesero mesero2 = new Mesero();
            mesero2.setNombre("Ana García");
            mesero2.setPresentacion("Mesera con 3 años de experiencia");
            mesero2.setCondicion(discapacidadAuditiva);
            mesero2.setEdad(28);

            Mesero mesero3 = new Mesero();
            mesero3.setNombre("Luis Martínez");
            mesero3.setPresentacion("Mesero especializado en atención personalizada");
            mesero3.setCondicion(discapacidadIntelectual);
            mesero3.setEdad(30);

            Mesero mesero4 = new Mesero();
            mesero4.setNombre("María López");
            mesero4.setPresentacion("Mesera con gran capacidad de aprendizaje");
            mesero4.setCondicion(discapacidadIntelectual);
            mesero4.setEdad(22);

            if (meseroRepository.findByNombre("Carlos Rodríguez").isEmpty()) {
                meseroRepository.saveAndFlush(mesero1);
            }

            if (meseroRepository.findByNombre("Ana García").isEmpty()) {
                meseroRepository.saveAndFlush(mesero2);
            }

            if (meseroRepository.findByNombre("Luis Martínez").isEmpty()) {
                meseroRepository.saveAndFlush(mesero3);
            }

            if (meseroRepository.findByNombre("María López").isEmpty()) {
                meseroRepository.saveAndFlush(mesero4);
            }



            // ==================== CANDIDATOS ====================
            if (candidatoRepository.findByEmail("juan.perez@ejemplo.com").isEmpty()) {
                Candidato candidato1 = new Candidato();
                candidato1.setNombre("Juan Pérez");
                candidato1.setEmail("juan.perez@ejemplo.com");
                candidato1.setTelefono("555-123-4567");
                candidato1.setFechaEnvio(LocalDateTime.now().minusDays(5));
                candidatoRepository.saveAndFlush(candidato1);
            }

            if (candidatoRepository.findByEmail("maria.lopez@ejemplo.com").isEmpty()) {
                Candidato candidato2 = new Candidato();
                candidato2.setNombre("María López");
                candidato2.setEmail("maria.lopez@ejemplo.com");
                candidato2.setTelefono("555-987-6543");
                candidato2.setFechaEnvio(LocalDateTime.now().minusDays(3));
                candidatoRepository.saveAndFlush(candidato2);
            }

            if (candidatoRepository.findByEmail("roberto.sanchez@ejemplo.com").isEmpty()) {
                Candidato candidato3 = new Candidato();
                candidato3.setNombre("Roberto Sánchez");
                candidato3.setEmail("roberto.sanchez@ejemplo.com");
                candidato3.setTelefono("555-456-7890");
                candidato3.setFechaEnvio(LocalDateTime.now().minusDays(1));
                candidatoRepository.saveAndFlush(candidato3);
            }

            // ==================== PUBLICACIONES ====================
            if (publicacionRepository.findByTitulo("Inauguración de Café Inclusivo").isEmpty()) {
                Publicacion publicacion1 = new Publicacion();
                publicacion1.setTitulo("Inauguración de Café Inclusivo");
                publicacion1.setContenido("Nos complace anunciar la inauguración de nuestro Café Inclusivo, un espacio diseñado para todos...");
                publicacion1.setFechaPublicacion(LocalDateTime.now().minusDays(30));
                publicacionRepository.saveAndFlush(publicacion1);
            }

            if (publicacionRepository.findByTitulo("Nuevo Taller de Lenguaje de Señas").isEmpty()) {
                Publicacion publicacion2 = new Publicacion();
                publicacion2.setTitulo("Nuevo Taller de Lenguaje de Señas");
                publicacion2.setContenido("Estamos emocionados de anunciar nuestro nuevo taller de lenguaje de señas que comenzará el próximo mes...");
                publicacion2.setFechaPublicacion(LocalDateTime.now().minusDays(15));
                publicacionRepository.saveAndFlush(publicacion2);
            }

            if (publicacionRepository.findByTitulo("Contratación de Personal con Discapacidad").isEmpty()) {
                Publicacion publicacion3 = new Publicacion();
                publicacion3.setTitulo("Contratación de Personal con Discapacidad");
                publicacion3.setContenido("En Café Inclusivo estamos comprometidos con la inclusión laboral. Buscamos personal con discapacidad...");
                publicacion3.setFechaPublicacion(LocalDateTime.now().minusDays(7));
                publicacionRepository.saveAndFlush(publicacion3);
            }

            // ==================== TALLERES ====================
            if (tallerRepository.findByNombre("Taller de Lenguaje de Señas Básico").isEmpty()) {
                Taller taller1 = new Taller();
                taller1.setNombre("Taller de Lenguaje de Señas Básico");
                taller1.setDescripcion("Aprende los fundamentos del lenguaje de señas");
                taller1.setFechaInicio(LocalDateTime.now().plusDays(15).withHour(10).withMinute(0));
                taller1.setFechaFin(LocalDateTime.now().plusDays(15).withHour(13).withMinute(0));
                tallerRepository.saveAndFlush(taller1);
            }

            if (tallerRepository.findByNombre("Taller de Sensibilización sobre Discapacidad").isEmpty()) {
                Taller taller2 = new Taller();
                taller2.setNombre("Taller de Sensibilización sobre Discapacidad");
                taller2.setDescripcion("Aprende sobre las diferentes discapacidades y cómo interactuar adecuadamente");
                taller2.setFechaInicio(LocalDateTime.now().plusDays(30).withHour(15).withMinute(0));
                taller2.setFechaFin(LocalDateTime.now().plusDays(30).withHour(18).withMinute(0));
                tallerRepository.saveAndFlush(taller2);
            }

            if (tallerRepository.findByNombre("Taller de Barismo para Personas con Discapacidad").isEmpty()) {
                Taller taller3 = new Taller();
                taller3.setNombre("Taller de Barismo para Personas con Discapacidad");
                taller3.setDescripcion("Aprende el arte de preparar café adaptado para personas con discapacidad");
                taller3.setFechaInicio(LocalDateTime.now().plusDays(45).withHour(9).withMinute(0));
                taller3.setFechaFin(LocalDateTime.now().plusDays(45).withHour(14).withMinute(0));
                tallerRepository.saveAndFlush(taller3);
            }

            // ==================== PRODUCTOS TIENDA ====================

                ProductoTienda producto1 = new ProductoTienda();
                producto1.setNombre("Taza Café Inclusivo");
                producto1.setDescripcion("Taza con el logo de Café Inclusivo");
                producto1.setPrecio(new BigDecimal("12.99"));
                producto1.setCategoria("Merchandising");

                producto1.setDescuento(new BigDecimal("0.00"));

                producto1.setFechaCreacion(LocalDateTime.now());
                producto1.setCaracteristicas("Taza de cerámica, 350ml, apta para microondas y lavavajillas");

                productoTiendaRepository.saveAndFlush(producto1);



               ProductoTienda producto2 = new ProductoTienda();
                producto2.setNombre("Libro de Lenguaje de Señas");
                producto2.setDescripcion("Guía completa de lenguaje de señas");
                producto2.setPrecio(new BigDecimal("24.99"));
                producto2.setCategoria("Libros");

                producto2.setDescuento(new BigDecimal("5.00"));

                producto2.setFechaCreacion(LocalDateTime.now());
                producto2.setCaracteristicas("200 páginas, tapa blanda, incluye ilustraciones a color");

                productoTiendaRepository.saveAndFlush(producto2);



              ProductoTienda producto3 = new ProductoTienda();
                producto3.setNombre("Café en Grano Premium");
                producto3.setDescripcion("Café en grano de alta calidad");
                producto3.setPrecio(new BigDecimal("18.50"));
                producto3.setCategoria("Café");

                producto3.setDescuento(new BigDecimal("0.00"));

                producto3.setFechaCreacion(LocalDateTime.now());
                producto3.setCaracteristicas("500g, tueste medio, origen Colombia");

                productoTiendaRepository.saveAndFlush(producto3);


              ProductoTienda producto4 = new ProductoTienda();
                producto4.setNombre("Camiseta Café Inclusivo");
                producto4.setDescripcion("Camiseta con el logo de Café Inclusivo");
                producto4.setPrecio(new BigDecimal("19.99"));
                producto4.setCategoria("Ropa");

                producto4.setDescuento(new BigDecimal("0.00"));

                producto4.setFechaCreacion(LocalDateTime.now());
                producto4.setCaracteristicas("100% algodón, disponible en tallas S, M, L, XL");

                productoTiendaRepository.saveAndFlush(producto4);


              ProductoTienda producto5 = new ProductoTienda();
                producto5.setNombre("Tarjeta Regalo");
                producto5.setDescripcion("Tarjeta regalo para usar en Café Inclusivo");
                producto5.setPrecio(new BigDecimal("25.00"));
                producto5.setCategoria("Regalos");

                producto5.setDescuento(new BigDecimal("0.00"));

                producto5.setFechaCreacion(LocalDateTime.now());
                producto5.setCaracteristicas("Tarjeta regalo de $25, válida por 1 año");

                productoTiendaRepository.saveAndFlush(producto5);

        };
    }
}
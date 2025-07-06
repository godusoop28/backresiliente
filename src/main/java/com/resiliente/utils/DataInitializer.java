package com.resiliente.utils;

import com.resiliente.model.*;
import com.resiliente.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
            PublicacionRepository publicacionRepository,
            TallerRepository tallerRepository,
            ProductoTiendaRepository productoTiendaRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            try {
            // ==================== ROLES ====================
            Rol adminRole = rolRepository.findByNombre("ADMIN")
                    .orElseGet(() -> rolRepository.save(new Rol("ADMIN")));

            Rol empleadoRole = rolRepository.findByNombre("EMPLEADO")
                    .orElseGet(() -> rolRepository.save(new Rol("EMPLEADO")));

            // ==================== USUARIOS ====================
            String adminEmail = "direccion@proyectoresiliente.org";
            if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Marco");
                admin.setApellido("Santos");
                admin.setRol(adminRole);
                admin.setNumeroEmpleado(1001);
                admin.setArea("Administración");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setStatus(true);
                usuarioRepository.save(admin);
            }

            String empleadoEmail = "empleado@resiliente.com";
            if (usuarioRepository.findByEmail(empleadoEmail).isEmpty()) {
                Usuario empleado = new Usuario();
                empleado.setNombre("Empleado");
                empleado.setApellido("Resiliente");
                empleado.setRol(empleadoRole);
                empleado.setNumeroEmpleado(2001);
                empleado.setArea("Operaciones");
                empleado.setEmail(empleadoEmail);
                empleado.setPassword(passwordEncoder.encode("empleado123"));
                empleado.setStatus(true);
                usuarioRepository.save(empleado);
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
            senaCafe.setVideo("https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=400&h=300&fit=crop");

            Sena senaTe = new Sena();
            senaTe.setNombre("Té");
            senaTe.setVideo("https://images.unsplash.com/photo-1544787219-7f47ccb76574?w=400&h=300&fit=crop");

            Sena senaAgua = new Sena();
            senaAgua.setNombre("Agua");
            senaAgua.setVideo("https://images.unsplash.com/photo-1548839140-29a749e1cf4d?w=400&h=300&fit=crop");

            Sena senaPastel = new Sena();
            senaPastel.setNombre("Pastel");
            senaPastel.setVideo("https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400&h=300&fit=crop");

            Sena senaSandwich = new Sena();
            senaSandwich.setNombre("Sandwich");
            senaSandwich.setVideo("https://images.unsplash.com/photo-1553909489-cd47e0ef937f?w=400&h=300&fit=crop");

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
            cafeAmericano.setFoto("https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=400&h=300&fit=crop");

            Producto cafeConLeche = new Producto();
            cafeConLeche.setNombre("Café con Leche");
            cafeConLeche.setPrecio(new BigDecimal("3.00"));
            cafeConLeche.setDescripcion("Café con leche cremosa");
            cafeConLeche.setCategoria("Bebidas calientes");
            cafeConLeche.setCodigo("CAF-002");
            cafeConLeche.setFoto("https://images.unsplash.com/photo-1461023058943-07fcbe16d735?w=400&h=300&fit=crop");

            Producto teVerde = new Producto();
            teVerde.setNombre("Té Verde");
            teVerde.setPrecio(new BigDecimal("2.00"));
            teVerde.setDescripcion("Té verde natural");
            teVerde.setCategoria("Bebidas calientes");
            teVerde.setCodigo("TE-001");
            teVerde.setFoto("https://images.unsplash.com/photo-1544787219-7f47ccb76574?w=400&h=300&fit=crop");

            Producto pastelChocolate = new Producto();
            pastelChocolate.setNombre("Pastel de Chocolate");
            pastelChocolate.setPrecio(new BigDecimal("4.50"));
            pastelChocolate.setDescripcion("Delicioso pastel de chocolate");
            pastelChocolate.setCategoria("Postres");
            pastelChocolate.setCodigo("POS-001");
            pastelChocolate.setFoto("https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400&h=300&fit=crop");

            Producto sandwichJamon = new Producto();
            sandwichJamon.setNombre("Sandwich de Jamón y Queso");
            sandwichJamon.setPrecio(new BigDecimal("5.00"));
            sandwichJamon.setDescripcion("Sandwich con jamón y queso");
            sandwichJamon.setCategoria("Alimentos");
            sandwichJamon.setCodigo("ALI-001");
            sandwichJamon.setFoto("https://images.unsplash.com/photo-1553909489-cd47e0ef937f?w=400&h=300&fit=crop");

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
            mesero1.setStatus(true);
            mesero1.setFoto("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=400&fit=crop&crop=face");

            Mesero mesero2 = new Mesero();
            mesero2.setNombre("Ana García");
            mesero2.setPresentacion("Mesera con 3 años de experiencia");
            mesero2.setCondicion(discapacidadAuditiva);
            mesero2.setEdad(28);
            mesero2.setStatus(true);
            mesero2.setFoto("https://images.unsplash.com/photo-1494790108755-2616b612b786?w=400&h=400&fit=crop&crop=face");

            Mesero mesero3 = new Mesero();
            mesero3.setNombre("Luis Martínez");
            mesero3.setPresentacion("Mesero especializado en atención personalizada");
            mesero3.setCondicion(discapacidadIntelectual);
            mesero3.setEdad(30);
            mesero3.setStatus(true);
            mesero3.setFoto("https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400&h=400&fit=crop&crop=face");

            Mesero mesero4 = new Mesero();
            mesero4.setNombre("María López");
            mesero4.setPresentacion("Mesera con gran capacidad de aprendizaje");
            mesero4.setCondicion(discapacidadIntelectual);
            mesero4.setEdad(22);
            mesero4.setStatus(true);
            mesero4.setFoto("https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400&h=400&fit=crop&crop=face");

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

            // ==================== PUBLICACIONES ====================
            if (publicacionRepository.findByTitulo("Inauguración de Café Inclusivo").isEmpty()) {
                Publicacion publicacion1 = new Publicacion();
                publicacion1.setTitulo("Inauguración de Café Inclusivo");
                publicacion1.setContenido("Nos complace anunciar la inauguración de nuestro Café Inclusivo, un espacio diseñado para todos. Este proyecto representa nuestro compromiso con la inclusión social y laboral, ofreciendo oportunidades de empleo para personas con discapacidad. Nuestro café no solo sirve bebidas de alta calidad, sino que también es un lugar donde la diversidad se celebra y se valora. Cada taza de café que servimos lleva consigo una historia de superación y esperanza.");
                publicacion1.setFechaPublicacion(LocalDateTime.now().minusDays(30));
                publicacion1.setImagen("https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=800&h=600&fit=crop");
                publicacionRepository.saveAndFlush(publicacion1);
            }
            if (publicacionRepository.findByTitulo("Nuevo Taller de Lenguaje de Señas").isEmpty()) {
                Publicacion publicacion2 = new Publicacion();
                publicacion2.setTitulo("Nuevo Taller de Lenguaje de Señas");
                publicacion2.setContenido("Estamos emocionados de anunciar nuestro nuevo taller de lenguaje de señas que comenzará el próximo mes. Este taller está diseñado para personas que desean aprender a comunicarse con la comunidad sorda y con discapacidad auditiva. A través de sesiones prácticas y dinámicas, los participantes aprenderán los fundamentos del lenguaje de señas, desde el alfabeto básico hasta conversaciones cotidianas. Nuestro objetivo es crear puentes de comunicación y fomentar una sociedad más inclusiva.");
                publicacion2.setFechaPublicacion(LocalDateTime.now().minusDays(15));
                publicacion2.setImagen("https://images.unsplash.com/photo-1559027615-cd4628902d4a?w=800&h=600&fit=crop");
                publicacionRepository.saveAndFlush(publicacion2);
            }
            if (publicacionRepository.findByTitulo("Contratación de Personal con Discapacidad").isEmpty()) {
                Publicacion publicacion3 = new Publicacion();
                publicacion3.setTitulo("Contratación de Personal con Discapacidad");
                publicacion3.setContenido("En Café Inclusivo estamos comprometidos con la inclusión laboral. Buscamos personal con discapacidad para unirse a nuestro equipo de trabajo. Creemos firmemente que la diversidad enriquece nuestro ambiente laboral y nos permite ofrecer un mejor servicio a nuestra comunidad. Ofrecemos capacitación especializada, un ambiente de trabajo adaptado y oportunidades de crecimiento profesional. Si tienes ganas de trabajar y formar parte de un proyecto que transforma vidas, te invitamos a postularte.");
                publicacion3.setFechaPublicacion(LocalDateTime.now().minusDays(7));
                publicacion3.setImagen("https://images.unsplash.com/photo-1521737604893-d14cc237f11d?w=800&h=600&fit=crop");
                publicacionRepository.saveAndFlush(publicacion3);
            }

            // ==================== TALLERES ====================
            if (tallerRepository.findByNombre("Taller de Lenguaje de Señas Básico").isEmpty()) {
                Taller taller1 = new Taller();
                taller1.setNombre("Taller de Lenguaje de Señas Básico");
                taller1.setDescripcion("Aprende los fundamentos del lenguaje de señas");
                taller1.setFechaInicio(LocalDateTime.now().plusDays(15).withHour(10).withMinute(0));
                taller1.setFechaFin(LocalDateTime.now().plusDays(15).withHour(13).withMinute(0));
                taller1.setImagen("https://images.unsplash.com/photo-1559027615-cd4628902d4a?w=800&h=600&fit=crop");
                tallerRepository.saveAndFlush(taller1);
            }
            if (tallerRepository.findByNombre("Taller de Sensibilización sobre Discapacidad").isEmpty()) {
                Taller taller2 = new Taller();
                taller2.setNombre("Taller de Sensibilización sobre Discapacidad");
                taller2.setDescripcion("Aprende sobre las diferentes discapacidades y cómo interactuar adecuadamente");
                taller2.setFechaInicio(LocalDateTime.now().plusDays(30).withHour(15).withMinute(0));
                taller2.setFechaFin(LocalDateTime.now().plusDays(30).withHour(18).withMinute(0));
                taller2.setImagen("https://images.unsplash.com/photo-1521737604893-d14cc237f11d?w=800&h=600&fit=crop");
                tallerRepository.saveAndFlush(taller2);
            }
            if (tallerRepository.findByNombre("Taller de Barismo para Personas con Discapacidad").isEmpty()) {
                Taller taller3 = new Taller();
                taller3.setNombre("Taller de Barismo para Personas con Discapacidad");
                taller3.setDescripcion("Aprende el arte de preparar café adaptado para personas con discapacidad");
                taller3.setFechaInicio(LocalDateTime.now().plusDays(45).withHour(9).withMinute(0));
                taller3.setFechaFin(LocalDateTime.now().plusDays(45).withHour(14).withMinute(0));
                taller3.setImagen("https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=800&h=600&fit=crop");
                tallerRepository.saveAndFlush(taller3);
            }

            // ==================== PRODUCTOS TIENDA ====================
            if (productoTiendaRepository.findByNombreContaining("Taza Café Inclusivo").isEmpty()) {
                ProductoTienda producto1 = new ProductoTienda();
                producto1.setNombre("Taza Café Inclusivo");
                producto1.setDescripcion("Taza con el logo de Café Inclusivo");
                producto1.setPrecio(new BigDecimal("12.99"));
                producto1.setCategoria("Merchandising");
                producto1.setDescuento(new BigDecimal("0.00"));
                producto1.setFechaCreacion(LocalDateTime.now());
                producto1.setCaracteristicas("Taza de cerámica, 350ml, apta para microondas y lavavajillas");
                producto1.setImagen("https://images.unsplash.com/photo-1514228742587-6b1558fcf93a?w=400&h=400&fit=crop");
                productoTiendaRepository.saveAndFlush(producto1);
            }

            if (productoTiendaRepository.findByNombreContaining("Libro de Lenguaje de Señas").isEmpty()) {
                ProductoTienda producto2 = new ProductoTienda();
                producto2.setNombre("Libro de Lenguaje de Señas");
                producto2.setDescripcion("Guía completa de lenguaje de señas");
                producto2.setPrecio(new BigDecimal("24.99"));
                producto2.setCategoria("Libros");
                producto2.setDescuento(new BigDecimal("5.00"));
                producto2.setFechaCreacion(LocalDateTime.now());
                producto2.setCaracteristicas("200 páginas, tapa blanda, incluye ilustraciones a color");
                producto2.setImagen("https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=400&h=400&fit=crop");
                productoTiendaRepository.saveAndFlush(producto2);
            }

            if (productoTiendaRepository.findByNombreContaining("Café en Grano Premium").isEmpty()) {
                ProductoTienda producto3 = new ProductoTienda();
                producto3.setNombre("Café en Grano Premium");
                producto3.setDescripcion("Café en grano de alta calidad");
                producto3.setPrecio(new BigDecimal("18.50"));
                producto3.setCategoria("Café");
                producto3.setDescuento(new BigDecimal("0.00"));
                producto3.setFechaCreacion(LocalDateTime.now());
                producto3.setCaracteristicas("500g, tueste medio, origen Colombia");
                producto3.setImagen("https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=400&h=400&fit=crop");
                productoTiendaRepository.saveAndFlush(producto3);
            }

            if (productoTiendaRepository.findByNombreContaining("Camiseta Café Inclusivo").isEmpty()) {
                ProductoTienda producto4 = new ProductoTienda();
                producto4.setNombre("Camiseta Café Inclusivo");
                producto4.setDescripcion("Camiseta con el logo de Café Inclusivo");
                producto4.setPrecio(new BigDecimal("19.99"));
                producto4.setCategoria("Ropa");
                producto4.setDescuento(new BigDecimal("0.00"));
                producto4.setFechaCreacion(LocalDateTime.now());
                producto4.setCaracteristicas("100% algodón, disponible en tallas S, M, L, XL");
                producto4.setImagen("https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=400&fit=crop");
                productoTiendaRepository.saveAndFlush(producto4);
            }

            if (productoTiendaRepository.findByNombreContaining("Tarjeta Regalo").isEmpty()) {
                ProductoTienda producto5 = new ProductoTienda();
                producto5.setNombre("Tarjeta Regalo");
                producto5.setDescripcion("Tarjeta regalo para usar en Café Inclusivo");
                producto5.setPrecio(new BigDecimal("25.00"));
                producto5.setCategoria("Regalos");
                producto5.setDescuento(new BigDecimal("0.00"));
                producto5.setFechaCreacion(LocalDateTime.now());
                producto5.setCaracteristicas("Tarjeta regalo de $25, válida por 1 año");
                producto5.setImagen("https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400&h=400&fit=crop");
                productoTiendaRepository.saveAndFlush(producto5);
            }
            } catch (Exception e) {
                System.err.println("Error durante la inicialización de datos: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
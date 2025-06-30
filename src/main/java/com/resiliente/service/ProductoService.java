package com.resiliente.service;

import com.resiliente.dto.ProductoDto;
import com.resiliente.model.Producto;
import com.resiliente.model.Sena;
import com.resiliente.repository.ProductoRepository;
import com.resiliente.repository.SenaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final SenaRepository senaRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository, SenaRepository senaRepository) {
        this.productoRepository = productoRepository;
        this.senaRepository = senaRepository;
    }

    @Transactional
    public ResponseEntity<Object> crearProducto(ProductoDto productoDto) {
        try {
            System.out.println("=== SERVICIO: CREANDO PRODUCTO ===");
            System.out.println("DTO recibido: " + productoDto.toString());

            // ✅ MEJORADO: Validaciones más específicas
            if (productoDto.getNombre() == null || productoDto.getNombre().trim().isEmpty()) {
                return crearRespuestaError("El nombre del producto es obligatorio", HttpStatus.BAD_REQUEST);
            }

            if (productoDto.getPrecio() == null || productoDto.getPrecio().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                return crearRespuestaError("El precio debe ser mayor que cero", HttpStatus.BAD_REQUEST);
            }

            if (productoDto.getDescripcion() == null || productoDto.getDescripcion().trim().isEmpty()) {
                return crearRespuestaError("La descripción es obligatoria", HttpStatus.BAD_REQUEST);
            }

            if (productoDto.getCodigo() == null || productoDto.getCodigo().trim().isEmpty()) {
                return crearRespuestaError("El código es obligatorio", HttpStatus.BAD_REQUEST);
            }

            // Verificar si ya existe un producto con el mismo código
            if (productoRepository.existsByCodigo(productoDto.getCodigo())) {
                return crearRespuestaError("Ya existe un producto con el código: " + productoDto.getCodigo(), HttpStatus.BAD_REQUEST);
            }

            // Verificar si la seña existe (si se proporciona)
            Sena sena = null;
            if (productoDto.getIdSena() != null) {
                Optional<Sena> senaOptional = senaRepository.findById(productoDto.getIdSena());
                if (senaOptional.isEmpty()) {
                    return crearRespuestaError("Seña no encontrada con ID: " + productoDto.getIdSena(), HttpStatus.BAD_REQUEST);
                }
                sena = senaOptional.get();
            }

            // Crear y guardar el nuevo producto
            Producto producto = new Producto();
            producto.setNombre(productoDto.getNombre().trim());
            producto.setPrecio(productoDto.getPrecio());
            producto.setDescripcion(productoDto.getDescripcion().trim());
            producto.setFoto(productoDto.getFoto());
            producto.setCategoria(productoDto.getCategoria() != null ? productoDto.getCategoria().trim() : null);
            producto.setCodigo(productoDto.getCodigo().trim());
            producto.setSena(sena);
            producto.setStatus(true);

            System.out.println("Producto a guardar: " + producto.toString());

            Producto productoGuardado = productoRepository.save(producto);

            System.out.println("✅ Producto guardado exitosamente con ID: " + productoGuardado.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("datos", productoGuardado);
            response.put("mensaje", "Producto creado exitosamente");
            response.put("tipo", "SUCCESS");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            System.err.println("❌ Error al crear producto: " + e.getMessage());
            e.printStackTrace();
            return crearRespuestaError("Error interno del servidor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<Object> actualizarProducto(Integer id, ProductoDto productoDto) {
        try {
            System.out.println("=== SERVICIO: ACTUALIZANDO PRODUCTO ===");
            System.out.println("ID: " + id);
            System.out.println("DTO recibido: " + productoDto.toString());

            Optional<Producto> productoOptional = productoRepository.findById(id);

            if (productoOptional.isEmpty()) {
                return crearRespuestaError("Producto no encontrado con ID: " + id, HttpStatus.NOT_FOUND);
            }

            Producto producto = productoOptional.get();
            System.out.println("Producto encontrado: " + producto.toString());

            // ✅ MEJORADO: Validaciones más específicas
            if (productoDto.getNombre() != null && !productoDto.getNombre().trim().isEmpty()) {
                producto.setNombre(productoDto.getNombre().trim());
            }

            if (productoDto.getPrecio() != null && productoDto.getPrecio().compareTo(java.math.BigDecimal.ZERO) > 0) {
                producto.setPrecio(productoDto.getPrecio());
            }

            if (productoDto.getDescripcion() != null && !productoDto.getDescripcion().trim().isEmpty()) {
                producto.setDescripcion(productoDto.getDescripcion().trim());
            }

            if (productoDto.getFoto() != null) {
                producto.setFoto(productoDto.getFoto());
            }

            if (productoDto.getCategoria() != null) {
                producto.setCategoria(productoDto.getCategoria().trim());
            }

            // Verificar código único si se está cambiando
            if (productoDto.getCodigo() != null && !producto.getCodigo().equals(productoDto.getCodigo()) &&
                    productoRepository.existsByCodigo(productoDto.getCodigo())) {
                return crearRespuestaError("Ya existe un producto con el código: " + productoDto.getCodigo(), HttpStatus.BAD_REQUEST);
            }

            if (productoDto.getCodigo() != null && !productoDto.getCodigo().trim().isEmpty()) {
                producto.setCodigo(productoDto.getCodigo().trim());
            }

            if (productoDto.getStatus() != null) {
                producto.setStatus(productoDto.getStatus());
            }

            // Verificar si la seña existe (si se proporciona)
            if (productoDto.getIdSena() != null) {
                Optional<Sena> senaOptional = senaRepository.findById(productoDto.getIdSena());
                if (senaOptional.isEmpty()) {
                    return crearRespuestaError("Seña no encontrada con ID: " + productoDto.getIdSena(), HttpStatus.BAD_REQUEST);
                }
                producto.setSena(senaOptional.get());
            }

            System.out.println("Producto a actualizar: " + producto.toString());

            Producto productoActualizado = productoRepository.save(producto);

            System.out.println("✅ Producto actualizado exitosamente");

            Map<String, Object> response = new HashMap<>();
            response.put("datos", productoActualizado);
            response.put("mensaje", "Producto actualizado exitosamente");
            response.put("tipo", "SUCCESS");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("❌ Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return crearRespuestaError("Error interno del servidor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ NUEVO: Método helper para crear respuestas de error
    private ResponseEntity<Object> crearRespuestaError(String mensaje, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", mensaje);
        response.put("tipo", "ERROR");
        return new ResponseEntity<>(response, status);
    }

    // ... resto de métodos sin cambios ...
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerProductoPorId(Integer id) {
        Optional<Producto> productoOptional = productoRepository.findById(id);

        if (productoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productoOptional.get());
        response.put("mensaje", "Producto encontrado");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerTodosLosProductos() {
        List<Producto> productos = productoRepository.findAll();

        if (productos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay productos registrados");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productos);
        response.put("mensaje", "Lista de productos");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerProductosPorEstado(Boolean status) {
        List<Producto> productos = productoRepository.findByStatus(status);

        if (productos.isEmpty()) {
            String mensaje = status ? "No hay productos activos" : "No hay productos inactivos";
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", mensaje);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        String mensaje = status ? "Lista de productos activos" : "Lista de productos inactivos";
        Map<String, Object> response = new HashMap<>();
        response.put("datos", productos);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerProductosPorSena(Integer idSena) {
        if (!senaRepository.existsById(idSena)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Seña no encontrada con ID: " + idSena);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        List<Producto> productos = productoRepository.findBySenaId(idSena);

        if (productos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay productos asociados a esta seña");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productos);
        response.put("mensaje", "Lista de productos por seña");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> asignarSenaAProducto(Integer idProducto, Integer idSena) {
        Optional<Producto> productoOptional = productoRepository.findById(idProducto);
        if (productoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + idProducto);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Optional<Sena> senaOptional = senaRepository.findById(idSena);
        if (senaOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Seña no encontrada con ID: " + idSena);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Producto producto = productoOptional.get();
        producto.setSena(senaOptional.get());
        Producto productoActualizado = productoRepository.save(producto);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productoActualizado);
        response.put("mensaje", "Seña asignada al producto exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> removerSenaDeProducto(Integer idProducto) {
        Optional<Producto> productoOptional = productoRepository.findById(idProducto);
        if (productoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + idProducto);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Producto producto = productoOptional.get();
        producto.setSena(null);
        Producto productoActualizado = productoRepository.save(producto);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productoActualizado);
        response.put("mensaje", "Seña removida del producto exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> cambiarEstadoProducto(Integer id, Boolean status) {
        Optional<Producto> productoOptional = productoRepository.findById(id);

        if (productoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Producto producto = productoOptional.get();
        producto.setStatus(status);

        Producto productoActualizado = productoRepository.save(producto);

        String mensaje = status ? "Producto activado exitosamente" : "Producto desactivado exitosamente";

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productoActualizado);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> eliminarProducto(Integer id) {
        if (!productoRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        productoRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Producto eliminado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerProductosActivos() {
        List<Producto> productos = productoRepository.findByStatus(true);

        if (productos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay productos activos");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productos);
        response.put("mensaje", "Lista de productos activos");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerProductosInactivos() {
        List<Producto> productos = productoRepository.findByStatus(false);

        if (productos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay productos inactivos");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productos);
        response.put("mensaje", "Lista de productos inactivos");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

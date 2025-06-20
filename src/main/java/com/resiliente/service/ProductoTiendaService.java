package com.resiliente.service;

import com.resiliente.dto.ProductoTiendaDto;
import com.resiliente.model.ProductoTienda;
import com.resiliente.repository.ProductoTiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductoTiendaService {

    private final ProductoTiendaRepository productoTiendaRepository;

    @Autowired
    public ProductoTiendaService(ProductoTiendaRepository productoTiendaRepository) {
        this.productoTiendaRepository = productoTiendaRepository;
    }

    @Transactional
    public ResponseEntity<Object> crearProductoTienda(ProductoTiendaDto productoTiendaDto) {
        // Verificar si ya existe un producto con el mismo SKU

        // Crear y guardar el nuevo producto
        ProductoTienda productoTienda = new ProductoTienda();
        productoTienda.setNombre(productoTiendaDto.getNombre());
        productoTienda.setDescripcion(productoTiendaDto.getDescripcion());
        productoTienda.setPrecio(productoTiendaDto.getPrecio());
        productoTienda.setCategoria(productoTiendaDto.getCategoria());

        productoTienda.setImagen(productoTiendaDto.getImagen());

        productoTienda.setDescuento(productoTiendaDto.getDescuento());

        productoTienda.setFechaCreacion(LocalDateTime.now());
        productoTienda.setStatus(true); // Por defecto, el producto está activo
        productoTienda.setCaracteristicas(productoTiendaDto.getCaracteristicas());

        ProductoTienda productoGuardado = productoTiendaRepository.save(productoTienda);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productoGuardado);
        response.put("mensaje", "Producto creado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerProductoTiendaPorId(Integer id) {
        Optional<ProductoTienda> productoOptional = productoTiendaRepository.findById(id);

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
    public ResponseEntity<Object> obtenerTodosLosProductosTienda() {
        List<ProductoTienda> productos = productoTiendaRepository.findAll();

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
    public ResponseEntity<Object> obtenerProductosTiendaPorEstado(Boolean status) {
        List<ProductoTienda> productos = productoTiendaRepository.findByStatus(status);

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
    public ResponseEntity<Object> obtenerProductosTiendaPorCategoria(String categoria) {
        List<ProductoTienda> productos = productoTiendaRepository.findByCategoria(categoria);

        if (productos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay productos en la categoría: " + categoria);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productos);
        response.put("mensaje", "Lista de productos en la categoría: " + categoria);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @Transactional(readOnly = true)
    public ResponseEntity<Object> buscarProductosTiendaPorNombre(String nombre) {
        List<ProductoTienda> productos = productoTiendaRepository.findByNombreContaining(nombre);

        if (productos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No se encontraron productos con el nombre: " + nombre);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productos);
        response.put("mensaje", "Resultados de la búsqueda para: " + nombre);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerProductosTiendaPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        List<ProductoTienda> productos = productoTiendaRepository.findByPrecioBetween(precioMin, precioMax);

        if (productos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No se encontraron productos en el rango de precio especificado");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productos);
        response.put("mensaje", "Productos en el rango de precio: " + precioMin + " - " + precioMax);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarProductoTienda(Integer id, ProductoTiendaDto productoTiendaDto) {
        Optional<ProductoTienda> productoOptional = productoTiendaRepository.findById(id);

        if (productoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        ProductoTienda producto = productoOptional.get();



        // Actualizar el producto
        if (productoTiendaDto.getNombre() != null) producto.setNombre(productoTiendaDto.getNombre());
        if (productoTiendaDto.getDescripcion() != null) producto.setDescripcion(productoTiendaDto.getDescripcion());
        if (productoTiendaDto.getPrecio() != null) producto.setPrecio(productoTiendaDto.getPrecio());
        if (productoTiendaDto.getCategoria() != null) producto.setCategoria(productoTiendaDto.getCategoria());

        if (productoTiendaDto.getImagen() != null) producto.setImagen(productoTiendaDto.getImagen());

        if (productoTiendaDto.getDescuento() != null) producto.setDescuento(productoTiendaDto.getDescuento());

       if (productoTiendaDto.getStatus() != null) producto.setStatus(productoTiendaDto.getStatus());
        if (productoTiendaDto.getCaracteristicas() != null) producto.setCaracteristicas(productoTiendaDto.getCaracteristicas());

        // Actualizar fecha de actualización


        ProductoTienda productoActualizado = productoTiendaRepository.save(producto);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productoActualizado);
        response.put("mensaje", "Producto actualizado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> actualizarStockProductoTienda(Integer id, Integer stock) {
        Optional<ProductoTienda> productoOptional = productoTiendaRepository.findById(id);

        if (productoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        ProductoTienda producto = productoOptional.get();


        ProductoTienda productoActualizado = productoTiendaRepository.save(producto);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productoActualizado);
        response.put("mensaje", "Stock actualizado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> cambiarEstadoProductoTienda(Integer id, Boolean status) {
        Optional<ProductoTienda> productoOptional = productoTiendaRepository.findById(id);

        if (productoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        ProductoTienda producto = productoOptional.get();
        producto.setStatus(status);


        ProductoTienda productoActualizado = productoTiendaRepository.save(producto);

        String mensaje = status ? "Producto activado exitosamente" : "Producto desactivado exitosamente";

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productoActualizado);
        response.put("mensaje", mensaje);
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @Transactional
    public ResponseEntity<Object> eliminarProductoTienda(Integer id) {
        if (!productoTiendaRepository.existsById(id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        productoTiendaRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Producto eliminado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerProductosTiendaActivos() {
        List<ProductoTienda> productos = productoTiendaRepository.findByStatus(true);

        if (productos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay productos de tienda activos");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productos);
        response.put("mensaje", "Lista de productos de tienda activos");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> obtenerProductosTiendaInactivos() {
        List<ProductoTienda> productos = productoTiendaRepository.findByStatus(false);

        if (productos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No hay productos de tienda inactivos");
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productos);
        response.put("mensaje", "Lista de productos de tienda inactivos");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
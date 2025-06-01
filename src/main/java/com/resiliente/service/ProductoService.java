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
        // Verificar si ya existe un producto con el mismo código
        if (productoRepository.existsByCodigo(productoDto.getCodigo())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un producto con el código: " + productoDto.getCodigo());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Verificar si la seña existe (si se proporciona)
        Sena sena = null;
        if (productoDto.getIdSena() != null) {
            Optional<Sena> senaOptional = senaRepository.findById(productoDto.getIdSena());
            if (senaOptional.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Seña no encontrada con ID: " + productoDto.getIdSena());
                response.put("tipo", "ERROR");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            sena = senaOptional.get();
        }

        // Crear y guardar el nuevo producto
        Producto producto = new Producto();
        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setFoto(productoDto.getFoto());
        producto.setCategoria(productoDto.getCategoria());
        producto.setCodigo(productoDto.getCodigo());
        producto.setSena(sena);
        producto.setStatus(true);

        Producto productoGuardado = productoRepository.save(producto);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productoGuardado);
        response.put("mensaje", "Producto creado exitosamente");
        response.put("tipo", "SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

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
        // Verificar si la seña existe
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
    public ResponseEntity<Object> actualizarProducto(Integer id, ProductoDto productoDto) {
        Optional<Producto> productoOptional = productoRepository.findById(id);

        if (productoOptional.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Producto no encontrado con ID: " + id);
            response.put("tipo", "WARNING");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Producto producto = productoOptional.get();

        // Verificar si ya existe otro producto con el mismo código
        if (productoDto.getCodigo() != null && !producto.getCodigo().equals(productoDto.getCodigo()) &&
                productoRepository.existsByCodigo(productoDto.getCodigo())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Ya existe un producto con el código: " + productoDto.getCodigo());
            response.put("tipo", "ERROR");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Verificar si la seña existe (si se proporciona)
        if (productoDto.getIdSena() != null) {
            Optional<Sena> senaOptional = senaRepository.findById(productoDto.getIdSena());
            if (senaOptional.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Seña no encontrada con ID: " + productoDto.getIdSena());
                response.put("tipo", "ERROR");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            producto.setSena(senaOptional.get());
        }

        // Actualizar el producto
        if (productoDto.getNombre() != null) producto.setNombre(productoDto.getNombre());
        if (productoDto.getPrecio() != null) producto.setPrecio(productoDto.getPrecio());
        if (productoDto.getDescripcion() != null) producto.setDescripcion(productoDto.getDescripcion());
        if (productoDto.getFoto() != null) producto.setFoto(productoDto.getFoto());
        if (productoDto.getCodigo() != null) producto.setCodigo(productoDto.getCodigo());
        if (productoDto.getCategoria() != null) producto.setCategoria(productoDto.getCategoria());
        if (productoDto.getStatus() != null) producto.setStatus(productoDto.getStatus());

        Producto productoActualizado = productoRepository.save(producto);

        Map<String, Object> response = new HashMap<>();
        response.put("datos", productoActualizado);
        response.put("mensaje", "Producto actualizado exitosamente");
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
}
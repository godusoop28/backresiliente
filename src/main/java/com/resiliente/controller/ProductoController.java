package com.resiliente.controller;

import com.resiliente.dto.ProductoDto;
import com.resiliente.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<Object> crearProducto(@RequestBody ProductoDto productoDto) {
        return productoService.crearProducto(productoDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerProductoPorId(@PathVariable Integer id) {
        return productoService.obtenerProductoPorId(id);
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodosLosProductos() {
        return productoService.obtenerTodosLosProductos();
    }

    @GetMapping("/estado/{status}")
    public ResponseEntity<Object> obtenerProductosPorEstado(@PathVariable Boolean status) {
        return productoService.obtenerProductosPorEstado(status);
    }

    @GetMapping("/sena/{idSena}")
    public ResponseEntity<Object> obtenerProductosPorSena(@PathVariable Integer idSena) {
        return productoService.obtenerProductosPorSena(idSena);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarProducto(@PathVariable Integer id, @RequestBody ProductoDto productoDto) {
        return productoService.actualizarProducto(id, productoDto);
    }

    @PatchMapping("/{id}/estado/{status}")
    public ResponseEntity<Object> cambiarEstadoProducto(@PathVariable Integer id, @PathVariable Boolean status) {
        return productoService.cambiarEstadoProducto(id, status);
    }

    @PatchMapping("/{idProducto}/sena/{idSena}")
    public ResponseEntity<Object> asignarSenaAProducto(@PathVariable Integer idProducto, @PathVariable Integer idSena) {
        return productoService.asignarSenaAProducto(idProducto, idSena);
    }

    @DeleteMapping("/{idProducto}/sena")
    public ResponseEntity<Object> removerSenaDeProducto(@PathVariable Integer idProducto) {
        return productoService.removerSenaDeProducto(idProducto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarProducto(@PathVariable Integer id) {
        return productoService.eliminarProducto(id);
    }
    @GetMapping("/activos")
    public ResponseEntity<Object> obtenerProductosActivos() {
        return productoService.obtenerProductosActivos();
    }

    @GetMapping("/inactivos")
    public ResponseEntity<Object> obtenerProductosInactivos() {
        return productoService.obtenerProductosInactivos();
    }


}

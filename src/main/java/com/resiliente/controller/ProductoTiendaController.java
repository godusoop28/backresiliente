package com.resiliente.controller;

import com.resiliente.dto.ProductoTiendaDto;
import com.resiliente.service.ProductoTiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/productos-tienda")
@CrossOrigin(origins = "*")
public class ProductoTiendaController {

    private final ProductoTiendaService productoTiendaService;

    @Autowired
    public ProductoTiendaController(ProductoTiendaService productoTiendaService) {
        this.productoTiendaService = productoTiendaService;
    }

    @PostMapping
    public ResponseEntity<Object> crearProductoTienda(@RequestBody ProductoTiendaDto productoTiendaDto) {
        return productoTiendaService.crearProductoTienda(productoTiendaDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerProductoTiendaPorId(@PathVariable Integer id) {
        return productoTiendaService.obtenerProductoTiendaPorId(id);
    }

    @GetMapping
    public ResponseEntity<Object> obtenerTodosLosProductosTienda() {
        return productoTiendaService.obtenerTodosLosProductosTienda();
    }

    @GetMapping("/estado/{status}")
    public ResponseEntity<Object> obtenerProductosTiendaPorEstado(@PathVariable Boolean status) {
        return productoTiendaService.obtenerProductosTiendaPorEstado(status);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<Object> obtenerProductosTiendaPorCategoria(@PathVariable String categoria) {
        return productoTiendaService.obtenerProductosTiendaPorCategoria(categoria);
    }



    @GetMapping("/buscar")
    public ResponseEntity<Object> buscarProductosTiendaPorNombre(@RequestParam String nombre) {
        return productoTiendaService.buscarProductosTiendaPorNombre(nombre);
    }

    @GetMapping("/precio")
    public ResponseEntity<Object> obtenerProductosTiendaPorRangoPrecio(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return productoTiendaService.obtenerProductosTiendaPorRangoPrecio(min, max);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarProductoTienda(
            @PathVariable Integer id,
            @RequestBody ProductoTiendaDto productoTiendaDto) {
        return productoTiendaService.actualizarProductoTienda(id, productoTiendaDto);
    }

    @PatchMapping("/{id}/stock/{stock}")
    public ResponseEntity<Object> actualizarStockProductoTienda(
            @PathVariable Integer id,
            @PathVariable Integer stock) {
        return productoTiendaService.actualizarStockProductoTienda(id, stock);
    }

    @PatchMapping("/{id}/estado/{status}")
    public ResponseEntity<Object> cambiarEstadoProductoTienda(
            @PathVariable Integer id,
            @PathVariable Boolean status) {
        return productoTiendaService.cambiarEstadoProductoTienda(id, status);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarProductoTienda(@PathVariable Integer id) {
        return productoTiendaService.eliminarProductoTienda(id);
    }
    @GetMapping("/activos")
    public ResponseEntity<Object> obtenerProductosTiendaActivos() {
        return productoTiendaService.obtenerProductosTiendaActivos();
    }

    @GetMapping("/inactivos")
    public ResponseEntity<Object> obtenerProductosTiendaInactivos() {
        return productoTiendaService.obtenerProductosTiendaInactivos();
    }


}
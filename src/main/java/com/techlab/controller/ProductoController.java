package com.techlab.controller;

import com.techlab.dto.ProductoRequest;
import com.techlab.dto.ProductoResponse;
import com.techlab.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // GET /api/productos            (lista todos, o busca por nombre con ?nombre=)
    @GetMapping
    public List<ProductoResponse> listar(@RequestParam(required = false) String nombre) {
        var productos = (nombre != null && !nombre.isBlank())
                ? productoService.buscarPorNombre(nombre)
                : productoService.listar();
        return productos.stream().map(ProductoResponse::desde).toList();
    }

    // GET /api/productos/stock-bajo (alerta de stock minimo)
    @GetMapping("/stock-bajo")
    public List<ProductoResponse> stockBajo() {
        return productoService.conStockBajo().stream().map(ProductoResponse::desde).toList();
    }

    // GET /api/productos/{id}
    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable Long id) {
        return ProductoResponse.desde(productoService.obtenerPorId(id));
    }

    // POST /api/productos
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        var creado = ProductoResponse.desde(productoService.crear(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/productos/{id}
    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable Long id,
                                       @Valid @RequestBody ProductoRequest request) {
        return ProductoResponse.desde(productoService.actualizar(id, request));
    }

    // DELETE /api/productos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

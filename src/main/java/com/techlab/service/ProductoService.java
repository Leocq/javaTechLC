package com.techlab.service;

import com.techlab.dto.ProductoRequest;
import com.techlab.entity.Categoria;
import com.techlab.entity.Producto;
import com.techlab.exception.RecursoNoEncontradoException;
import com.techlab.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private static final int UMBRAL_STOCK_MINIMO = 5;

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    public ProductoService(ProductoRepository productoRepository, CategoriaService categoriaService) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id " + id));
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> conStockBajo() {
        return productoRepository.findByStockLessThan(UMBRAL_STOCK_MINIMO);
    }

    public Producto crear(ProductoRequest request) {
        Categoria categoria = categoriaService.obtenerPorId(request.getCategoriaId());
        Producto producto = new Producto(
                request.getNombre(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getStock(),
                request.getImagenUrl(),
                categoria);
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, ProductoRequest request) {
        Producto producto = obtenerPorId(id);
        Categoria categoria = categoriaService.obtenerPorId(request.getCategoriaId());
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        productoRepository.delete(producto);
    }
}

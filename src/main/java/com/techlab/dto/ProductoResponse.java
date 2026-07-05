package com.techlab.dto;

import com.techlab.entity.Producto;

public class ProductoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String imagenUrl;
    private Long categoriaId;
    private String categoriaNombre;

    public static ProductoResponse desde(Producto p) {
        ProductoResponse r = new ProductoResponse();
        r.id = p.getId();
        r.nombre = p.getNombre();
        r.descripcion = p.getDescripcion();
        r.precio = p.getPrecio();
        r.stock = p.getStock();
        r.imagenUrl = p.getImagenUrl();
        if (p.getCategoria() != null) {
            r.categoriaId = p.getCategoria().getId();
            r.categoriaNombre = p.getCategoria().getNombre();
        }
        return r;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }
}

package com.techlab.productos;

import java.util.ArrayList;

public class ProductoService {

    private ArrayList<Producto> productos = new ArrayList<>();

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        System.out.println("Producto agregado correctamente.");
    }

    public void listarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (Producto producto : productos) {
            producto.mostrarInformacion();
            System.out.println("--------------------");
        }
    }

    public Producto buscarPorId(int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    public boolean eliminarProducto(int id) {
        Producto producto = buscarPorId(id);

        if (producto != null) {
            productos.remove(producto);
            return true;
        }

        return false;
    }

    public boolean actualizarPrecio(int id, double nuevoPrecio) {
        Producto producto = buscarPorId(id);

        if (producto != null && nuevoPrecio >= 0) {
            producto.setPrecio(nuevoPrecio);
            return true;
        }

        return false;
    }

    public boolean actualizarStock(int id, int nuevoStock) {
        Producto producto = buscarPorId(id);

        if (producto != null && nuevoStock >= 0) {
            producto.setStock(nuevoStock);
            return true;
        }

        return false;
    }
}
package com.techlab.pedidos;

import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.productos.Producto;

import java.util.ArrayList;
import java.util.List;

public class PedidoService {

    private List<Pedido> pedidos = new ArrayList<>();

    public void agregarLineaAPedido(Pedido pedido, Producto producto, int cantidad)
            throws StockInsuficienteException {

        if (cantidad <= 0) {
            throw new StockInsuficienteException("La cantidad debe ser mayor a cero.");
        }

        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException(
                    "Stock insuficiente para '" + producto.getNombre() + "'. "
                            + "Disponible: " + producto.getStock() + ", solicitado: " + cantidad + ".");
        }

        pedido.agregarLinea(new LineaPedido(producto, cantidad));
        producto.setStock(producto.getStock() - cantidad);
    }

    public void registrarPedido(Pedido pedido) {
        pedidos.add(pedido);
        System.out.println("Pedido #" + pedido.getId() + " creado correctamente. "
                + "Total: $" + pedido.calcularTotal());
    }

    public void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }

        for (Pedido pedido : pedidos) {
            pedido.mostrarInformacion();
            System.out.println("--------------------");
        }
    }
}

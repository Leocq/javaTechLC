package com.techlab.dto;

import com.techlab.entity.LineaPedido;
import com.techlab.entity.Pedido;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {

    private Long id;
    private Long usuarioId;
    private LocalDateTime fecha;
    private String estado;
    private double total;
    private List<ItemResponse> items;

    public static PedidoResponse desde(Pedido pedido) {
        PedidoResponse r = new PedidoResponse();
        r.id = pedido.getId();
        r.usuarioId = pedido.getUsuario() != null ? pedido.getUsuario().getId() : null;
        r.fecha = pedido.getFecha();
        r.estado = pedido.getEstado().name();
        r.total = pedido.getTotal();
        r.items = pedido.getLineas().stream().map(ItemResponse::desde).toList();
        return r;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public double getTotal() {
        return total;
    }

    public List<ItemResponse> getItems() {
        return items;
    }

    public static class ItemResponse {
        private Long productoId;
        private String nombreProducto;
        private int cantidad;
        private double precioUnitario;
        private double subtotal;

        public static ItemResponse desde(LineaPedido linea) {
            ItemResponse i = new ItemResponse();
            i.productoId = linea.getProducto() != null ? linea.getProducto().getId() : null;
            i.nombreProducto = linea.getProducto() != null ? linea.getProducto().getNombre() : null;
            i.cantidad = linea.getCantidad();
            i.precioUnitario = linea.getPrecioUnitario();
            i.subtotal = linea.getSubtotal();
            return i;
        }

        public Long getProductoId() {
            return productoId;
        }

        public String getNombreProducto() {
            return nombreProducto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public double getPrecioUnitario() {
            return precioUnitario;
        }

        public double getSubtotal() {
            return subtotal;
        }
    }
}

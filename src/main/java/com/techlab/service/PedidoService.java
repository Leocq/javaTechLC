package com.techlab.service;

import com.techlab.dto.PedidoRequest;
import com.techlab.entity.EstadoPedido;
import com.techlab.entity.LineaPedido;
import com.techlab.entity.Pedido;
import com.techlab.entity.Producto;
import com.techlab.entity.Usuario;
import com.techlab.exception.RecursoNoEncontradoException;
import com.techlab.exception.StockInsuficienteException;
import com.techlab.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProductoService productoService,
                         UsuarioService usuarioService) {
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Pedido no encontrado con id " + id));
    }

    public List<Pedido> historialDeUsuario(Long usuarioId) {
        usuarioService.obtenerPorId(usuarioId); // valida que el usuario exista (404 si no)
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Crea un pedido: valida stock de cada item, descuenta stock, calcula total
     * y lo deja en estado PENDIENTE. Si algun item no tiene stock suficiente,
     * lanza StockInsuficienteException (que el handler traduce a HTTP 400).
     */
    @Transactional
    public Pedido crear(PedidoRequest request) {
        Usuario usuario = usuarioService.obtenerPorId(request.getUsuarioId());

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);

        for (PedidoRequest.ItemPedidoRequest item : request.getItemsPedido()) {
            Producto producto = productoService.obtenerPorId(item.getProductoId());

            if (producto.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para '" + producto.getNombre() + "'. "
                                + "Disponible: " + producto.getStock()
                                + ", solicitado: " + item.getCantidad() + ".");
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            pedido.agregarLinea(new LineaPedido(producto, item.getCantidad()));
        }

        pedido.calcularTotal();
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}

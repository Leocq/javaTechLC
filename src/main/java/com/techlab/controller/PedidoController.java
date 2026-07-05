package com.techlab.controller;

import com.techlab.dto.PedidoRequest;
import com.techlab.dto.PedidoResponse;
import com.techlab.entity.EstadoPedido;
import com.techlab.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // GET /api/pedidos
    @GetMapping
    public List<PedidoResponse> listar() {
        return pedidoService.listar().stream().map(PedidoResponse::desde).toList();
    }

    // GET /api/pedidos/{id}
    @GetMapping("/{id}")
    public PedidoResponse obtener(@PathVariable Long id) {
        return PedidoResponse.desde(pedidoService.obtenerPorId(id));
    }

    // POST /api/pedidos
    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest request) {
        var creado = PedidoResponse.desde(pedidoService.crear(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PATCH /api/pedidos/{id}/estado?valor=CONFIRMADO
    @PatchMapping("/{id}/estado")
    public PedidoResponse cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido valor) {
        return PedidoResponse.desde(pedidoService.cambiarEstado(id, valor));
    }
}

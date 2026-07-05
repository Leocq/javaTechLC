package com.techlab.controller;

import com.techlab.dto.PedidoResponse;
import com.techlab.entity.Usuario;
import com.techlab.service.PedidoService;
import com.techlab.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;

    public UsuarioController(UsuarioService usuarioService, PedidoService pedidoService) {
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(usuario));
    }

    // GET /api/usuarios/{id}/pedidos  -> historial de pedidos del usuario
    @GetMapping("/{id}/pedidos")
    public List<PedidoResponse> pedidosDeUsuario(@PathVariable Long id) {
        return pedidoService.historialDeUsuario(id).stream().map(PedidoResponse::desde).toList();
    }
}

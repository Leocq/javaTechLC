package com.techlab.service;

import com.techlab.entity.Usuario;
import com.techlab.exception.RecursoNoEncontradoException;
import com.techlab.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id " + id));
    }

    public Usuario crear(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}

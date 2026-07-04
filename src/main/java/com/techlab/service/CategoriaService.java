package com.techlab.service;

import com.techlab.dto.CategoriaRequest;
import com.techlab.entity.Categoria;
import com.techlab.exception.RecursoNoEncontradoException;
import com.techlab.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoria no encontrada con id " + id));
    }

    public Categoria crear(CategoriaRequest request) {
        return categoriaRepository.save(new Categoria(request.getNombre()));
    }

    public Categoria actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = obtenerPorId(id);
        categoria.setNombre(request.getNombre());
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        Categoria categoria = obtenerPorId(id);
        categoriaRepository.delete(categoria);
    }
}

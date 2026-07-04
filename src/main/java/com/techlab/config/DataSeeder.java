package com.techlab.config;

import com.techlab.entity.Categoria;
import com.techlab.entity.Producto;
import com.techlab.entity.Usuario;
import com.techlab.repository.CategoriaRepository;
import com.techlab.repository.ProductoRepository;
import com.techlab.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Carga datos de ejemplo al arrancar si la base esta vacia,
 * asi el frontend tiene productos, categorias y un usuario para probar.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public DataSeeder(CategoriaRepository categoriaRepository,
                      ProductoRepository productoRepository,
                      UsuarioRepository usuarioRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() > 0) {
            return;
        }

        Categoria bebidas = categoriaRepository.save(new Categoria("Bebidas"));
        Categoria comidas = categoriaRepository.save(new Categoria("Comidas"));
        Categoria limpieza = categoriaRepository.save(new Categoria("Limpieza"));

        productoRepository.save(new Producto("Cafe Premium", "Cafe colombiano tostado",
                12.50, 100, "http://ejemplo.com/cafe.jpg", bebidas));
        productoRepository.save(new Producto("Agua Mineral", "Botella 500ml",
                3.00, 200, "http://ejemplo.com/agua.jpg", bebidas));
        productoRepository.save(new Producto("Galletas", "Paquete de galletas dulces",
                5.20, 80, "http://ejemplo.com/galletas.jpg", comidas));
        productoRepository.save(new Producto("Chocolate", "Barra de chocolate amargo",
                8.90, 3, "http://ejemplo.com/chocolate.jpg", comidas));

        usuarioRepository.save(new Usuario("Cliente Demo", "demo@techlab.com"));
    }
}

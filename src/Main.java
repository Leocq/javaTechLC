import com.techlab.productos.Producto;
import com.techlab.productos.ProductoService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ProductoService productoService = new ProductoService();

        int opcion;

        do {
            System.out.println("=====================================");
            System.out.println("SISTEMA DE GESTIÓN - TECHLAB");
            System.out.println("=====================================");
            System.out.println("1) Agregar producto");
            System.out.println("2) Listar productos");
            System.out.println("3) Buscar producto");
            System.out.println("4) Eliminar producto");
            System.out.println("5) Actualizar producto");
            System.out.println("6) Salir");
            System.out.print("Elija una opción: ");

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.print("Nombre: ");
                    scanner.nextLine();
                    String nombre = scanner.nextLine();

                    System.out.print("Precio: ");
                    double precio = scanner.nextDouble();

                    System.out.print("Stock: ");
                    int stock = scanner.nextInt();

                    Producto producto = new Producto(nombre, precio, stock);
                    productoService.agregarProducto(producto);
                    break;

                case 2:
                    productoService.listarProductos();
                    break;

                case 3:
                    System.out.print("Ingrese ID: ");
                    int idBuscar = scanner.nextInt();

                    Producto encontrado = productoService.buscarPorId(idBuscar);

                    if (encontrado != null) {
                        encontrado.mostrarInformacion();
                    } else {
                        System.out.println("Producto no encontrado.");
                    }
                    break;

                case 4:
                    System.out.print("Ingrese ID a eliminar: ");
                    int idEliminar = scanner.nextInt();

                    if (productoService.eliminarProducto(idEliminar)) {
                        System.out.println("Producto eliminado.");
                    } else {
                        System.out.println("No se encontró el producto.");
                    }
                    break;

                case 5:
                    System.out.print("Ingrese ID: ");
                    int idActualizar = scanner.nextInt();

                    System.out.println("1) Actualizar precio");
                    System.out.println("2) Actualizar stock");
                    int opcionUpdate = scanner.nextInt();

                    if (opcionUpdate == 1) {
                        System.out.print("Nuevo precio: ");
                        double nuevoPrecio = scanner.nextDouble();
                        productoService.actualizarPrecio(idActualizar, nuevoPrecio);
                    } else if (opcionUpdate == 2) {
                        System.out.print("Nuevo stock: ");
                        int nuevoStock = scanner.nextInt();
                        productoService.actualizarStock(idActualizar, nuevoStock);
                    }
                    break;

                case 6:
                    System.out.println("Saliendo...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 6);

        scanner.close();
    }
}
import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.pedidos.Pedido;
import com.techlab.pedidos.PedidoService;
import com.techlab.productos.Bebida;
import com.techlab.productos.Comida;
import com.techlab.productos.Producto;
import com.techlab.productos.ProductoService;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        ProductoService productoService = new ProductoService();
        PedidoService pedidoService = new PedidoService();

        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Elija una opción: ");

            switch (opcion) {
                case 1:
                    agregarProducto(productoService);
                    break;
                case 2:
                    productoService.listarProductos();
                    break;
                case 3:
                    buscarActualizarProducto(productoService);
                    break;
                case 4:
                    eliminarProducto(productoService);
                    break;
                case 5:
                    crearPedido(productoService, pedidoService);
                    break;
                case 6:
                    pedidoService.listarPedidos();
                    break;
                case 7:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 7);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("=====================================");
        System.out.println("SISTEMA DE GESTIÓN - TECHLAB");
        System.out.println("=====================================");
        System.out.println("1) Agregar producto");
        System.out.println("2) Listar productos");
        System.out.println("3) Buscar/Actualizar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Crear un pedido");
        System.out.println("6) Listar pedidos");
        System.out.println("7) Salir");
    }

    private static void agregarProducto(ProductoService productoService) {
        System.out.println("Tipo de producto:");
        System.out.println("1) Producto genérico");
        System.out.println("2) Bebida");
        System.out.println("3) Comida");
        int tipo = leerEntero("Elija el tipo: ");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        double precio = leerDouble("Precio: ");
        int stock = leerEntero("Stock: ");

        Producto producto;
        switch (tipo) {
            case 2:
                double volumen = leerDouble("Volumen (litros): ");
                producto = new Bebida(nombre, precio, stock, volumen);
                break;
            case 3:
                System.out.print("Fecha de vencimiento: ");
                String fecha = scanner.nextLine();
                producto = new Comida(nombre, precio, stock, fecha);
                break;
            default:
                producto = new Producto(nombre, precio, stock);
        }

        productoService.agregarProducto(producto);
    }

    private static void buscarActualizarProducto(ProductoService productoService) {
        System.out.println("Buscar por:");
        System.out.println("1) ID");
        System.out.println("2) Nombre");
        int criterio = leerEntero("Elija una opción: ");

        Producto encontrado;
        if (criterio == 2) {
            System.out.print("Ingrese nombre: ");
            String nombre = scanner.nextLine();
            encontrado = productoService.buscarPorNombre(nombre);
        } else {
            int id = leerEntero("Ingrese ID: ");
            encontrado = productoService.buscarPorId(id);
        }

        if (encontrado == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.println("Producto encontrado:");
        encontrado.mostrarInformacion();

        System.out.println("¿Desea actualizar algún dato?");
        System.out.println("1) Actualizar precio");
        System.out.println("2) Actualizar stock");
        System.out.println("3) No actualizar");
        int opcionUpdate = leerEntero("Elija una opción: ");

        if (opcionUpdate == 1) {
            double nuevoPrecio = leerDouble("Nuevo precio: ");
            if (productoService.actualizarPrecio(encontrado.getId(), nuevoPrecio)) {
                System.out.println("Precio actualizado.");
            } else {
                System.out.println("El precio no puede ser negativo.");
            }
        } else if (opcionUpdate == 2) {
            int nuevoStock = leerEntero("Nuevo stock: ");
            if (productoService.actualizarStock(encontrado.getId(), nuevoStock)) {
                System.out.println("Stock actualizado.");
            } else {
                System.out.println("El stock no puede ser negativo.");
            }
        }
    }

    private static void eliminarProducto(ProductoService productoService) {
        int id = leerEntero("Ingrese ID a eliminar: ");

        System.out.print("¿Confirma la eliminación? (s/n): ");
        String confirmacion = scanner.nextLine();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Eliminación cancelada.");
            return;
        }

        if (productoService.eliminarProducto(id)) {
            System.out.println("Producto eliminado.");
        } else {
            System.out.println("No se encontró el producto.");
        }
    }

    private static void crearPedido(ProductoService productoService, PedidoService pedidoService) {
        int cantidadProductos = leerEntero("¿Cuántos productos distintos desea agregar al pedido? ");

        if (cantidadProductos <= 0) {
            System.out.println("Debe agregar al menos un producto.");
            return;
        }

        Pedido pedido = new Pedido();

        for (int i = 0; i < cantidadProductos; i++) {
            int id = leerEntero("ID del producto: ");
            Producto producto = productoService.buscarPorId(id);

            if (producto == null) {
                System.out.println("Producto no encontrado. Se omite esta línea.");
                continue;
            }

            int cantidad = leerEntero("Cantidad: ");

            try {
                pedidoService.agregarLineaAPedido(pedido, producto, cantidad);
                System.out.println("Agregado al pedido.");
            } catch (StockInsuficienteException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        if (pedido.estaVacio()) {
            System.out.println("El pedido no contiene productos. No se registró.");
        } else {
            pedidoService.registrarPedido(pedido);
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            try {
                return Integer.parseInt(entrada.trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número entero.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            try {
                return Double.parseDouble(entrada.trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número.");
            }
        }
    }
}

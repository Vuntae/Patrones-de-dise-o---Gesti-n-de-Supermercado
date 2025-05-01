import java.util.Scanner;

/**
 * Clase main que representa el Supermercado
 * @author 2Esio | Vuntae
 */

public class Supermercado {
    public static void main(String[] args) {
        Almacen almacen = new Almacen();
        Simulacion simulacion = new Simulacion();
         try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Presiona ENTER para iniciar la simulación y presiona q para detenerla");
            scanner.nextLine(); // Espera a que el usuario presione ENTER

            // Crear productos
            almacen.generarProducto("cocacola");
            almacen.generarProducto("Sopa");
            almacen.generarProducto("Pan bimbo");
            almacen.generarProducto("Chocolate");
            almacen.generarProducto("Dulce de Leche");
            almacen.generarProducto("Donas");
            almacen.generarProducto("manzana", 3.4,  5200);

            // Imprimir los productos
            for (Producto producto : almacen.getProductos())
            System.out.println(producto);


            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("");

            

            simulacion.simular();

        System.out.println("Presiona 'q' para detener la simulación...");
        while (true) {
            String input = scanner.nextLine(); // Lee la entrada del usuario
            if (input.equalsIgnoreCase("q")) {
                simulacion.setRunning(false); // Detiene la simulación
                System.out.println("Simulación detenida.");
                break;
            }
        }

        scanner.close(); // Cierra el Scanner
        }
    }
}

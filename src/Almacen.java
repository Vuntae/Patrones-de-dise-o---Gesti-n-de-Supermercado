import java.util.*;

/**
 * Clase que representa el Almacen
 * @author 2Esio | Vuntae
 */

public class Almacen {
    private static List<Producto> productos;
    private Random random;
    private int nextId; // Variable para generar IDs únicos

    public Almacen() {
        Almacen.productos = new ArrayList<>();
        this.random= new Random();
        this.nextId= 12345;
    }

   // Método para generar un producto con solo el nombre
   public void generarProducto(String nombre) {
    generarProducto(nombre, null, null);
}

    // Método para generar un producto con nombre, precio y existencias
    public void generarProducto(String nombre, Double precio, Integer existencias) {
        if (precio == null) {
            precio = 1 + (100 - 1) * random.nextDouble();
        }   
        if (existencias == null) {
            existencias = 10000 + random.nextInt(10000);
        }
        Producto producto = new Producto(nextId++, nombre, precio, existencias);
        productos.add(producto);
}
    // Nétodo para buscar un producto por su ID
    public Producto buscarProductoPorId(int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null; // Devuelve null si no se encuentra el producto
    }

    // Métodos para actualizar las existencias de los productos y para eliminarlos
    public void actualizarExistencias(int id, int existencias) {
        Producto producto = buscarProductoPorId(id);
        if (producto != null) {
            producto.setExistencias(existencias);
        }
    }

    public void eliminarProducto(int id) {
        Producto producto = buscarProductoPorId(id);
        if (producto != null) {
            productos.remove(producto);
        }
    }

    public List<Producto> getProductos() {
        return productos;
    }
}

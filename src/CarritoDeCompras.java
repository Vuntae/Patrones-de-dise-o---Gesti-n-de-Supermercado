import java.util.*;

/**
 * Clase que representa el Carrito de Compras
 * @author 2Esio | Vuntae
 */

public class CarritoDeCompras {
    private Map<Integer, Integer> productos;

    public CarritoDeCompras() {
        productos = new HashMap<>();
    }

    public void agregarProducto(int idProducto, int cantidad) {
        productos.put(idProducto, productos.getOrDefault(idProducto, 0) + cantidad);
    }

    public Map<Integer, Integer> getProductos() {
        return productos;
    }
}
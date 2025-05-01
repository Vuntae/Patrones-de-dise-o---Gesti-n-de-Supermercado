import java.util.List;
import java.util.Random;

/**
 * Clase que representa al Cliente
 * @author 2Esio | Vuntae
 */

public class Cliente {
    private CarritoDeCompras carrito;
    private Random random;
    private int cantidadProductos = 0;
    private boolean fueAtendido; 

    public Cliente() {
        carrito = new CarritoDeCompras(); // Inicializamos un nuevo CarritoDeCompras cuando se crea un Cliente
        random = new Random(); // Inicializamos un nuevo objeto Random para generación de números aleatorios
    }

    public void tomarProducto(Producto producto) {
        int cantidad = random.nextInt(Math.min(20, producto.getExistencias())); // Generamos una cantidad aleatoria entre 0 y la existencias
        // Verifica si se pudo tomar la cantidad especificada del producto
        if (producto.tomarProducto(cantidad)) { 
            carrito.agregarProducto(producto.getId(), cantidad); // Agrega el producto y la cantidad al carrito
            cantidadProductos += cantidad;
        }
    }
    
    public void tomarProductos(Almacen almacen) {
        List<Producto> productos = almacen.getProductos();
        if (!productos.isEmpty()) { // Verifica que la lista de productos no esté vacía
            int numProductos = 1 + random.nextInt(productos.size()); // Genera un número aleatorio de productos para tomar
            for (int i = 0; i < numProductos; i++) {
                int indiceProducto = random.nextInt(productos.size());
                Producto producto = productos.get(indiceProducto);
                tomarProducto(producto);
            }
        }
    }

    public void marcarComoAtendido() {
        this.fueAtendido = true;
    }

    public int getcantidadProductos(){
        return cantidadProductos;
    }
    
    public static Cliente crearCliente(){
        return new Cliente();
    }
    
    public CarritoDeCompras getCarritoDeCompras() {
        return carrito;
    }

    public boolean fueAtendido() {
        return this.fueAtendido;
    }

}

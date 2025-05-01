/**
 * Clase que representa el Producto
 * @author 2Esio | Vuntae
 */

public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private int existencias;
    private boolean siendoTomado;

    public Producto(int id, String nombre, double precio, int existencias) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.existencias = existencias;
        this.siendoTomado= false;
    }
    // Método para tomar un producto haciendo asugurándose de que solo una persona pueda estar tomando el producto
    public synchronized boolean tomarProducto(int cantidad) {
        if (!siendoTomado && existencias > 0) {
            siendoTomado = true;
            existencias-= cantidad;
            siendoTomado = false;
            return true;
        }
        return false;
    }

    //Métodos para obtener los parámetros del producto
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getExistencias() {
        return existencias;
    }

    public boolean getSiendoTomado(){
        return siendoTomado;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }


    // Metodo para obtener un string con los datos del producto
    @Override
    public String toString() {
        return id + ", " + nombre + ", $" + precio + ", " + existencias;
    }
}

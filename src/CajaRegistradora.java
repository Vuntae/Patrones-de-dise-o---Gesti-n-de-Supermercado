import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Clase que representa la caja Registradora
 * @author 2Esio | Vuntae
 */

public abstract class CajaRegistradora extends Thread {
    protected Queue<Cliente> filaClientes; // filaClientes es una cola que almacena los clientes en espera
    protected Cliente clienteActual; // clienteActual es el cliente que está siendo atendido
    protected Map<Integer, Integer> ticketCompra; // ticketCompra es un mapa que almacena el ticket de compra del cliente actual 
    protected Almacen almacen;
    protected Semaphore semaphore; // Añadimos el semaforo para gestionar el acceso a la consola
    private boolean habilitada;
    private int numeroDeCaja;
    private static int numCajas = 0;
    private static final int MAX_CAJAS = 20;

    public Queue<Cliente> getFilaClientes() {
        return filaClientes;
    }

    // Constructor
    protected CajaRegistradora(Semaphore semaphore) {
        this.semaphore = semaphore;
        this.clienteActual = null; // inicializamos la caja sin ningun cliente siendo atendido
        this.ticketCompra = new HashMap<>();
        this.habilitada = true;
        try {
            if (numCajas >= MAX_CAJAS) {
                throw new IllegalStateException("Se ha alcanzado el límite máximo de cajas registradoras.");
            }
            numCajas++;
            this.numeroDeCaja = numCajas; // asignamos el número único de caja a la caja registradora
        } catch (IllegalStateException e) {
            // Manejo de la excepción
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Metodo para recibir un cliente
    public void recibirCliente(Cliente cliente) {
        // Si no hay ningún cliente siendo atendido, el cliente es atendido
        if (clienteActual == null) {
            clienteActual = cliente;
            // cobrarProductos(); (Si se quita de comentario esta linea se cobran los productos de forma secuencial)
        } else {
            // Si ya hay un cliente siendo atendido, el cliente se pone en la fila
            filaClientes.add(cliente);
            // Si el cliente actual ha terminado de ser atendido, atiende al siguiente cliente en la fila
            if (clienteActual.fueAtendido()) {
                Cliente siguienteCliente = filaClientes.poll();
                if (siguienteCliente != null) {
                    clienteActual = siguienteCliente;
                    // cobrarProductos(); (Si se quita de comentario esta linea se cobran los productos de forma secuencial)
                } else {
                    System.out.println("\nYa no hay clientes en la fila de la caja " + getNumeroDeCaja() + "\n");
                }
            }
        }
    }

    // Metodo para cobrar los productos al cliente
    public void cobrarProductos() {
        try {
            semaphore.acquire(); // Adquiere el permiso antes de imprimir
    
        if (clienteActual != null) {
            ticketCompra = clienteActual.getCarritoDeCompras().getProductos();
            double sumaTotal = 0;
            double iva;
            double total;

            // La caja se tarda un tiempo proporcional a la cantidad de objetos que tiene el cliente
            try {
                Thread.sleep(clienteActual.getcantidadProductos() * 10); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // Generar el ticket de compra
            System.out.println("Ticket de Compra de la caja " + numeroDeCaja + ": ");
            System.out.println("-------------------------------------------------------");
            System.out.printf("%-10s %-10s %-15s %-10s %-10s%n", "ID", "Cantidad", "Nombre", "Precio", "Total");
            for (Map.Entry<Integer, Integer> entrada : ticketCompra.entrySet()) {
                Integer idProducto = entrada.getKey();
                Integer cantidad = entrada.getValue();
                Producto producto = almacen.buscarProductoPorId(idProducto);
                if (producto != null) {
                    double precio = producto.getPrecio();
                    double totalProducto = cantidad * precio;
                    sumaTotal += totalProducto;
                    System.out.printf("%-10d %-10d %-15s $%-10.2f $%-10.2f%n", idProducto, cantidad, producto.getNombre(), precio, totalProducto);
                }
                
            }
            iva = sumaTotal * 0.16; // IVA del 16%
            total = sumaTotal + iva;
            System.out.println("-------------------------------------------------------");
            System.out.printf("Subtotal: $%.2f%n", sumaTotal);
            System.out.printf("IVA: $%.2f%n", iva);
            System.out.printf("Total: $%.2f%n", total);
            System.out.println("");

      
            if (filaClientes.poll() != null) {
                clienteActual = filaClientes.poll(); // Atiende al siguiente cliente en la fila
            } else {
                System.out.println("Ya no hay clientes en la fila de la caja " + getNumeroDeCaja());
                System.out.println("");
            }
        }

        } catch (InterruptedException e) {
            e.printStackTrace();
            } finally {
            semaphore.release(); // Libera el permiso después de imprimir
        }

    }
    
    public void cobrarProductosATodosLosClientes() {
        while (!filaClientes.isEmpty()) {
            cobrarProductos();
        }
    }

    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }

    public Cliente getClienteActual() {
        return clienteActual;
    }

    public boolean estaHabilitada() {
        return this.habilitada;
    }

    // Metodo que devuelve el ticket de compra
    public Map<Integer, Integer> getTicketCompra() {
        return ticketCompra;
    }

    public int getNumeroDeCaja(){
        return numeroDeCaja;
    }

    @Override
    public void run() {
        while (!filaClientes.isEmpty()) {
            cobrarProductos();
        }
    }   
}

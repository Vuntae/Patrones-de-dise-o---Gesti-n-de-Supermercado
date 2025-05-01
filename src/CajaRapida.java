import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Clase que representa la Caja Rapida
 * @author 2Esio | Vuntae
 */

public class CajaRapida extends CajaRegistradora{
   
    // Constructor
    public CajaRapida(Almacen almacen, Semaphore semaphore) {
        super(semaphore);
        this.almacen = almacen;
        this.filaClientes = new LinkedList<>();
        
    }

    // Metodo que devuelve el ticket de compra
    public Map<Integer, Integer> getTicketCompra() {
        return ticketCompra;
    }
}

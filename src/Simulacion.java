import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Clase que representa la simulación
 * @author 2Esio | Vuntae
 */

public class Simulacion {
    private Almacen almacen;
    private List<CajaRegistradora> cajas;
    private Random random;
    private ScheduledExecutorService scheduler;
    private Semaphore semaphore;
    private AtomicBoolean running;

    public Simulacion() {
        this.almacen = new Almacen();
        this.cajas = new ArrayList<>();
        this.random = new Random();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.semaphore = new Semaphore(1);
        this.running = new AtomicBoolean(true);
        for (int i = 0; i < 15; i++) {
            if (i < 10) {
                cajas.add(new CajaNormal(almacen, semaphore));
            } else {
                cajas.add(new CajaRapida(almacen, semaphore));
            }
        }
    }

    public void simular() {
        int tiempo = 0;
        ExecutorService executor = Executors.newFixedThreadPool(cajas.size()); // Crea un ExecutorService con un hilo para cada caja
            while (tiempo < 100 && running.get()) {
                

            int numClientes = 1 + random.nextInt(50); // Genera un número aleatorio de clientes para entrar a la tienda
            System.out.println("Llegaron " + numClientes + " Clientes nuevos");
            System.out.println("");
            for (int i = 0; i < numClientes; i++) {
                Cliente cliente = Cliente.crearCliente(); // Genera un nuevo cliente 
                cliente.tomarProductos(almacen); // El cliente toma productos del almacén

                // El cliente elige la caja con menos clientes en su fila
                CajaRegistradora cajaRapidaConMenorFila = cajas.get(0);
                for (CajaRegistradora caja : cajas) {
                    if (caja instanceof CajaRapida && (cajaRapidaConMenorFila == null || caja.getFilaClientes().size() < cajaRapidaConMenorFila.getFilaClientes().size())) {
                        cajaRapidaConMenorFila = caja;
                    }
                }

                // El cliente elige la caja con menos clientes en su fila
                CajaRegistradora cajaNormalConMenorFila = cajas.get(0);
                for (CajaRegistradora caja : cajas) {
                    if (caja instanceof CajaNormal && (cajaNormalConMenorFila == null || caja.getFilaClientes().size() < cajaNormalConMenorFila.getFilaClientes().size())) {
                        cajaNormalConMenorFila = caja;
                    }
                }

                // Si el cliente tiene menos de 20 productos, se forma en una caja rápida
                if (cliente.getcantidadProductos() < 20) {
                    cajaRapidaConMenorFila.recibirCliente(cliente);
                } else {
                    cajaNormalConMenorFila.recibirCliente(cliente);
                }
            }
            for (CajaRegistradora caja : cajas) {
                executor.submit(() -> { // Utiliza executor.submit() en lugar de new Thread().start()
                    int tiempoInterno = 0;
                    while (tiempoInterno < 100 && running.get()) {
                        if (caja.getClienteActual() != null) {
                            caja.cobrarProductos();
                        }
                        tiempoInterno += 2;
                    }
                });
            }
            // Deshabilita una caja aleatoria por un tiempo arbitrario
            CajaRegistradora cajaADeshabilitar = cajas.get(random.nextInt(cajas.size()));
            cajaADeshabilitar.setHabilitada(false);
            System.out.println("Se deshabilita la caja " + cajaADeshabilitar.getNumeroDeCaja() + System.lineSeparator() );
            int tiempoDeshabilitada = 2 + random.nextInt(5); // Tiempo arbitrario entre 2 y 6 segundos
            scheduler.schedule(() -> {
                cajaADeshabilitar.setHabilitada(true);
                System.out.println("Se habilita la caja " + cajaADeshabilitar.getNumeroDeCaja() + System.lineSeparator());
            }, tiempoDeshabilitada, TimeUnit.SECONDS);
    
            try {
                Thread.sleep(2000); // Espera 2 segundos antes de que los siguientes clientes entren a la tienda
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            tiempo += 2;
        }

        executor.shutdown(); // Detiene todos los hilos cuando la simulación ha terminado
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Espera a que todos los hilos terminen
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setRunning(boolean isRunning) {
        this.running.set(isRunning);
    }
}

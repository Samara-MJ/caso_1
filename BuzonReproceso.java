//Nombres: Daniela Torres Turriago,
import java.util.LinkedList; 
import java.util.Queue;

// Clase que representa un buzón de reproceso donde se almacenan los productos rechazados
public class BuzonReproceso {
    private static Queue<Producto> productos = new LinkedList<>(); // Cola FIFO para almacenar los productos
    private static boolean  finEnviado = false;
    
    // Método sincronizado para agregar un producto al buzón
    public synchronized void agregarProducto(Producto producto) {
        productos.add(producto); // Se agrega el producto a la cola
        
        // Notificar a todos los productores en espera
        notifyAll();
    }

    // Método sincronizado para obtener un producto del buzón
    public synchronized Producto obtenerProducto() throws InterruptedException {
        while (productos.isEmpty()) { // Mientras no haya productos, el productor espera
            wait();
        }
        Producto producto = productos.poll(); // Se extrae y devuelve el primer producto de la cola
        
        // Notificar a los productores que pueden agregar más productos si había un límite
        notifyAll();
        return producto;
    }

    // Método para verificar si el buzón está vacío
    public synchronized boolean estaVacio() {
        return productos.isEmpty();
    }
    // Metodo para obtener el total de productos en el buzon
    public synchronized int tamano() {
        return productos.size();
    }

    public synchronized boolean seEnvioFin() {
        return finEnviado;
    }

    public boolean isFinEnviado() {
        return finEnviado;
    }

}

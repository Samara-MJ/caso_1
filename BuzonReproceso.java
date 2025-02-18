//Nombres: Daniela Torres Turriago,
import java.util.LinkedList; 
import java.util.Queue;

// Clase que representa un buzón de reproceso donde se almacenan los productos rechazados
public class BuzonReproceso {
    private Queue<Producto> productos = new LinkedList<>(); // Cola FIFO para almacenar los productos
    private int productoresEsperando = 0; // Contador de productores que están esperando productos

    // Método sincronizado para agregar un producto al buzón
    public synchronized void agregarProducto(Producto producto) {
        productos.add(producto); // Se agrega el producto a la cola
        
        // Solo se notifica si hay productores esperando por un producto
        if (productoresEsperando > 0) {
            notifyAll(); // Despierta a los productores en espera
        }
    }

    // Método sincronizado para obtener un producto del buzón
    public synchronized Producto obtenerProducto() throws InterruptedException {
        while (productos.isEmpty()) { // Mientras no haya productos, el productor espera
            productoresEsperando++;  // Se incrementa el contador de productores en espera
            wait(); // El productor entra en espera hasta que haya un producto disponible
            productoresEsperando--;  // Cuando se despierta, se reduce el contador de espera
        }
        return productos.poll(); // Se extrae y devuelve el primer producto de la cola
    }

    // Método para verificar si el buzón está vacío
    public synchronized boolean estaVacio() {
        return productos.isEmpty();
    }
    // Metodo para obtener el total de productos en el buzon
    public synchronized int tamano() {
        return productos.size();
    }
}

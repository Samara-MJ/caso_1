import java.util.LinkedList;
import java.util.Queue;

// Clase que representa un buzón de revisión donde los productos esperan para ser evaluados
public class BuzonRevision {
    private static Queue<Producto> productos; // Cola FIFO que almacena los productos en espera de revisión
    private int capacidadMaxima; // Capacidad máxima del buzón de revisión

    // Constructor que inicializa el buzón con una capacidad máxima
    public BuzonRevision(int capacidadMaxima) {
        productos = new LinkedList<>();
        this.capacidadMaxima = capacidadMaxima;
    }

    // Método sincronizado para agregar un producto al buzón
    public synchronized void agregarProducto(Producto producto) throws InterruptedException {
        while (productos.size() >= capacidadMaxima) { // Si el buzón está lleno, el hilo espera
            System.out.println("Productor esperando");
            wait();
        }
        // Notificar a los consumidores (operarios de calidad) si hay productos disponibles
        productos.add(producto); 
        System.out.println("Producto " + producto.getId() + " agregado al buzon");
        notifyAll();
    }

    // Método sincronizado para obtener un producto del buzón para su revisión
    public synchronized Producto obtenerProducto() throws InterruptedException {
        while (productos.isEmpty()) { // Si el buzón está vacío, el hilo espera
            wait();
        }
        Producto producto = productos.poll(); // Extraer el primer producto de la cola
        System.out.println("Producto " + producto.getId() + " obtenido del buzon");
        // Notificar a los productores que pueden agregar más productos si había un límite
        notifyAll();
        return producto;
    }

    // Método sincronizado para verificar si hay espacio en el buzón
    public synchronized boolean hayEspacio() {
        return productos.size() < capacidadMaxima;
    }
    //Metodo para obtener la cantidad de productos
    public synchronized int tamano() {
        return productos.size();
    }
}






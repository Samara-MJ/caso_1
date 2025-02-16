import java.util.ArrayList;

public class Deposito {
    private ArrayList<Producto> productos = new ArrayList<>();
    private int totalNecesario;
    private boolean finGenerado = false;

    public Deposito(int totalNecesario) {
        this.totalNecesario = totalNecesario;
    }

    // Agrega un producto aprobado al depósito
    public synchronized void agregarProducto(Producto producto) {
        if (productos.size() < totalNecesario) { // Solo agregar si no se ha alcanzado la meta
            productos.add(producto);
            System.out.println("Producto " + producto.getId() + " agregado al depósito.");
            
            // Notificar a todos cuando se alcance la meta
            if (productos.size() >= totalNecesario) {
                notifyAll();
            }
        }
    }


    // Verifica si se ha alcanzado la meta de producción
    public synchronized boolean haAlcanzadoMeta() {
        return productos.size() >= totalNecesario;
    }

    public int getCantidadActual() {
        return productos.size();
    }

     // Devuelve el total de productos requeridos
     public int getTotalNecesario() {
        return totalNecesario;
    }

    // Muestra el estado actual del depósito
    public synchronized void imprimirEstado() {
        System.out.println("[Depósito] Estado actual: " + productos.size() + "/" + totalNecesario + " productos.");
    }
}

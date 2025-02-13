import java.util.ArrayList;

public class Deposito {
    private ArrayList<Producto> productos = new ArrayList<>();
    private int totalNecesario;

    public Deposito(int totalNecesario) {
        this.totalNecesario = totalNecesario;
    }

    // Agrega un producto aprobado al depósito
    public synchronized void agregarProducto(Producto producto) {
        productos.add(producto);
        System.out.println("Producto " + producto.getId() + " agregado al depósito.");
        notify();
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

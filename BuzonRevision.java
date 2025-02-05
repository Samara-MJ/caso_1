import java.util.LinkedList;
import java.util.Queue;

public class BuzonRevision {
    
    private Queue<Producto> productos;
    private int capacidadMaxima;

    public BuzonRevision(int capacidadMaxima){
        productos= new LinkedList<>();
        this.capacidadMaxima = capacidadMaxima;
    }

    public synchronized void agregarProducto(Producto producto) throws InterruptedException{
        while(productos.size() >= capacidadMaxima){
            wait();
        }
        productos.add(producto);
        notifyAll();
    }

    public synchronized Producto obtenerProducto() throws InterruptedException{
        while(productos.isEmpty()){
            wait(); //EEsperar hasta que haya un producto disponible
        }
        Producto producto = productos.poll();
        notifyAll();
        return producto;
    }

    public synchronized boolean hayEspacio(){
        return productos.size() < capacidadMaxima;
    }









}

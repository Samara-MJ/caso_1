import java.util.LinkedList;
import java.util.Queue;

public class BuzonReproceso {

    private Queue<Producto> productos;

    public BuzonReproceso(){
        productos = new LinkedList<>();
    }

    public synchronized void agregarProducto(Producto producto){
        productos.add(producto);
        notifyAll(); // notificar a los productores que hay un producto disponible

    }

    public synchronized Producto obtenerProducto() throws InterruptedException{
        while(productos.isEmpty()){
            wait(); // esperar a que haya un producto disponible
        }
        return productos.poll();
        
    }

    public synchronized boolean estaVacio(){
        return productos.isEmpty();
    }
    

}

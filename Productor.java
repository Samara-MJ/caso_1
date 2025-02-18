import java.util.concurrent.atomic.AtomicInteger;

public class Productor extends Thread {
    private static final AtomicInteger contadorProductos = new AtomicInteger(0);
    private int id;
    private BuzonReproceso buzonReproceso;
    private BuzonRevision buzonRevision;
    private boolean continuar = true;

    public Productor(int id, BuzonReproceso buzonReproceso, BuzonRevision buzonRevision) {
        this.id = id;
        this.buzonReproceso = buzonReproceso;
        this.buzonRevision = buzonRevision;
    }

    public void run() {
        try {
            while (this.continuar) {
                Producto producto = null;
                    if (!buzonReproceso.estaVacio()) {
                        producto = buzonReproceso.obtenerProducto();
                        if (producto.esFin()) {
                            System.out.println("Productor " + id + " recibió mensaje de FIN. Terminando...");
                            detener();
                        } else {
                            System.out.println("Productor " + id + " reprocesando producto " + producto.getId());
                            buzonRevision.agregarProducto(producto);
                        }
                    }
                    else {
                        int nuevoId = contadorProductos.incrementAndGet();
                        producto = new Producto(nuevoId, "Nuevo");
                        System.out.println("Productor " + id + " generó producto " + producto.getId());
                        buzonRevision.agregarProducto(producto);
                    }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void detener() {
        this.continuar = false;
    }
}

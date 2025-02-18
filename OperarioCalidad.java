import java.util.Random;
public class OperarioCalidad extends Thread { 
    private int id;
    private BuzonRevision buzonRevision;
    private BuzonReproceso buzonReproceso;
    private Deposito deposito;
    private int maxRechazos;
    private int productosRechazados = 0;
    private Random random = new Random();
    private volatile boolean ejecutando = true; // 🔴 Hacer que sea volatile para visibilidad en hilos

    public OperarioCalidad(int id, BuzonRevision buzonRevision, BuzonReproceso buzonReproceso, Deposito deposito, int totalProductos) {
        this.id = id;
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.deposito = deposito;
        this.maxRechazos = (int) Math.floor(totalProductos * 0.1);
    }

    @Override
    public void run() {
        try {
            while (ejecutando) {
                Producto producto = buzonRevision.obtenerProducto();
                System.out.println("Operario " + id + " recibió producto " + producto.getId());
                if (!producto.esFin()) {
                    revisarProducto(producto);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Operario " + id + " interrumpido. Terminando...");
            Thread.currentThread().interrupt();
        }
    }

    public void detener() {
        this.ejecutando = false; // 🔴 Ahora sí se usa correctamente
    }

    private void revisarProducto(Producto producto) throws InterruptedException {
        int numeroAleatorio = random.nextInt(100) + 1;
        boolean aprobado = !(numeroAleatorio % 7 == 0) || productosRechazados >= maxRechazos;
        if (!aprobado) {
            producto.setEstado("Rechazado");
            productosRechazados++;
            buzonReproceso.agregarProducto(producto);
        } else {
            if (!deposito.haAlcanzadoMeta()) {
                producto.setEstado("Aprobado");
                deposito.agregarProducto(producto);
            } else {
                
                Producto finProducto = new Producto(-1, "FIN");
                buzonReproceso.agregarProducto(finProducto);
                System.out.println("Operario " + id + " generó y envió mensaje de FIN.");
                if (buzonRevision.tamano() == 0) {
                    Producto sobrante = buzonRevision.obtenerProducto();
                    buzonReproceso.agregarProducto(sobrante);
                }
                System.out.println("Operario " + id + " ha terminado.");
                detener();
            }
        }
    }

    public int getOperarioId() {
        return id;
    }
}

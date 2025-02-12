import java.util.Random;

// Clase que representa a un operario del equipo de calidad
public class OperarioCalidad implements Runnable {
    public int id; // Identificador del operario
    public BuzonRevision buzonRevision; // Buzón donde se depositan productos para revisión
    public BuzonReproceso buzonReproceso; // Buzón donde se depositan productos rechazados
    public Deposito deposito; // Depósito final donde se almacenan productos aprobados
    public int productosAprobar; // Cantidad total de productos a aprobar
    public int maxRechazos; // Número máximo de productos que pueden ser rechazados
    private int productosRechazados = 0; // Contador de productos rechazados
    private int productosAprobados = 0; // Contador de productos aprobados
    private Random random = new Random(); // Generador de números aleatorios
    private static final int TIEMPO_ESPERA = 100; // Tiempo de espera en milisegundos para espera semiactiva

    // Constructor de la clase
    public OperarioCalidad(int id, BuzonRevision buzonRevision, BuzonReproceso buzonReproceso, Deposito deposito, int totalProductos) {
        this.id = id;
        this.buzonRevision = buzonRevision;
        this.buzonReproceso = buzonReproceso;
        this.deposito = deposito;
        this.productosAprobar = totalProductos;
        this.maxRechazos = (int) Math.floor(totalProductos * 0.1); // 10% del total de productos permitidos como rechazados
    }

    // Método principal de ejecución del operario
    @Override
    public void run() {
        try {
            while (true) {
                Producto producto = null;
                while (producto == null) {
                    producto = buzonRevision.obtenerProducto(); // Obtiene un producto del buzón de revisión
                    if (producto == null) {
                        Thread.yield(); // Ceder el control del CPU a otros hilos sin bloquear el actual
                    }
                }
                
                if (producto.esFin()) { // Verifica si el producto es el mensaje de finalización
                    System.out.println("Operario " + id + " recibió mensaje de FIN. Terminando...");
                    buzonReproceso.agregarProducto(producto); // Asegurar que el mensaje de FIN se propague
                    break; // Termina la ejecución del operario
                }
                
                revisarProducto(producto); // Revisión del producto
                
                // Si se alcanzó el número de productos aprobados, generar mensaje "FIN"
                if (productosAprobados >= productosAprobar) {
                    Producto finProducto = new Producto(-1, Producto.FIN); // Crear producto con mensaje de finalización
                    buzonReproceso.agregarProducto(finProducto); // Enviar mensaje de FIN al buzón de reproceso
                    System.out.println("Operario " + id + " envió mensaje de FIN al buzón de reproceso.");
                    break; // Termina la ejecución del operario
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Manejo adecuado de la interrupción
            System.out.println("Operario " + id + " interrumpido. Terminando...");
        }
    }

    // Método para revisar un producto y determinar si se aprueba o rechaza
    public void revisarProducto(Producto producto) throws InterruptedException {
        int numeroAleatorio = random.nextInt(100) + 1; // Genera un número entre 1 y 100
        boolean aprobado = !(numeroAleatorio % 7 == 0) || productosRechazados >= maxRechazos; // Verifica si debe aprobarse
        
        if (!aprobado) { // Producto rechazado
            producto.setEstado("Rechazado");
            productosRechazados++;
            buzonReproceso.agregarProducto(producto); // Enviar al buzón de reproceso
            System.out.println("Operario " + id + " rechazó producto " + producto.getId());
        } else { // Producto aprobado
            producto.setEstado("Aprobado");
            deposito.agregarProducto(producto); // Enviar al depósito final
            productosAprobados++;
            System.out.println("Operario " + id + " aprobó producto " + producto.getId());
        }
    }
}

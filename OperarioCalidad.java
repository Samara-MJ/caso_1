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
    private boolean finGenerado = false; // Saber si se generó el mensaje de FIN
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

                System.out.println("Operario " + id + " recibió producto " + producto.getId());

                if (producto.esFin()) { // Verifica si el producto es el mensaje de finalización
                    System.out.println("Operario " + id + " recibió mensaje de FIN. Terminando...");
                    buzonReproceso.agregarProducto(producto); // Asegurar que el mensaje de FIN se propague
                    break; // Termina la ejecución del operario
                }

                revisarProducto(producto); // Revisión del producto

                // Si se alcanzó el número de productos aprobados y no se ha generado el mensaje de fin, generar mensaje "FIN"
                if (productosAprobados >= productosAprobar && !finGenerado) {
                    Producto finProducto = new Producto(-1, "FIN"); // Crear producto con mensaje de finalización
                    buzonReproceso.agregarProducto(finProducto); // Enviar mensaje de FIN al buzón de reproceso
                    System.out.println("Operario " + id + " envió mensaje de FIN al buzón de reproceso.");
                    finGenerado = true; // Marcar que el mensaje de FIN ha sido generado
                }

                // Termina si se generó el mensaje de FIN y el buzón de revisión está vacío
                if (finGenerado && buzonRevision.tamano() == 0) {
                    System.out.println("Operario " + id + " No hay más productos en el buzón. Terminando ejecución.");
                    break;
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
            System.out.println("Operario " + id + " rechazó producto " + producto.getId());
            buzonReproceso.agregarProducto(producto); // Enviar al buzón de reproceso
        } else { // Producto aprobado
            producto.setEstado("Aprobado");
            System.out.println("Operario " + id + " aprobó producto " + producto.getId());
            deposito.agregarProducto(producto); // Enviar al depósito final
            System.out.println("Producto " + producto.getId() + " enviado al depósito por Operario " + id);
            productosAprobados++;
        }
    }
}

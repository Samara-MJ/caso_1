public class Productor extends Thread {
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
            while (continuar) {
                Producto producto = null;
                
                // Intentar reprocesar si hay productos en el buzón de reproceso
                synchronized (buzonReproceso) {
                    if (!buzonReproceso.estaVacio()) {
                        producto = buzonReproceso.obtenerProducto();
                        if (producto.esFin()) {
                            System.out.println("Productor " + id + " recibió mensaje de FIN. Terminando...");
                            continuar = false;
                        } else {
                            System.out.println("Productor " + id + " reprocesando producto " + producto.getId());
                        }
                    }
                }
                
                // Si recibió mensaje de FIN, salir del bucle principal
                if (!continuar) {
                    return;
                }
                
                // Si no hay productos en reproceso, generar uno nuevo
                if (producto == null) {
                    producto = new Producto(id, "Nuevo");
                    System.out.println("Productor " + id + " generó producto " + producto.getId());
                }
                
                // Intentar depositarlo en el buzón de revisión
                synchronized (buzonRevision) {
                    buzonRevision.agregarProducto(producto);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

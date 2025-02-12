import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AreaProduccion {
    private List<Productor> productores;
    private List<OperarioCalidad> operariosCalidad;
    private static BuzonReproceso buzonReproceso;
    private static BuzonRevision buzonRevision;
    private Deposito deposito;

    public AreaProduccion(int numOperarios, int numProductos, int capacidadBuzon) {
        buzonReproceso = new BuzonReproceso();
        buzonRevision = new BuzonRevision(capacidadBuzon);
        deposito = new Deposito(numProductos);
        productores = new ArrayList<>();
        operariosCalidad = new ArrayList<>();

        // Crear productores
        for (int i = 0; i < numOperarios; i++) {
            productores.add(new Productor(i, buzonReproceso, buzonRevision));
        }

        // Crear operarios de calidad
        for (int i = 0; i < numOperarios; i++) {
            operariosCalidad.add(new OperarioCalidad(i, buzonRevision, buzonReproceso, deposito, numProductos));
        }
    }

    public void iniciarProduccion() {
        // Iniciar productores
        for (Productor p : productores) {
            p.start();
        }

        // Iniciar operarios de calidad
        for (OperarioCalidad o : operariosCalidad) {
            o.start();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el número de operarios productores y de calidad: ");
        int numOperarios = scanner.nextInt();

        System.out.print("Ingrese el número total de productos a producir: ");
        int numProductos = scanner.nextInt();

        System.out.print("Ingrese la capacidad del buzón de revisión: ");
        int capacidadBuzon = scanner.nextInt();

        scanner.close();

        AreaProduccion area = new AreaProduccion(numOperarios, numProductos, capacidadBuzon);
        area.iniciarProduccion();
    }
}


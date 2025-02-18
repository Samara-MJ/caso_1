import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AreaProduccion {
    private static List<Productor> productores;
        private List<OperarioCalidad> operariosCalidad; // 🔴 Guardamos referencia a los operarios
        private static BuzonReproceso buzonReproceso;
        private static BuzonRevision buzonRevision;
        private Deposito deposito;
        public static boolean productoresTerminados = false;
    
        public AreaProduccion(int numOperarios, int numProductos, int capacidadBuzon) {
            buzonReproceso = new BuzonReproceso();
            buzonRevision = new BuzonRevision(capacidadBuzon);
            deposito = new Deposito(numProductos);
            productores = new ArrayList<>();
            operariosCalidad = new ArrayList<>();
    
            for (int i = 0; i < numOperarios; i++) {
                productores.add(new Productor(i, buzonReproceso, buzonRevision));
            }
    
            for (int i = 0; i < numOperarios; i++) {
                OperarioCalidad operario = new OperarioCalidad(i, buzonRevision, buzonReproceso, deposito, numProductos);
                operariosCalidad.add(operario);
            }
        }
    
        public void iniciarProduccion() {
            for (Productor p : productores) {
                p.start();
            }
    
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

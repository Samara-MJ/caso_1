//Nombres: Daniela Torres Turriago,


// Clase que representa un producto dentro de la línea de producción
public class Producto {
    private int id; // Identificador del producto
    private String estado; // Estado del producto (Nuevo, Rechazado, etc.)
    public static final String FIN = "FIN"; // Constante que representa el mensaje de finalización

    public Producto(int id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para verificar si un producto es el mensaje de finalización
    public boolean esFin() {
        return FIN.equals(estado);
    }
}


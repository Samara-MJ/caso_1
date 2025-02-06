public class Producto {
    private int id;
    private String estado;


    public Producto(int id, String estado){
        this.id = id;
        this.estado = estado;
    }   

    public int getId(){
        return id;
    }

    public String getEstado(){
        return estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    public void marcarAprobado(){

    }

    public void marcarRechazado(){
        
        
    }


}

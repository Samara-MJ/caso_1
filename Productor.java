public class Productor  extends Thread{
    private int id;
    private static BuzonReproceso buzonReproceso ;
    private static BuzonReproceso buzonRevision;

    public Productor(int id){
        this.id = id;
    }


    public void run(){}

    public int getProductorId()
    {
        return id;
    }
    public void generarProducto ()
    {}
    public void reprocesarProducto()
    {}


    

    
}

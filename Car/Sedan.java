package Car ;

public class Sedan extends Car{
    private double engine;
    private int seats;

    public Sedan(double engine, int seats, int id, String brand,String dealer_name,Boolean available){
        super(id, brand,dealer_name,available);
        this.engine = engine;
        this.seats = seats;
    }

    public double getEngine(){
        return engine;
    }    

    public void setEngine(double engine){
        this.engine = engine;
    }

    public int getSeats(){
        return seats;
    }

    public void setSeats(int seats){
        this.seats = seats;
    }
}

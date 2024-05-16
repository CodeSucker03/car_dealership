package Car;


public class Van extends Car{
    private int seats;
    private String fuelType;

    public Van(int seats, String fuelType, int id, String brand, String dealer,Boolean available){
        super(id, brand, dealer, available);
        this.seats = seats;
        this.fuelType = fuelType;
    }

    public int getSeats(){
        return seats;
    }

    public void setSeats(int seats){
        this.seats = seats;
    }

    public String getFuelType(){
        return fuelType;
    }

    public void setFuelType(String fuelType){
        this.fuelType = fuelType;
    }
}

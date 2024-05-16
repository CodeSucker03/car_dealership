package Car;

public class Truck extends Car{
    private double weight;
    private int tires;

    public Truck(double weight, int tires, int id, String brand, String dealer,Boolean available){
        super(id, brand, dealer,available);
        this.weight = weight;
        this.tires = tires;
    }

    public double getWeight(){
        return weight;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public int getTires(){
        return tires;
    }

    public void setTires(int tires){
        this.tires = tires;
    }
}
package Car;


public class SUV extends Car{
    private double weight;
    private String color;

    public SUV(double weight, String color, int id, String brand, String dealer,Boolean available) {
        super(id, brand, dealer,available);
        this.weight = weight;
        this.color = color;
    }   

    public double getWeight(){
        return weight;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public String getColor(){
        return color;
    }

    public void setColor(String color){
        this.color = color;
    }
}

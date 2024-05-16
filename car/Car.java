package Car;

public class Car {
    private int id;
    private String brand;
    private String dealer;
    private Boolean available;

    public Car() {
    }

    public Car(int id, String brand, String dealer, Boolean available) {
        this.id = id;
        this.brand = brand;
        this.dealer = dealer;
        this.available = available;
    }

    public String getDealer() {
        return dealer;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}

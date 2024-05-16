package User;

public class Dealer extends User {
    private String country;

    public Dealer(int dealerId, String username, String password,String country) {
        super(username, password,dealerId);
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
   
}



package User;

public class Customer extends User{
    private String email;

    public Customer(int CustomerId, String username, String password,String Email) {
        super(username, password,CustomerId);
        this.email = Email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}

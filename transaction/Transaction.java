package transaction;

import java.util.Date;

import Car.Car;
import User.Customer;

public class 
Transaction {
    private Customer customer;
    private Car car;
    private Date date;

    public Transaction(Car car, Customer customer, Date date) {
        this.car = car;
        this.customer = customer;
        this.date = date;
    }

    public Transaction(Car car, Customer customer) {
        this.car = car;
        this.customer= customer;
        this.date = new Date();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer_Name(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

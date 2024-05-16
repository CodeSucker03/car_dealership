package view;

import User.Customer;
import User.Dealer;
import User.User;
import User.Admin;
import Car.Car;
import Car.SUV;
import Car.Sedan;
import Car.Truck;
import Car.Van;
import Transaction.Transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class dbConnectTo {
    private String dbUrl = "jdbc:mysql://localhost:3306/mydb1";
    private String dbUsername = "root";
    private String dbPassword = "171171";
    private String dataTable;
    private String sql;

    public dbConnectTo() {
    };

    public dbConnectTo(String dataTable) {// select which table to work
        this.dataTable = dataTable;
    }

    public Car getSpecific_Car(int car_id) { // return one specific obj car HELPER FUNCTION for Transaction
        Car newcar = new Car();
        sql = "SELECT * FROM car, " + this.dataTable + " WHERE car.car_id = " + this.dataTable
                + ".car_id AND car.car_id = ? ";
        try {
            Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement prepstmt = con.prepareStatement(sql);
            prepstmt.setInt(1, car_id);
            ResultSet resultSet = prepstmt.executeQuery();
            while (resultSet.next()) {
                switch (this.dataTable) {
                    case "sedan":
                        newcar = new Sedan(resultSet.getDouble("engine"), resultSet.getInt("seats"),
                                resultSet.getInt("car_id"), resultSet.getString("brand"),
                                resultSet.getString("dealer_name"), resultSet.getBoolean("available"));
                        break;
                    case "suv":
                        newcar = new SUV(resultSet.getDouble("weight"), resultSet.getString("color"),
                                resultSet.getInt("car_id"), resultSet.getString("brand"),
                                resultSet.getString("dealer_name"), resultSet.getBoolean("available"));
                        break;
                    case "truck":
                        newcar = new Truck(resultSet.getDouble("weight"), resultSet.getInt("tires_number"),
                                resultSet.getInt("car_id"), resultSet.getString("brand"),
                                resultSet.getString("dealer_name"), resultSet.getBoolean("available"));
                        break;
                    case "van":
                        newcar = new Van(resultSet.getInt("seats"), resultSet.getString("fuel_type"),
                                resultSet.getInt("car_id"), resultSet.getString("brand"),
                                resultSet.getString("dealer_name"), resultSet.getBoolean("available"));
                        break;
                    default:
                        break;
                }
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newcar;
    }

    public List<Transaction> get_Transaction(String userName) {
        List<Transaction> transactions = new ArrayList<>();
        sql = "SELECT * FROM user, customer, transaction WHERE transaction.username = user.username and user.user_id = customer.user_id AND transaction.username = ?";
        try {
            Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement prepstmt = con.prepareStatement(sql);
            prepstmt.setString(1, userName);
            ResultSet rs = prepstmt.executeQuery();
            while (rs.next()) {
                dbConnectTo newCon_get_Car = new dbConnectTo(rs.getString("vehicle_type"));
                Car newCar = newCon_get_Car.getSpecific_Car(rs.getInt("car_id"));
                Customer customer = new Customer(rs.getInt("user_id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("email"));
                Transaction newTran = new Transaction(newCar, customer, rs.getTimestamp("transaction_time"));
                transactions.add(newTran);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    };

    public Boolean crate_Transaction(Transaction transaction, String vehicleType) {
        Boolean check = false;
        String sql1 = "UPDATE car SET available = ? WHERE car_id = ?"; // update car table
        try {
            Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement prepStatement = con.prepareStatement(sql1);
            prepStatement.setInt(1, 0);
            prepStatement.setInt(2, transaction.getCar().getId());
            prepStatement.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // add transaction
        sql = "INSERT INTO " + this.dataTable
                + " (username, transaction_time, vehicle_type,car_id) VALUES (?, ?, ?, ?)";
        try {
            Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement prepStatement = con.prepareStatement(sql);
            prepStatement.setString(1, transaction.getCustomer().getUsername());
            prepStatement.setTimestamp(2, new Timestamp(transaction.getDate().getTime()));
            prepStatement.setString(3, vehicleType);
            prepStatement.setInt(4, transaction.getCar().getId());
            int rowsAffected = prepStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Purchase successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                check = true;
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    public Boolean addCustomer(Customer customer) {
        Boolean check = true;
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement prepStatement = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            prepStatement.setString(1, customer.getUsername());
            prepStatement.setString(2, customer.getPassword());
            prepStatement.executeUpdate();
            // Retrieve the auto-generated key
            ResultSet resultSet = prepStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int generatedKey = resultSet.getInt(1);
                if (generatedKey >= 0) {
                    String sql2 = "INSERT INTO customer (email,user_id) VALUES (?,?)";
                    try {
                        PreparedStatement prepStatement2 = conn.prepareStatement(sql2);
                        prepStatement2.setString(1, customer.getEmail());
                        prepStatement2.setInt(2, generatedKey);

                        int rowAffected = prepStatement2.executeUpdate();
                        if (rowAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Register success");
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            check = true;
            conn.close();
        } catch (SQLException e1) {
            if (e1.getMessage().contains("Duplicate entry")) {
                // This exception occurs when there's a duplicate entry for a unique constraint
                JOptionPane.showMessageDialog(null, "Username is already taken. Please choose another username.");
                check = false;
            } else {
                e1.printStackTrace();
            }
        }
        return check;
    }

    public Boolean addADmin(Admin admin) {
        Boolean check = true;
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement prepStatement = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            prepStatement.setString(1, admin.getUsername());
            prepStatement.setString(2, admin.getPassword());
            prepStatement.executeUpdate();
            // Retrieve the auto-generated key
            ResultSet resultSet = prepStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int generatedKey = resultSet.getInt(1);
                if (generatedKey >= 0) {
                    String sql2 = "INSERT INTO admin (user_id) VALUES (?)";
                    try {
                        PreparedStatement prepStatement2 = conn.prepareStatement(sql2);
                        prepStatement2.setInt(1, generatedKey);

                        int rowAffected = prepStatement2.executeUpdate();
                        if (rowAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Register success");
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            check = true;
            conn.close();
        } catch (SQLException e1) {
            if (e1.getMessage().contains("Duplicate entry")) {
                // This exception occurs when there's a duplicate entry for a unique constraint
                JOptionPane.showMessageDialog(null, "Username is already taken. Please choose another username.");
                check = false;
            } else {
                e1.printStackTrace();
            }
        }
        return check;
    }

    public Boolean addDealer(Dealer dealer) {
        Boolean check = true;
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement prepStatement = conn.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            prepStatement.setString(1, dealer.getUsername());
            prepStatement.setString(2, dealer.getPassword());
            prepStatement.executeUpdate();
            // Retrieve the auto-generated key
            ResultSet resultSet = prepStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int generatedKey = resultSet.getInt(1);
                if (generatedKey >= 0) {
                    String sql2 = "INSERT INTO dealer (country, user_id) VALUES (?,?)";
                    try {
                        PreparedStatement prepStatement2 = conn.prepareStatement(sql2);
                        prepStatement2.setString(1, dealer.getCountry());
                        prepStatement2.setInt(2, generatedKey);

                        int rowAffected = prepStatement2.executeUpdate();
                        if (rowAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Register success");
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            check = true;
            conn.close();
        } catch (SQLException e1) {
            if (e1.getMessage().contains("Duplicate entry")) {
                // This exception occurs when there's a duplicate entry for a unique constraint
                JOptionPane.showMessageDialog(null, "Username is already taken. Please choose another username.");
                check = false;
            } else {
                e1.printStackTrace();
            }
        }
        return check;
    }

    User log_In_Verify(String username, String password) {
        User newUser = new User();
        if (this.dataTable.equals("customer") || this.dataTable.equals("dealer")) {
            sql = "SELECT * FROM user, " + this.dataTable + " WHERE user.user_id = " + this.dataTable
                    + ".user_id and username = ? and password = ?";
        }
        if (this.dataTable.equals("admin")) {
            sql = "SELECT * FROM user, " + this.dataTable + " WHERE username = ? and password = ?";
        }
        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement prepStatement = conn.prepareStatement(sql);
            prepStatement.setString(1, username);
            prepStatement.setString(2, password);
            ResultSet result = prepStatement.executeQuery();
            if (result.next()) {
                if (this.dataTable.equals("customer")) {
                    newUser = new Customer(result.getInt("user_id"), result.getString("username"),
                            result.getString("password"), result.getString("email"));
                } else if (this.dataTable.equals("dealer")) {
                    newUser = new Dealer(result.getInt("user_id"), result.getString("username"),
                            result.getString("password"), result.getString("country"));
                } else {
                    newUser = new Admin(result.getString("username"),
                            result.getString("password"), result.getInt("user_id"));
                }
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newUser;
    }

    public void removeUserData(User user) {
        sql = "DELETE FROM " + this.dataTable + " WHERE user_id = ?";
        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            PreparedStatement prepStatement = conn.prepareStatement(sql);
            prepStatement.setInt(1, user.getId());
            int rowsAffected = prepStatement.executeUpdate();
            if (rowsAffected == 0) {
                JOptionPane.showMessageDialog(null, "No user with id '" + user.getId() + "'' found.");

            } else {
                try {
                    String sql2 = "DELETE FROM user WHERE user_id = ?";
                    PreparedStatement prepStatement2 = conn.prepareStatement(sql2);
                    prepStatement2.setInt(1, user.getId());
                    int rowsAffected2 = prepStatement2.executeUpdate();
                    if (rowsAffected2 != 0) {
                        String sql1 = "UPDATE car SET available = ? WHERE dealer_name = ?"; // update car table
                        try {
                            PreparedStatement prepStatement3 = conn.prepareStatement(sql1);
                            prepStatement3.setInt(1, 0);
                            prepStatement3.setString(2, user.getUsername());
                            prepStatement3.executeUpdate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(null, "User removed successfully", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            conn.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public List<User> search_By_UserName(String username) {
        List<User> user = new ArrayList<>();
        ResultSet resultSet;
        try {
            // Establish the database connection
            Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            if (username == null) { // If no username then get all user
                sql = "SELECT * FROM user, " + this.dataTable + " WHERE user.user_id = " + this.dataTable
                        + ".user_id";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                resultSet = preparedStatement.executeQuery();
            } else { // Get specific user with username
                sql = "SELECT * FROM user, " + this.dataTable + " WHERE user.user_id = " + this.dataTable
                        + ".user_id AND username = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, username);
                resultSet = preparedStatement.executeQuery();
            }

            while (resultSet.next()) {
                if (this.dataTable == "customer") {
                    user.add(new Customer(resultSet.getInt("user_id"), resultSet.getString("username"),
                            resultSet.getString("password"), resultSet.getString("email")));
                }
                if (this.dataTable == "dealer") {
                    user.add(new Dealer(resultSet.getInt("user_id"), resultSet.getString("username"),
                            resultSet.getString("password"), resultSet.getString("country")));

                }
                if (this.dataTable == "admin") {
                    user.add(new Admin(resultSet.getString("username"),
                            resultSet.getString("password"), resultSet.getInt("user_id")));
                }
            }
            // Close the connections
            con.close();
        } catch (SQLException sqlE) {

            sqlE.printStackTrace();

        }
        return user;
    }

    public void removeCarData(int carID) {
        sql = "DELETE FROM " + this.dataTable + " WHERE car_id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                PreparedStatement prepStatement = conn.prepareStatement(sql)) {
            prepStatement.setInt(1, carID);
            int rowsAffected = prepStatement.executeUpdate();
            if (rowsAffected == 0) {
                JOptionPane.showMessageDialog(null, "No car with id '" + carID + "'' found.");

            } else {
                sql = "DELETE FROM car WHERE car_id = ?";
                PreparedStatement prepStatement2 = conn.prepareStatement(sql);
                prepStatement2.setInt(1, carID);
                int rowsAffected2 = prepStatement2.executeUpdate();
                if (rowsAffected2 != 0) {
                    JOptionPane.showMessageDialog(null, "Car removed successfully", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
            conn.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public Boolean addCarData(Car car) { // call to add car data
        Boolean check = false;
        try {
            // Establish the database connection
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            sql = "INSERT INTO car (dealer_name, brand, available) VALUES (?, ?, ?)";
            PreparedStatement prepStatement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prepStatement.setString(1, car.getDealer());
            prepStatement.setString(2, car.getBrand());
            prepStatement.setInt(3, 1);
            int rowAffected = prepStatement.executeUpdate();
            if (rowAffected != 0) {
                ResultSet resultSet = prepStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int generatedKey = resultSet.getInt(1);
                    if (generatedKey >= 0) {
                        int rowAffected2 = 0;

                        switch (this.dataTable) {
                            case "sedan":
                                Sedan sedan = (Sedan) car;
                                sql = "INSERT INTO " + this.dataTable
                                        + " (engine, seats, car_id) VALUES (?, ?, ?)";
                                PreparedStatement prepStatement2 = conn.prepareStatement(sql);
                                prepStatement2.setDouble(1, sedan.getEngine());
                                prepStatement2.setInt(2, sedan.getSeats());
                                prepStatement2.setInt(3, generatedKey);
                                rowAffected2 = prepStatement2.executeUpdate();
                                break;
                            case "suv":
                                SUV suv = (SUV) car;
                                sql = "INSERT INTO " + this.dataTable
                                        + " (weight, color, car_id) VALUES (?, ?, ?)";
                                PreparedStatement prepStatement3 = conn.prepareStatement(sql);

                                prepStatement3.setDouble(1, suv.getWeight());
                                prepStatement3.setString(2, suv.getColor());
                                prepStatement3.setInt(3, generatedKey);
                                rowAffected2 = prepStatement3.executeUpdate();
                                break;
                            case "truck":
                                Truck truck = (Truck) car;
                                sql = "INSERT INTO " + this.dataTable
                                        + " (weight, tires_number, car_id) VALUES (?, ?, ?)";
                                PreparedStatement prepStatement4 = conn.prepareStatement(sql);

                                prepStatement4.setDouble(1, truck.getWeight());
                                prepStatement4.setInt(2, truck.getTires());
                                prepStatement4.setInt(3, generatedKey);

                                rowAffected2 = prepStatement4.executeUpdate();
                                break;
                            case "van":
                                Van van = (Van) car;
                                sql = "INSERT INTO " + this.dataTable
                                        + " (fuel_type, seats, car_id) VALUES (?, ?, ?)";
                                PreparedStatement prepStatement5 = conn.prepareStatement(sql);

                                prepStatement5.setString(1, van.getFuelType());
                                prepStatement5.setInt(2, van.getSeats());
                                prepStatement5.setInt(3, generatedKey);
                                rowAffected2 = prepStatement5.executeUpdate();
                                break;
                            default:
                                break;
                        }
                        if (rowAffected2 != 0) {
                            check = true;
                            JOptionPane.showMessageDialog(null, "Data inserted successfully", "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
            // Close the connections
            conn.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while inserting data", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return check;
    }

    public List<Car> search_Car_ByColumn(String columnName, String value_to_get) {
        List<Car> cars = new ArrayList<>();
        ResultSet resultSet;
        try {
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            if (columnName == null) {
                sql = "SELECT * FROM car, " + this.dataTable + " WHERE car.car_id = " + this.dataTable + ".car_id ";
                PreparedStatement stmt = conn.prepareStatement(sql);
                resultSet = stmt.executeQuery();

            } else {
                sql = "SELECT * FROM car, " + this.dataTable + " WHERE car.car_id = " + this.dataTable + ".car_id AND "
                        + columnName + " = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, value_to_get);
                resultSet = stmt.executeQuery();
            }
            while (resultSet.next()) {
                if (this.dataTable == "sedan") {
                    Sedan sedan = new Sedan(resultSet.getDouble("engine"), resultSet.getInt("seats"),
                            resultSet.getInt("car_id"), resultSet.getString("brand"),
                            resultSet.getString("dealer_name"), resultSet.getBoolean("available"));
                    cars.add(sedan);
                }
                if (this.dataTable == "suv") {
                    SUV suv = new SUV(resultSet.getDouble("weight"), resultSet.getString("color"),
                            resultSet.getInt("car_id"), resultSet.getString("brand"),
                            resultSet.getString("dealer_name"), resultSet.getBoolean("available"));
                    cars.add(suv);
                }
                if (this.dataTable == "truck") {
                    Truck truck = new Truck(resultSet.getDouble("weight"), resultSet.getInt("tires_number"),
                            resultSet.getInt("car_id"), resultSet.getString("brand"),
                            resultSet.getString("dealer_name"), resultSet.getBoolean("available"));
                    cars.add(truck);
                }
                if (this.dataTable == "van") {
                    Van van = new Van(resultSet.getInt("seats"), resultSet.getString("fuel_type"),
                            resultSet.getInt("car_id"), resultSet.getString("brand"),
                            resultSet.getString("dealer_name"), resultSet.getBoolean("available"));
                    cars.add(van);
                }
            }
            // Close the connections
            conn.close();
        } catch (SQLException sqlE) {

            sqlE.printStackTrace();
        }
        return cars;
    }

    public static void main(String[] args) {
        dbConnectTo newCon = new dbConnectTo("customer");
        // List<Car> cars = newCon.searchbyColumn("dealer_name", "jeff");
        // for (Car car : cars) {
        // System.out.println(car.getBrand());
        // }
        List<User> users = newCon.search_By_UserName("pewdiepie");
        for (User user : users) {
            Customer customer = (Customer) user;
            System.out.println(customer.getUsername());
        }
    }
}

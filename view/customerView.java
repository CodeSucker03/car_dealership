package loginPrj;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import Car.Car;
import Car.SUV;
import Car.Sedan;
import Car.Truck;
import Car.Van;
import Transaction.Transaction;
import User.Customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class customerView extends JFrame {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel topPanel;
    JComboBox<String> carOption;
    private List<Car> cars;
    private String[] options = { "sedan", "suv", "truck", "van" };
    private Customer customer;

    public customerView(Customer customer) {
        this.customer = customer;
        this.setSize(900, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // Create top panel
        topPanel = new JPanel();
        JLabel userJLabel = new JLabel("Welcome customer: " + customer.getUsername());
        topPanel.add(userJLabel);

        // Create the left panel (larger) and set it as scrollable
        leftPanel = new JPanel();
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);

        carOption = new JComboBox<>(options);

        JTextField searchCarField = new JTextField(12);

        // display default car list
        disPlay_Car_List("sedan");
        int rowForGrid = cars.size() < 2 ? 10 : 4;
        leftPanel.setLayout(new GridLayout(rowForGrid, 1, 0, 20));
        // Create the right panel (smaller)
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(150, this.getHeight()));
        rightPanel.setBackground(Color.GRAY);

        carOption.addActionListener((e) -> {
            cars.clear();
            leftPanel.removeAll();
            switch ((String) carOption.getSelectedItem()) {
                case "sedan":
                    searchCarField.setText("");
                    disPlay_Car_List("sedan");
                    break;
                case "suv":
                    searchCarField.setText("");
                    disPlay_Car_List("suv");
                    break;
                case "truck":
                    searchCarField.setText("");
                    disPlay_Car_List("truck");

                    break;
                case "van":
                    searchCarField.setText("");
                    disPlay_Car_List("van");

                    break;
                default:
                    break;
            }
            leftPanel.revalidate();
            leftPanel.repaint();
        });
        rightPanel.add(carOption);

        rightPanel.add(searchCarField);
        // Serach car button
        JButton search_button = new JButton("Search brand");
        search_button.addActionListener((e1) -> {
            String wantedCar = searchCarField.getText();
            leftPanel.removeAll();
            System.out.println(cars.size());
            for (Car car : cars) {
                if (car.getBrand().equalsIgnoreCase(wantedCar)) {
                    carDisplayPanel_for_Customer newpanel1 = new carDisplayPanel_for_Customer(
                            (String) carOption.getSelectedItem(),
                            car, customer,leftPanel);
                    leftPanel.add(newpanel1);
                }
            }
            leftPanel.revalidate();
            leftPanel.repaint();
        });

        rightPanel.add(search_button);

        // Transcation button to view
        JButton showTransactionButton = new JButton("My Transactions");
        showTransactionButton.addActionListener((e) -> {
            @SuppressWarnings("unused")
            transactionView newTranViewForUser = new transactionView(customer.getUsername());
        });
        rightPanel.add(showTransactionButton);

        JButton return_to_log_in_screen = new JButton("Log out");
        return_to_log_in_screen.addActionListener((e) -> {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure want to log out?",
                    "Confirm purchase", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                this.dispose();
                @SuppressWarnings("unused")
                Login_View newLogIn = new Login_View();
            }

        });
        rightPanel.add(return_to_log_in_screen);

        // Add the panels to the frame
        this.add(leftScrollPane, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);
        this.add(topPanel, BorderLayout.NORTH);
        // Make the JFrame visible
        this.setVisible(true);
    }

    void disPlay_Car_List(String option) {
        dbConnectTo newConn = new dbConnectTo(option);
        cars = newConn.search_Car_ByColumn("available", "1");
        for (Car car : cars) {
            carDisplayPanel_for_Customer newpanel = new carDisplayPanel_for_Customer(
                    (String) carOption.getSelectedItem(),
                    car, customer,leftPanel);
            leftPanel.add(newpanel);
        }
        leftPanel.revalidate();
        leftPanel.repaint();
    }
}

class carDisplayPanel_for_Customer extends JPanel {
    private JLabel brandLabel;
    private JLabel sell_state_label;
    private JLabel carId_label;
    JLabel vehicle1;
    String vehicleType;
    JButton Purchase_Button, car_Deatail_button;
    JPanel lefJPanel;

    carDisplayPanel_for_Customer(String vehicleType, Car car, Customer customer, JPanel leftPanel) {
        this.lefJPanel = leftPanel;
        this.vehicleType = vehicleType;
        this.setPreferredSize(new Dimension(300, 70));
        setLayout(new GridLayout(2, 2, 0, 5));
        this.setBackground(Color.lightGray);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 5);
        this.setBorder(border);
        this.setPreferredSize(new Dimension(300, 100));

        // Create labels for dealer name, brand, and availability
        vehicle1 = new JLabel("Type: " + vehicleType);
        brandLabel = new JLabel("Brand: " + car.getBrand());
        sell_state_label = new JLabel("Availability: " + (car.getAvailable() ? "On sell" : "Sold"));
        carId_label = new JLabel("ID: " + Integer.toString(car.getId()));
        Font font = new Font("Monospaced", Font.BOLD, 16);
        brandLabel.setFont(font);
        sell_state_label.setFont(font);
        vehicle1.setFont(font);
        carId_label.setFont(font);
        // Purchase button
        Purchase_Button = new JButton("Purchase");
        Purchase_Button.addActionListener((e1) -> {
            int option = JOptionPane.showConfirmDialog(null, "Confirm your purchase ?",
                    "Confirm purchase", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                process_Purchase(car, customer,carDisplayPanel_for_Customer.this);
            }
        });
        car_Deatail_button = new JButton("See details");
        car_Deatail_button.addActionListener((e2) -> {
            showCarDetail_for_Customer(car);
        });
        // Add labels to the panel
        this.add(vehicle1);
        this.add(brandLabel);
        this.add(carId_label);
        this.add(sell_state_label);
        this.add(car_Deatail_button);
        this.add(Purchase_Button);

    }

    void process_Purchase(Car car, Customer customer,carDisplayPanel_for_Customer carPanel) {
        Transaction newTransaction = new Transaction(car, customer);
        dbConnectTo newCon = new dbConnectTo("transaction");
        if(newCon.crate_Transaction(newTransaction, vehicleType)){
            lefJPanel.remove(carPanel);
            lefJPanel.revalidate();
            lefJPanel.repaint();
        };
    }

    void showCarDetail_for_Customer(Car car) {
        // Constructing the message to display
        StringBuilder message = new StringBuilder();
        message.append("Type: " + vehicleType + "\n");
        message.append("Dealer Name: ").append(car.getDealer()).append("\n");
        message.append("ID: ").append(car.getId()).append("\n");
        message.append("Brand: ").append(car.getBrand()).append("\n");
        message.append("Availability: ").append(car.getAvailable() ? "On market" : "Sold").append("\n");

        switch (vehicleType) {
            case "sedan":
                Sedan sedan = (Sedan) car;
                message.append("Engine: V").append(sedan.getEngine()).append("\n");
                message.append("Seats: ").append(sedan.getSeats()).append("\n");
                break;
            case "suv":
                SUV suv = (SUV) car;
                message.append("Weight: ").append(suv.getWeight()).append("\n");
                message.append("Color: ").append(suv.getColor()).append("\n");
                break;
            case "truck":
                Truck truck = (Truck) car;
                message.append("Weight: ").append(truck.getWeight()).append("\n");
                message.append("Tires: ").append(truck.getTires()).append("\n");
                break;
            case "van":
                Van van = (Van) car;
                message.append("Fuel Type: ").append(van.getFuelType()).append("\n");
                message.append("Seats: ").append(van.getSeats()).append("\n");
                break;
            default:
                break;
        }

        // Displaying the message using JOptionPane
        JOptionPane.showMessageDialog(null, message.toString(), "Vehicle Information", JOptionPane.INFORMATION_MESSAGE);
    }
}

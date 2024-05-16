package view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import Car.Car;
import Car.SUV;
import Car.Sedan;
import Car.Truck;
import Car.Van;
import Transaction.Transaction;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class transactionView extends JFrame {
    JPanel maiPanel;
    List<Transaction> transactions;

    public transactionView(String userName) {
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        dbConnectTo newCOn = new dbConnectTo("transaction");
        transactions = newCOn.get_Transaction(userName);

        maiPanel = new JPanel();
        JScrollPane leftScrollPane = new JScrollPane(maiPanel);

        for (Transaction transaction : transactions) {
            transaction_Panel newpanel = new transaction_Panel(transaction);
            maiPanel.add(newpanel);
        }
        if (transactions.size()==0) {
            JLabel note = new JLabel("NO transaction");
            maiPanel.add(note);
        }
        int rowForGrid = transactions.size() < 2 ? 12 : 6;
        maiPanel.setLayout(new GridLayout(rowForGrid, 1, 0, 20));
        this.add(leftScrollPane);
        this.setVisible(true);
    }
}

class transaction_Panel extends JPanel {
    private JLabel brandLabel;
    private JLabel carId_label;
    private JLabel Transaction_Time_label;
    private JButton car_Deatail_button;

    transaction_Panel(Transaction transaction) { // panel for a transaction display
        this.setPreferredSize(new Dimension(300, 70));
        setLayout(new GridLayout(2, 2, 0, 5));
        this.setBackground(Color.lightGray);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 5);
        this.setBorder(border);
        this.setPreferredSize(new Dimension(300, 100));

        // Create labels for dealer name, brand, and availability
        brandLabel = new JLabel("Brand: " + transaction.getCar().getBrand());
        carId_label = new JLabel("Car ID: " + Integer.toString(transaction.getCar().getId()));
        Font font = new Font("Monospaced", Font.BOLD, 16);
        Transaction_Time_label = new JLabel("Time: " + transaction.getDate());

        brandLabel.setFont(font);
        carId_label.setFont(font);

        car_Deatail_button = new JButton("See details");
        car_Deatail_button.addActionListener((e2) -> {
            showCarDetail_for_TransactionVIew(transaction.getCar());
        });
        // Add labels to the panel
        this.add(brandLabel);
        this.add(carId_label);
        this.add(Transaction_Time_label);
        this.add(car_Deatail_button);

    }

    void showCarDetail_for_TransactionVIew(Car car) {
        // Constructing the message to display
        StringBuilder message = new StringBuilder();
        message.append("Dealer Name: ").append(car.getDealer()).append("\n");
        message.append("ID: ").append(car.getId()).append("\n");
        message.append("Brand: ").append(car.getBrand()).append("\n");

        if (car instanceof Sedan) {
            Sedan sedan = (Sedan) car;
            message.append("Engine: V").append(sedan.getEngine()).append("\n");
            message.append("Seats: ").append(sedan.getSeats()).append("\n");
            message.append("Type: Sedan\n");

        }
        if (car instanceof SUV) {
            SUV suv = (SUV) car;
            message.append("Weight: ").append(suv.getWeight()).append("\n");
            message.append("Color: ").append(suv.getColor()).append("\n");
            message.append("Type: SUV\n");

        }
        if (car instanceof Truck) {
            Truck truck = (Truck) car;
            message.append("Weight: ").append(truck.getWeight()).append("\n");
            message.append("Tires: ").append(truck.getTires()).append("\n");
            message.append("Type: Truck\n");

        }
        if (car instanceof Van) {
            Van van = (Van) car;
            message.append("Fuel Type: ").append(van.getFuelType()).append("\n");
            message.append("Seats: ").append(van.getSeats()).append("\n");
            message.append("Type: Van\n");

        }

        // Displaying the message using JOptionPane
        JOptionPane.showMessageDialog(null, message.toString(), "Vehicle Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import User.Dealer;

public class dealerView extends JFrame {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel topPanel;
    private JComboBox<String> carOption;
    private List<Car> cars;
    private String[] options = { "sedan", "suv", "truck", "van" };
    private Dealer dealer;

    public dealerView(Dealer dealer) {
        this.dealer = dealer;
        this.setTitle("Dealer View");
        this.setSize(900, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // Create top panel
        topPanel = new JPanel();
        JLabel userJLabel = new JLabel("Welcome dealer: " + dealer.getUsername());
        topPanel.add(userJLabel);

        // Create the left panel (larger) and set it as scrollable
        leftPanel = new JPanel();
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);

        // Create the right panel (smaller)
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(150, this.getHeight()));
        rightPanel.setBackground(Color.GRAY);

        carOption = new JComboBox<>(options);

        // display default car list
        disPlay_Car_List(options[0]);
        int rowForGrid = cars.size() < 2 ? 12 : 6;
        leftPanel.setLayout(new GridLayout(rowForGrid, 1, 0, 20));

        // Change list display when switch options
        carOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == carOption) {
                    cars.clear();
                    leftPanel.removeAll();
                    switch ((String) carOption.getSelectedItem()) {
                        case "sedan":
                            disPlay_Car_List("sedan");
                            break;
                        case "suv":
                            disPlay_Car_List("suv");
                            break;
                        case "truck":
                            disPlay_Car_List("truck");
                            break;
                        case "van":
                            disPlay_Car_List("van");
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        rightPanel.add(carOption);

        // Add car button
        JButton addButton = new JButton("Add car to sell");
        addButton.addActionListener((e1) -> {
            addCar();
        });
        rightPanel.add(addButton);

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
        cars = newConn.search_Car_ByColumn("dealer_name", dealer.getUsername());
        for (Car car : cars) {
            carDisplayPanel_for_Dealer newpanel = new carDisplayPanel_for_Dealer((String) carOption.getSelectedItem(),
                    car, dealerView.this);
            leftPanel.add(newpanel);
        }
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    void addCar() {
        JComboBox<String> car_View_comboBox = new JComboBox<>(options);
        car_View_comboBox.setPreferredSize(new Dimension(400, 50));

        JPanel panel = new JPanel();
        panel.add(car_View_comboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Choose a car type:",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String selectedOption_Car = (String) car_View_comboBox.getSelectedItem();
            @SuppressWarnings("unused")
            carInputView inputCarFrame = new carInputView(selectedOption_Car, dealer.getUsername(), this.leftPanel, dealerView.this);
        }
    }

    void removeCar(Integer carId, carDisplayPanel_for_Dealer panel) {
        dbConnectTo newCon = new dbConnectTo((String) carOption.getSelectedItem());
        newCon.removeCarData(carId);
        leftPanel.remove(panel);
        this.revalidate();
        this.repaint();
    }

    String getCarOption() {
        return (String) carOption.getSelectedItem();
    }

}

class carDisplayPanel_for_Dealer extends JPanel {
    private JLabel brandLabel;
    private JLabel sell_state_label;
    private JLabel carId;
    JLabel vehicle1;
    String vehicleType;
    JButton removeCarButton, carDeatail;

    carDisplayPanel_for_Dealer(String vehicleType, Car car, dealerView frame) {
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
        carId = new JLabel("ID: " + Integer.toString(car.getId()));
        Font font = new Font("Monospaced", Font.BOLD, 16); // Example: Arial, bold, size 18
        brandLabel.setFont(font);
        sell_state_label.setFont(font);
        vehicle1.setFont(font);
        carId.setFont(font);

        // Create button to remove car
        removeCarButton = new JButton("Remove");
        removeCarButton.addActionListener((e) -> {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this car?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // User confirmed delete
                frame.removeCar(car.getId(), this);
            }
        });

        // Create button to see car details
        carDeatail = new JButton("See details");
        carDeatail.addActionListener((e2) -> {
            show_car_details_for_dealer(car);
        });
        // Add labels to the panel
        this.add(vehicle1);
        this.add(brandLabel);
        this.add(carId);
        this.add(sell_state_label);
        this.add(carDeatail);
        this.add(removeCarButton);

    }

    void show_car_details_for_dealer(Car car) {
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

package view;

import javax.swing.*;

import Car.Car;
import Car.SUV;
import Car.Sedan;
import Car.Truck;
import Car.Van;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class carInputView extends JFrame {

    private JTextField brandTextField;
    private JTextField engineTextField;
    private JTextField seatsTextField;
    private JTextField weightField;
    private JTextField tiresField;
    private JComboBox<String> colorComboBox, fuelTypeBox;
    private String dealer_Name;
    private JPanel passedJpanel;
    private dealerView passedJFrame;

    public carInputView(String vehicle_type, String dealer, JPanel leftJPanel, dealerView frame) {
        this.dealer_Name = dealer;
        this.passedJpanel = leftJPanel;
        this.passedJFrame = frame;
        setTitle("Car Input");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));

        JLabel brandLabel = new JLabel("Car Brand:");
        brandTextField = new JTextField();

        JLabel engineLabel = new JLabel("Engine (number):");
        engineTextField = new JTextField();

        JLabel seatsLabel = new JLabel("Number of Seats:");
        seatsTextField = new JTextField();

        JLabel weightLabel = new JLabel("Car weight (number): ");
        weightField = new JTextField();

        JLabel fuelTypeLabel = new JLabel("Fuel Type:");
        String[] fuelType = { "Diesel Oil", "Biodiesel", "Gasoline", "EV" };
        fuelTypeBox = new JComboBox<>(fuelType);

        JLabel tiresLabel = new JLabel("Number of tires:");
        tiresField = new JTextField();

        String[] colors = { "red", "black", "pink", "purple", "white", "yellow" };
        colorComboBox = new JComboBox<>(colors);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput(vehicle_type)) {
                    submitButtonClicked(vehicle_type);
                } else {
                    JOptionPane.showMessageDialog(carInputView.this,
                            "Please fill in all the fields with correct data type.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(brandLabel);
        panel.add(brandTextField);

        if (vehicle_type == "truck") {
            panel.add(weightLabel);
            panel.add(weightField);
            panel.add(tiresLabel);
            panel.add(tiresField);
        } else if (vehicle_type == "sedan") {
            panel.add(engineLabel);
            panel.add(engineTextField);
            panel.add(seatsLabel);
            panel.add(seatsTextField);
        } else if (vehicle_type == "suv") {
            panel.add(weightLabel);
            panel.add(weightField);
            panel.add(new JLabel("Color: "));
            panel.add(colorComboBox);
        } else {
            panel.add(fuelTypeLabel);
            panel.add(fuelTypeBox);
            panel.add(seatsLabel);
            panel.add(seatsTextField);
        }
        panel.add(new JLabel()); // Placeholder for empty cell
        panel.add(submitButton);

        this.add(panel);
        setVisible(true);
    }

    boolean validateInput(String vehicle_type) {
        if (vehicle_type == "truck") {
            return !(brandTextField.getText().isEmpty() || weightField.getText().isEmpty()
                    || isAlpha(weightField.getText())
                    || tiresField.getText().isEmpty());
        } else if (vehicle_type == "sedan") {
            return !(brandTextField.getText().isEmpty() || engineTextField.getText().isEmpty()
                    || isAlpha(engineTextField.getText())
                    || seatsTextField.getText().isEmpty() || isAlpha(seatsTextField.getText()));
        } else if (vehicle_type == "suv") {
            return !(brandTextField.getText().isEmpty() || weightField.getText().isEmpty()
                    || isAlpha(weightField.getText()));
        } else {
            return !(brandTextField.getText().isEmpty() || seatsTextField.getText().isEmpty()
                    || isAlpha(seatsTextField.getText()));
        }
    }

    public static boolean isAlpha(String str) {
        return str != null && str.matches("[a-zA-Z]+");
    }

    void submitButtonClicked(String vehicle_type) {
        dbConnectTo newCon1 = new dbConnectTo(vehicle_type);
        Car car = new Car();
        switch (vehicle_type) {
            case "sedan":
                car = new Sedan(Double.parseDouble(engineTextField.getText()),
                        Integer.parseInt(seatsTextField.getText()), 0, brandTextField.getText(), dealer_Name, true);

                break;
            case "suv":
                car = new SUV(Double.parseDouble(weightField.getText()),
                        (String) colorComboBox.getSelectedItem(), 0, brandTextField.getText(), dealer_Name, true);

                break;
            case "truck":
                car = new Truck(Double.parseDouble(weightField.getText()),
                        Integer.parseInt(tiresField.getText()), 0, brandTextField.getText(), dealer_Name, true);

                break;
            case "van":
                car = new Van(Integer.parseInt(seatsTextField.getText()),
                        (String) fuelTypeBox.getSelectedItem(), 0, brandTextField.getText(), dealer_Name, true);

                break;
            default:
                break;
        }
        if (newCon1.addCarData(car)) {
            passedJpanel.removeAll();
            List<Car> cars;
            dbConnectTo newCon = new dbConnectTo(passedJFrame.getCarOption());
            cars = newCon.search_Car_ByColumn("dealer_name", dealer_Name);
        for (Car car1 : cars) {
            carDisplayPanel_for_Dealer newpanel = new carDisplayPanel_for_Dealer(passedJFrame.getCarOption(),
                    car1, passedJFrame);
            passedJpanel.add(newpanel);
        }
        passedJpanel.revalidate();
        passedJpanel.repaint();
        }
        carInputView.this.dispose();
    }

}

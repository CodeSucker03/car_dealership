package loginPrj;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import User.Admin;
import User.Customer;
import User.Dealer;
import User.User;

public class Login_View extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JComboBox<String> log_In_select_box;
    // Constructor

    public Login_View() {
        this.setTitle("Login");
        this.setSize(800, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(4, 2));

        JLabel usernameLabel = new JLabel(" Username:");
        JLabel passwordLabel = new JLabel(" Password:");
        JLabel registerLabel = new JLabel(" Dont have an account?");
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        loginButton = new JButton("Login as");
        registerButton = new JButton("Register now");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == loginButton) {
                    loginAction(Login_View.this);
                }
            }

        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e2) {
                if (e2.getSource() == registerButton) {
                    // check log in
                    String[] options = { "customer", "dealer", "admin" };
                    JPanel mainpanel = new JPanel();
                    JComboBox<String> userTypeComboBox = new JComboBox<>(options);
                    mainpanel.add(userTypeComboBox);
                    int result = JOptionPane.showConfirmDialog(null, mainpanel, "Register as",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        registerAction((String) userTypeComboBox.getSelectedItem());
                    }
                }
            }
        });

        String[] values = { "customer", "dealer", "admin" };
        log_In_select_box = new JComboBox<>(values);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(250, 200, 250, 200)); // Add some padding
        mainPanel.add(labelPanel, BorderLayout.CENTER); // Add child panel to main panel
        mainPanel.setBackground(Color.LIGHT_GRAY);
        labelPanel.add(usernameLabel);
        labelPanel.add(usernameField);
        labelPanel.add(passwordLabel);
        labelPanel.add(passwordField);
        labelPanel.add(loginButton);
        labelPanel.add(log_In_select_box);
        labelPanel.add(registerLabel);
        labelPanel.add(registerButton);

        this.add(mainPanel);
        setVisible(true);
    }

    public void registerAction(String userType) {
        // Create the JPanel with components
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JLabel emailLabel = new JLabel("Email: "), countryLabel = new JLabel("Country: ");
        JTextField emailField = new JTextField(12);
        JTextField countryField = new JTextField(12);
        // Add components to the panel
        // Show the JOptionPane with the custom panel
        // Check if OK button was clicked
        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        if (userType.equals("customer")) {
            panel.add(emailLabel);
            panel.add(emailField);
            boolean validInput1 = false;

            while (!validInput1) {
                int result2 = JOptionPane.showConfirmDialog(null, panel, "Register as Customer",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result2 == JOptionPane.CANCEL_OPTION) {
                    // Exit the loop if the user clicks "Cancel"
                    break;
                }
                if (usernameField.getText().isEmpty() || new String(passwordField.getPassword()).isEmpty()
                        || emailField.getText().isEmpty()) {
                    // Show an error message if any field is empty
                    JOptionPane.showMessageDialog(null, "All fields must be filled out.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {

                    Customer customer = new Customer(0, usernameField.getText(),
                            new String(passwordField.getPassword()), emailField.getText());
                    dbConnectTo newCon = new dbConnectTo("customer");
                    if (newCon.addCustomer(customer)) {
                        validInput1 = true;
                    } else {
                        registerAction(userType);
                    }
                }
            }

        }
        if (userType.equals("dealer")) {
            panel.add(countryLabel);
            panel.add(countryField);
            boolean validInput2 = false;

            while (!validInput2) {
                int result2 = JOptionPane.showConfirmDialog(null, panel, "Register as Dealer",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result2 == JOptionPane.CANCEL_OPTION) {
                    // Exit the loop if the user clicks "Cancel"
                    break;
                }
                // Check if any of the fields are empty
                if (usernameField.getText().isEmpty() || new String(passwordField.getPassword()).isEmpty()
                        || countryField.getText().isEmpty()) {
                    // Show an error message if any field is empty
                    JOptionPane.showMessageDialog(null, "All fields must be filled out.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Proceed with registration if all fields are filled
                    Dealer dealer = new Dealer(0, usernameField.getText(), new String(passwordField.getPassword()),
                            countryField.getText());
                    dbConnectTo newCon = new dbConnectTo("dealer");

                    if (newCon.addDealer(dealer)) {
                        validInput2 = true; // Exit the loop if dealer is added successfully
                    } else {
                        registerAction(userType); // Retry registration if addDealer fails
                    }
                }
            }
        }
        if (userType.equals("admin")) {
            boolean validInput3 = false;

            while (!validInput3) {
                int result2 = JOptionPane.showConfirmDialog(null, panel, "Register as Admin",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result2 == JOptionPane.CANCEL_OPTION) {
                    // Exit the loop if the user clicks "Cancel"
                    break;
                }
                if (usernameField.getText().isEmpty() || new String(passwordField.getPassword()).isEmpty()) {
                    // Show an error message if any field is empty
                    JOptionPane.showMessageDialog(null, "All fields must be filled out.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    Admin admin = new Admin(usernameField.getText(), new String(passwordField.getPassword()), 0);
                    dbConnectTo newCon = new dbConnectTo("admin");
                    if (newCon.addADmin(admin)) {
                        validInput3 = true;
                    } else {
                        registerAction(userType); // Retry registration if addDealer fails
                    }
                }

            }
        }

    }

    public Boolean fillallInput(JPasswordField passwordField, JTextField userNamField, JTextField emailField,
            JTextField countryField) {
        return !(userNamField.getText().isEmpty()) && !(new String(passwordField.getPassword()).isEmpty())
                && !(emailField.getText().isEmpty());
    }

    @SuppressWarnings("unused")
    public void loginAction(JFrame frame) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String log_In_As = (String) log_In_select_box.getSelectedItem();
        dbConnectTo newCon = new dbConnectTo(log_In_As);
        User newUser = newCon.log_In_Verify(username, password);
        System.out.println(newUser.getUsername());
        // No matching username and password found in the database

        if (!newUser.isEmty()) {
            frame.dispose();
            if (log_In_As == "customer") {
                Customer newCus = (Customer) newUser;
                Login_View.this.dispose();
                customerView newCustomerView = new customerView(newCus);
            } else if (log_In_As == "dealer") {
                Login_View.this.dispose();
                Dealer newDealer = (Dealer) newUser;
                dealerView newdDealerView = new dealerView(newDealer);
            } else {
                Admin admin = (Admin) newUser;
                adminView newAdminView = new adminView(admin);
            }
        } else {
            JOptionPane.showMessageDialog(Login_View.this, "Invalid username or password");
        }
    }
}
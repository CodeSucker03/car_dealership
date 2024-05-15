package loginPrj;

import javax.swing.*;

import User.Admin;
import User.Customer;
import User.Dealer;
import User.User;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class adminView extends JFrame {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel topPanel;
    private List<User> users;
    private JComboBox<String> Switch_dealer_and_customer;

    public adminView(Admin admin) {
        this.setTitle("Customer View");
        this.setSize(900, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // Create top panel
        topPanel = new JPanel();
        JLabel userJLabel = new JLabel("Welcome admin");
        topPanel.add(userJLabel);

        // Create the left panel (larger) and set it as scrollable
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        JScrollPane leftScrollPane = new JScrollPane(leftPanel);

        // Add panels to left panel
        dbConnectTo newCon = new dbConnectTo("customer");// display default list
        users = newCon.search_By_UserName(null);
        for (User eachUser : users) {
            userPanels panel = new userPanels(eachUser, this);
            leftPanel.add(panel);

        }
        int rowForGrid = users.size() < 2 ? 10 : 4;
        leftPanel.setLayout(new GridLayout(rowForGrid, 1, 0, 20));
        // Create the right panel (smaller)
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(150, this.getHeight()));
        rightPanel.setBackground(Color.GRAY);

        JLabel searchLabel = new JLabel("Search Key:");
        JTextField searchField = new JTextField(13);
        rightPanel.add(searchLabel);
        rightPanel.add(searchField);

        String[] values = { "customer", "dealer","admin" };
        Switch_dealer_and_customer = new JComboBox<>(values);
        rightPanel.add(Switch_dealer_and_customer);

        JButton addButton = new JButton("Search");
        rightPanel.add(addButton);

        addButton.addActionListener((e1) -> {
            String searchKey = searchField.getText();
            if (searchKey.isBlank()) {
                searchKey = null;
            }
            String getSearchBox = (String) Switch_dealer_and_customer.getSelectedItem(); // get search string
            // clear the left panel content
            leftPanel.removeAll();
            users.clear();
            leftPanel.revalidate();
            leftPanel.repaint();
            // search for the user
            dbConnectTo newCon2 = new dbConnectTo(getSearchBox);
            users = newCon2.search_By_UserName(searchKey);
            for (User eachUser : users) {
                userPanels panel = new userPanels(eachUser, this);
                leftPanel.add(panel);
            }
            leftPanel.revalidate();
            leftPanel.repaint();

        });

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

    public void removePanel(userPanels panel, int user_id) {
        leftPanel.remove(panel); // Remove the panel from leftPanel
        dbConnectTo newCon = new dbConnectTo((String) Switch_dealer_and_customer.getSelectedItem());
        newCon.removeUserData(user_id);
        revalidate();
        repaint();
    }
}

class userPanels extends JPanel {
    private JLabel usernameLabel, idLabel, emailLabel, countryLabel;

    public userPanels(User user, adminView frame) {
        // Set layout for the panel
        this.setPreferredSize(new Dimension(300, 70));
        setLayout(new GridLayout(1, 4, 0, 5)); // 1 rows, 4 columns, with 5px horizontal and vertical gap

        // Create and configure labels
        usernameLabel = new JLabel("Username: " + user.getUsername());
        idLabel = new JLabel("ID: " + user.getId());
        // Add labels to the panel
        add(usernameLabel);
        add(idLabel);
        if (user instanceof Customer) {
            Customer cutomer = (Customer) user;
            emailLabel = new JLabel("Email: " + cutomer.getEmail());
            add(emailLabel);
        }
        if (user instanceof Dealer) {
            Dealer dealer = (Dealer) user;
            countryLabel = new JLabel("Country: " + dealer.getCountry());
            add(countryLabel);
        }

        JButton deleteButton = new JButton("Remove");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete?",
                        "Delete Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    frame.removePanel(userPanels.this, user.getId());
                }
            }
        });
        add(deleteButton);
    }
}
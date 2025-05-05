import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class RestaurantManager extends JFrame implements ActionListener {
    JTextField nameField, itemField, quantityField, tableField, contactField;
    JButton insertButton, displayButton, clearButton;

    public RestaurantManager() {
        setTitle("Restaurant Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);

        JLabel[] labels = {
                new JLabel("Customer Name:"), new JLabel("Food Item:"),
                new JLabel("Quantity:"), new JLabel("Table No:"), new JLabel("Contact:")
        };

        JTextField[] fields = {
                nameField = new JTextField(15),
                itemField = new JTextField(15),
                quantityField = new JTextField(15),
                tableField = new JTextField(15),
                contactField = new JTextField(15)
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            add(labels[i], gbc);
            gbc.gridx = 1;
            add(fields[i], gbc);
        }

        insertButton = new JButton("Insert");
        displayButton = new JButton("Display");
        clearButton = new JButton("Clear");

        JPanel panel = new JPanel();
        panel.add(insertButton);
        panel.add(displayButton);
        panel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(panel, gbc);

        insertButton.addActionListener(this);
        displayButton.addActionListener(this);
        clearButton.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/restaurantdb?useSSL=false&allowPublicKeyRetrieval=true";
            String user = "root";
            String pass = "luffy"; // Change to your MySQL password
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Connection Error:\n" + e.getMessage());
            return null;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insertButton) {
            insertOrder();
        } else if (e.getSource() == displayButton) {
            displayOrders();
        } else {
            clearFields();
        }
    }

    private void insertOrder() {
        String name = nameField.getText();
        String item = itemField.getText();
        String quantityText = quantityField.getText();
        String table = tableField.getText();
        String contact = contactField.getText();

        if (name.isEmpty() || item.isEmpty() || quantityText.isEmpty() || table.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        try (Connection conn = connect()) {
            int quantity = Integer.parseInt(quantityText);
            String sql = "INSERT INTO orders (customer_name, food_item, quantity, table_number, contact) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, item);
            ps.setInt(3, quantity);
            ps.setString(4, table);
            ps.setString(5, contact);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Order inserted successfully.");
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inserting order: " + ex.getMessage());
        }
    }

    private void displayOrders() {
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM orders";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            StringBuilder data = new StringBuilder();
            while (rs.next()) {
                data.append("ID: ").append(rs.getInt("orderid"))
                        .append(", Name: ").append(rs.getString("customer_name"))
                        .append(", Item: ").append(rs.getString("food_item"))
                        .append(", Qty: ").append(rs.getInt("quantity"))
                        .append(", Table: ").append(rs.getString("table_number"))
                        .append(", Contact: ").append(rs.getString("contact")).append("\n");
            }
            if (data.length() == 0) {
                JOptionPane.showMessageDialog(this, "No orders found.");
            } else {
                JOptionPane.showMessageDialog(this, data.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving orders: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        itemField.setText("");
        quantityField.setText("");
        tableField.setText("");
        contactField.setText("");
    }

    public static void main(String[] args) {
        new RestaurantManager();
    }
}

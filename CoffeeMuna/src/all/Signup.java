package all;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Signup extends JFrame {
    public Signup() {
        setTitle("Sign Up - CoffeeMuna");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton registerButton = new JButton("Register");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(registerButton);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
                new Login();
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists or error occurred.");
            }
        });

        setVisible(true);
    }

    private boolean registerUser(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}


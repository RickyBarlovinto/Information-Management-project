package all;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {

    public Login() {
        setTitle("Login - CoffeeMuna");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));


        JPanel centerPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        centerPanel.add(new JLabel("Username:"));
        centerPanel.add(usernameField);
        centerPanel.add(new JLabel("Password:"));
        centerPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        centerPanel.add(buttonPanel);

        add(centerPanel, BorderLayout.CENTER);


        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new Dashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            }
        });


        signupButton.addActionListener(e -> {
            dispose();
            new Signup();
        });

        setVisible(true);
    }

    private boolean authenticateUser(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

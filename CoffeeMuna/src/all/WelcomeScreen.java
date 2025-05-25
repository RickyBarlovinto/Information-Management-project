package all;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("Welcome - CoffeeMuna");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));


        JLabel welcomeLabel = new JLabel("Welcome to CoffeeMuna", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(welcomeLabel, BorderLayout.CENTER);


        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);


        loginButton.addActionListener(e -> {
            dispose();
            new Login();
        });

        setVisible(true);
    }

}

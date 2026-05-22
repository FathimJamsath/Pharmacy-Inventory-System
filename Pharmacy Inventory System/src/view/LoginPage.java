package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame implements ActionListener {

    JLabel userLabel, passLabel, messageLabel;
    JTextField userText;
    JPasswordField passText;
    JButton loginButton;

    // Sample username and password
    String username = "admin";
    String password = "1234";

    LoginPage() {
        setTitle("Pharmacy Inventory System - Login");
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background image (UNCHANGED SIZE)
        ImageIcon bgIcon = new ImageIcon("src/resources/images/blur.png");
        JLabel background = new JLabel(bgIcon);
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        // Transparent panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // more spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Font for labels
        Font labelFont = new Font("Times new roman", Font.BOLD, 40);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        userText = new JTextField(25);
        userText.setPreferredSize(new Dimension(250, 35));
        panel.add(userText, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        passLabel = new JLabel("Password:");
        passLabel.setFont(labelFont);
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        passText = new JPasswordField(25);
        passText.setPreferredSize(new Dimension(250, 35));
        panel.add(passText, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(180, 50));
        loginButton.setFont(new Font("Times new roman", Font.BOLD, 40));
        loginButton.addActionListener(this);
        panel.add(loginButton, gbc);

        // Message
        gbc.gridy = 3;

        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        messageLabel.setOpaque(true); // IMPORTANT
        messageLabel.setBackground(new Color(255, 255, 255, 180));
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));

        panel.add(messageLabel, gbc);

        // Add panel to background
        background.add(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user = userText.getText();
        String pass = new String(passText.getPassword());

        if (user.equals(username) && pass.equals(password)) {
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Login Successful!");

            new Dashboard();   //  open dashboard
            dispose();         //  close login page
        }
        else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Invalid Username or Password!");
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
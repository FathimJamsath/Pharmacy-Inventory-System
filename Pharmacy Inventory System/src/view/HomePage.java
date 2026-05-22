package view;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {

    public HomePage() {
        setTitle("Pharmacy Inventory System");

        // Increase frame size (IMPORTANT)
        setSize(1600, 1000);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Image
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/resources/images/home.png"));
        JLabel background = new JLabel(bgIcon);
        background.setLayout(null);

        // Title
        JLabel title = new JLabel(" Pharmacy Inventory System");
        title.setBounds(120, 90, 1000, 110);
        title.setFont(new Font("Times New Roman", Font.BOLD, 80));

        title.setOpaque(true); // IMPORTANT
        title.setBackground(new Color(255, 255, 255, 180)); // text box background
        title.setForeground(new Color(0, 102, 153)); // text color

        background.add(title);

        // Subtitle
        JLabel subtitle = new JLabel("Manage your pharmacy stock efficiently");
        subtitle.setBounds(90, 200, 1080, 80);
        subtitle.setFont(new Font("Times New Roman", Font.BOLD, 60));

        subtitle.setOpaque(true); // IMPORTANT
        subtitle.setBackground(new Color(255, 255, 255, 180)); // same soft white box
        subtitle.setForeground(new Color(60, 60, 60)); // soft dark gray

        subtitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // spacing inside box

        background.add(subtitle);

        // Login Button
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(600, 600, 200, 50);
        loginBtn.setFont(new Font("Times new roman", Font.BOLD, 28));
        loginBtn.setBackground(new Color(134, 34, 34));
        loginBtn.setForeground(Color.WHITE);

        // Removing rectangle
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);

        loginBtn.addActionListener(e -> {
            new LoginPage();
            dispose();
        });

        background.add(loginBtn);

        setContentPane(background);

        setVisible(true);
    }
}
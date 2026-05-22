package view;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Pharmacy Inventory Dashboard");
        setSize(1600, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Background Image
        JLabel background = new JLabel(
                new ImageIcon(getClass().getResource("/resources/images/blur.png"))
        );
        background.setLayout(null);
        setContentPane(background);

        addComponents(background);

        setVisible(true);
    }

    public void addComponents(JLabel background) {

        // Title
        JLabel title = new JLabel("Pharmacy Inventory Dashboard");
        title.setFont(new Font("Times New Roman", Font.BOLD, 70));
        title.setForeground(new Color(0, 102, 153));
        title.setBounds(250, 30, 1200, 100);
        background.add(title);

        // ===== BUTTON STYLE =====
        Font btnFont = new Font("Times New Roman", Font.BOLD, 30);

        // ===== MEDICINE BUTTON =====
        JButton medicineBtn = new JButton("Medicine Management");
        medicineBtn.setBounds(740, 250, 350, 60);
        medicineBtn.setFont(btnFont);
        medicineBtn.setBackground(new Color(0, 102, 153));
        medicineBtn.setForeground(Color.WHITE);
        medicineBtn.setFocusPainted(false);
        background.add(medicineBtn);

        // ===== SUPPLIER BUTTON =====
        JButton supplierBtn = new JButton("Supplier Management");
        supplierBtn.setBounds(430, 350, 350, 60);
        supplierBtn.setFont(btnFont);
        supplierBtn.setBackground(new Color(0, 102, 153));
        supplierBtn.setForeground(Color.WHITE);
        supplierBtn.setFocusPainted(false);
        background.add(supplierBtn);

        // ===== STOCK BUTTON =====
        JButton stockBtn = new JButton("Stock Management");
        stockBtn.setBounds(780, 520, 300, 60);
        stockBtn.setFont(btnFont);
        stockBtn.setBackground(new Color(0, 102, 153));
        stockBtn.setForeground(Color.WHITE);
        stockBtn.setFocusPainted(false);
        background.add(stockBtn);

        // ===== SALES BUTTON =====
        JButton salesBtn = new JButton("Sales Management");
        salesBtn.setBounds(430, 620, 300, 60);
        salesBtn.setFont(btnFont);
        salesBtn.setBackground(new Color(0, 102, 153));
        salesBtn.setForeground(Color.WHITE);
        salesBtn.setFocusPainted(false);
        background.add(salesBtn);

        // ===== ACTION LISTENERS =====

        // Supplier
        supplierBtn.addActionListener(e -> {
            new SupplierManagement();
            dispose();
        });

        // Medicine
        medicineBtn.addActionListener(e -> {
            new MedicineManagement();
            dispose();
        });

        // Stock
        stockBtn.addActionListener(e -> {
            new StockManagement();
            dispose();
        });

        // 🔥 SALES (THIS WAS MISSING)
        salesBtn.addActionListener(e -> {
            new SalesManagement(); // your sales GUI class
            dispose();
        });

        // ===== ICONS =====

        JLabel userIcon = new JLabel(resizeIcon("/resources/images/supplier.png", 150, 150));
        userIcon.setBounds(250, 300, 150, 150);
        background.add(userIcon);

        JLabel billIcon = new JLabel(resizeIcon("/resources/images/billing.png", 150, 150));
        billIcon.setBounds(250, 570, 150, 150);
        background.add(billIcon);

        JLabel medIcon = new JLabel(resizeIcon("/resources/images/medicine.png", 150, 150));
        medIcon.setBounds(1100, 200, 150, 150);
        background.add(medIcon);

        JLabel stockIcon = new JLabel(resizeIcon("/resources/images/stock.png", 150, 150));
        stockIcon.setBounds(1100, 470, 150, 150);
        background.add(stockIcon);
    }

    // ===== IMAGE RESIZE METHOD =====
    private ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
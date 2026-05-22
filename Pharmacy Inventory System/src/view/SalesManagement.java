package view;

import database.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;

public class SalesManagement extends JFrame implements ActionListener {

    JTextField txtMedName, txtMedId, txtQty, txtPrice, txtTotal;
    JLabel lblGrandTotal;
    JButton btnAdd, btnClear;
    JTable table;
    DefaultTableModel model;

    double grandTotal = 0;

    public SalesManagement() {

        setTitle("Sales Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel background = new JLabel(
                new ImageIcon(getClass().getResource("/resources/images/blur.png"))
        );
        setContentPane(background);
        background.setLayout(null);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Font textFont = new Font("Segoe UI", Font.BOLD, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);

        // ===== LABELS =====
        JLabel lblMedName = new JLabel("Medicine Name:");
        JLabel lblMedId = new JLabel("Medicine ID:");
        JLabel lblQty = new JLabel("Quantity:");
        JLabel lblPrice = new JLabel("Unit Price:");
        JLabel lblTotal = new JLabel("Total:");

        JLabel[] labels = {lblMedName, lblMedId, lblQty, lblPrice, lblTotal};

        int y = 120;
        for (JLabel lbl : labels) {
            lbl.setFont(labelFont);
            lbl.setBounds(120, y, 150, 30);
            background.add(lbl);
            y += 80;
        }

        // ===== TEXTFIELDS =====
        txtMedName = new JTextField();
        txtMedId = new JTextField();
        txtMedId.setEditable(false);

        txtQty = new JTextField();
        txtPrice = new JTextField();
        txtTotal = new JTextField();
        txtTotal.setEditable(false);

        JTextField[] fields = {txtMedName, txtMedId, txtQty, txtPrice, txtTotal};

        y = 120;
        for (JTextField tf : fields) {
            tf.setFont(textFont);
            tf.setBounds(300, y, 300, 40);
            background.add(tf);
            y += 80;
        }

        // ===== AUTO FETCH MEDICINE ID =====
        txtMedName.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                fetchMedicineId();
            }
        });

        // ===== AUTO CALCULATE TOTAL =====
        KeyAdapter calculateListener = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                calculateTotal();
            }
        };

        txtQty.addKeyListener(calculateListener);
        txtPrice.addKeyListener(calculateListener);

        // ===== BUTTONS =====
        btnAdd = new JButton("Add Item");
        btnClear = new JButton("Clear");

        btnAdd.setFont(buttonFont);
        btnAdd.setBounds(150, 500, 180, 50);
        background.add(btnAdd);

        btnClear.setFont(buttonFont);
        btnClear.setBounds(350, 500, 180, 50);
        background.add(btnClear);

        btnAdd.addActionListener(this);
        btnClear.addActionListener(this);

        // ===== TABLE =====
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "Medicine ID", "Quantity", "Unit Price", "Total Price"
        });

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setRowHeight(28);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(700, 120, 600, 400);
        background.add(scrollPane);

        // ===== GRAND TOTAL =====
        lblGrandTotal = new JLabel("Grand Total: 0.00");
        lblGrandTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblGrandTotal.setBounds(700, 550, 400, 40);
        background.add(lblGrandTotal);

        // ===== BACK BUTTON =====
        JButton btnBack = new JButton("Back");
        btnBack.setFont(buttonFont);
        btnBack.setBounds(700, 700, 200, 50);
        background.add(btnBack);

        btnBack.addActionListener(e -> {
            new Dashboard();
            dispose();
        });

        setVisible(true);
    }

    // ===== FETCH MEDICINE ID =====
    void fetchMedicineId() {
        String name = txtMedName.getText().trim();

        if (name.isEmpty()) {
            txtMedId.setText("");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT med_id FROM medicine WHERE name LIKE ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name + "%");

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtMedId.setText(String.valueOf(rs.getInt("med_id")));
            } else {
                txtMedId.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== AUTO TOTAL =====
    void calculateTotal() {
        try {
            int qty = Integer.parseInt(txtQty.getText());
            double price = Double.parseDouble(txtPrice.getText());

            double total = qty * price;
            txtTotal.setText(String.format("%.2f", total));

        } catch (Exception e) {
            txtTotal.setText("");
        }
    }

    // ===== ACTIONS =====
    public void actionPerformed(ActionEvent e) {

        String medId = txtMedId.getText().trim();
        String qtyText = txtQty.getText().trim();
        String priceText = txtPrice.getText().trim();

        if (medId.isEmpty() || qtyText.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyText);
            double unitPrice = Double.parseDouble(priceText);

            if (quantity <= 0 || unitPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Values must be greater than 0!");
                return;
            }

            double total = quantity * unitPrice;

            if (e.getSource() == btnAdd) {

                model.addRow(new Object[]{
                        medId, quantity, unitPrice, total
                });

                grandTotal += total;
                lblGrandTotal.setText("Grand Total: " + String.format("%.2f", grandTotal));

                Connection con = DBConnection.getConnection();

                String query = "INSERT INTO sales(med_id, quantity, total_price, sale_date) VALUES (?, ?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, Integer.parseInt(medId));
                ps.setInt(2, quantity);
                ps.setDouble(3, total);
                ps.setDate(4, new java.sql.Date(new Date().getTime()));

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Item Added!");

                clearFields();
            }

            if (e.getSource() == btnClear) {
                clearFields();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric values!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error!");
        }
    }

    void clearFields() {
        txtMedName.setText("");
        txtMedId.setText("");
        txtQty.setText("");
        txtPrice.setText("");
        txtTotal.setText("");
    }
}
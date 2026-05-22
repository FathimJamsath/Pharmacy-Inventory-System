package view;

import database.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StockManagement extends JFrame implements ActionListener {

    JTextField txtStockId, txtMedId, txtQty;
    JButton btnAdd, btnUpdate, btnDelete, btnClear;
    JTable table;
    DefaultTableModel model;

    public StockManagement() {

        setTitle("Stock Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel background = new JLabel(
                new ImageIcon(getClass().getResource("/resources/images/blur.png"))
        );
        setContentPane(background);
        background.setLayout(null);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Font textFont = new Font("Segoe UI", Font.BOLD, 16); //
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);

        // ===== LABELS =====
        JLabel lblStockId = new JLabel("Stock ID:");
        JLabel lblMedId = new JLabel("Medicine ID:");
        JLabel lblQty = new JLabel("Quantity:");

        JLabel[] labels = {lblStockId, lblMedId, lblQty};

        int y = 120;
        for (JLabel lbl : labels) {
            lbl.setFont(labelFont);
            lbl.setBounds(120, y, 150, 30);
            background.add(lbl);
            y += 80;
        }

        // ===== TEXTFIELDS =====
        txtStockId = new JTextField();
        txtStockId.setEditable(false);

        txtMedId = new JTextField();
        txtQty = new JTextField();

        JTextField[] fields = {txtStockId, txtMedId, txtQty};

        y = 120;
        for (JTextField tf : fields) {
            tf.setFont(textFont);
            tf.setBounds(300, y, 300, 40);
            background.add(tf);
            y += 80;
        }

        // ===== BUTTONS =====
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        JButton[] buttons = {btnAdd, btnUpdate, btnDelete, btnClear};

        int x = 120;
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBounds(x, 400, 140, 50);
            background.add(btn);
            x += 150;
        }

        // ===== BACK BUTTON =====
        JButton btnBack = new JButton("Back");
        btnBack.setFont(buttonFont);
        btnBack.setBounds(700, 650, 200, 50);
        background.add(btnBack);

        btnBack.addActionListener(e -> {
            new Dashboard();
            dispose();
        });

        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
        btnClear.addActionListener(this);

        // ===== TABLE =====
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "Stock ID", "Medicine ID", "Quantity"
        });

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setRowHeight(30);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18)); // 🔥 BOLD HEADER
        header.setForeground(Color.BLACK); // optional

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(750, 120, 600, 400);
        background.add(scrollPane);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();
                txtStockId.setText(model.getValueAt(i, 0).toString());
                txtMedId.setText(model.getValueAt(i, 1).toString());
                txtQty.setText(model.getValueAt(i, 2).toString());
            }
        });

        loadTable();

        setVisible(true);
    }

    // ===== LOAD TABLE =====
    void loadTable() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM stock";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("stock_id"),
                        rs.getInt("med_id"),
                        rs.getInt("quantity")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== ACTIONS =====
    public void actionPerformed(ActionEvent e) {

        String id = txtStockId.getText();
        String medId = txtMedId.getText();
        String qty = txtQty.getText();

        try {
            Connection con = DBConnection.getConnection();

            // ADD
            if (e.getSource() == btnAdd) {

                String query = "INSERT INTO stock(med_id, quantity) VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, Integer.parseInt(medId));
                ps.setInt(2, Integer.parseInt(qty));

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Stock Added!");
                loadTable();
                clearFields();
            }

            // UPDATE
            if (e.getSource() == btnUpdate) {

                String query = "UPDATE stock SET med_id=?, quantity=? WHERE stock_id=?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, Integer.parseInt(medId));
                ps.setInt(2, Integer.parseInt(qty));
                ps.setInt(3, Integer.parseInt(id));

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Updated!");
                loadTable();
                clearFields();
            }

            // DELETE
            if (e.getSource() == btnDelete) {

                String query = "DELETE FROM stock WHERE stock_id=?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, Integer.parseInt(id));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Deleted!");
                loadTable();
                clearFields();
            }

            // CLEAR
            if (e.getSource() == btnClear) {
                clearFields();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void clearFields() {
        txtStockId.setText("");
        txtMedId.setText("");
        txtQty.setText("");
    }
}
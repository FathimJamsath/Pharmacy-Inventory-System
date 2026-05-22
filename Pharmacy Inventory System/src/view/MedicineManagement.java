package view;

import database.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MedicineManagement extends JFrame implements ActionListener {

    JTextField txtId, txtName, txtCategory, txtPrice, txtExpiry;
    JButton btnAdd, btnUpdate, btnDelete, btnClear;
    JTable table;
    DefaultTableModel model;

    public MedicineManagement() {

        setTitle("Medicine Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel background = new JLabel(
                new ImageIcon(getClass().getResource("/resources/images/blur.png"))
        );
        setContentPane(background);
        background.setLayout(null);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);

        // ===== LABELS =====
        JLabel lblId = new JLabel("Medicine ID:");
        JLabel lblName = new JLabel("Name:");
        JLabel lblCategory = new JLabel("Category:");
        JLabel lblPrice = new JLabel("Price:");
        JLabel lblExpiry = new JLabel("Expiry Date:");

        JLabel[] labels = {lblId, lblName, lblCategory, lblPrice, lblExpiry};

        int y = 90;
        for (JLabel lbl : labels) {
            lbl.setFont(labelFont);
            lbl.setBounds(120, y, 150, 30);
            background.add(lbl);
            y += 70;
        }

        // ===== TEXTFIELDS =====
        txtId = new JTextField();
        txtId.setEditable(false); // AUTO_INCREMENT

        txtName = new JTextField();
        txtCategory = new JTextField();
        txtPrice = new JTextField();
        txtExpiry = new JTextField();

        JTextField[] fields = {txtId, txtName, txtCategory, txtPrice, txtExpiry};

        y = 90;
        for (JTextField tf : fields) {
            tf.setFont(textFont);
            tf.setBounds(270, y, 300, 35);
            background.add(tf);
            y += 70;
        }

        // ===== BUTTONS =====
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        JButton[] buttons = {btnAdd, btnUpdate, btnDelete, btnClear};

        int x = 40;
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBounds(x, 450, 130, 40);
            background.add(btn);
            x += 150;
        }

        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
        btnClear.addActionListener(this);

        // ===== TABLE =====
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "ID", "Name", "Category", "Price", "Expiry Date"
        });

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(650, 90, 650, 450);
        background.add(scrollPane);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();
                txtId.setText(model.getValueAt(i, 0).toString());
                txtName.setText(model.getValueAt(i, 1).toString());
                txtCategory.setText(model.getValueAt(i, 2).toString());
                txtPrice.setText(model.getValueAt(i, 3).toString());
                txtExpiry.setText(model.getValueAt(i, 4).toString());
            }
        });

        // ===== BACK BUTTON =====
        JButton btnBack = new JButton("Back");
        btnBack.setFont(buttonFont);
        btnBack.setBounds(700, 650, 200, 50);
        background.add(btnBack);

        btnBack.addActionListener(e -> {
            new Dashboard();
            dispose();
        });

        loadTable();

        setVisible(true);
    }

    // ===== LOAD TABLE =====
    void loadTable() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM medicine";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("med_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        String.format("%.2f", rs.getDouble("price")),
                        rs.getString("expiry_date")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== ACTIONS =====
    public void actionPerformed(ActionEvent e) {

        String id = txtId.getText();
        String name = txtName.getText();
        String category = txtCategory.getText();
        String price = txtPrice.getText();
        String expiry = txtExpiry.getText();

        try {
            Connection con = DBConnection.getConnection();

            // ADD
            if (e.getSource() == btnAdd) {
                String query = "INSERT INTO medicine(name, category, price, expiry_date) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setString(1, name);
                ps.setString(2, category);
                ps.setDouble(3, Double.parseDouble(price));
                ps.setString(4, expiry);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Medicine Added!");
                loadTable();
                clearFields();
            }

            // UPDATE
            if (e.getSource() == btnUpdate) {
                String query = "UPDATE medicine SET name=?, category=?, price=?, expiry_date=? WHERE med_id=?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setString(1, name);
                ps.setString(2, category);
                ps.setDouble(3, Double.parseDouble(price));
                ps.setString(4, expiry);
                ps.setString(5, id);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Updated!");
                loadTable();
                clearFields();
            }

            // DELETE
            if (e.getSource() == btnDelete) {
                String query = "DELETE FROM medicine WHERE med_id=?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setString(1, id);
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
        txtId.setText("");
        txtName.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
        txtExpiry.setText("");
    }
}
package view;

import database.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SupplierManagement extends JFrame implements ActionListener {

    JTextField txtId, txtName, txtContact, txtAddress;
    JButton btnAdd, btnUpdate, btnDelete, btnClear;
    JTable table;
    DefaultTableModel model;

    public SupplierManagement() {
        setTitle("Supplier Management - Pharmacy System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel background = new JLabel(new ImageIcon(getClass().getResource("/resources/images/blur.png")));
        setContentPane(background);
        background.setLayout(null);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);

        JLabel lblId = new JLabel("supplier_Id:");
        JLabel lblName = new JLabel("name:");
        JLabel lblContact = new JLabel("contact:");
        JLabel lblAddress = new JLabel("address:");

        JLabel[] labels = {lblId, lblName, lblContact, lblAddress};

        int y = 90;
        for (JLabel lbl : labels) {
            lbl.setFont(labelFont);
            lbl.setBounds(120, y, 150, 30);
            background.add(lbl);
            y += 70;
        }

        txtId = new JTextField();
        txtId.setEditable(false);

        txtName = new JTextField();
        txtContact = new JTextField();
        txtAddress = new JTextField();

        JTextField[] fields = {txtId, txtName, txtContact, txtAddress};

        y = 90;
        for (JTextField tf : fields) {
            tf.setFont(textFont);
            tf.setBounds(270, y, 300, 35);
            background.add(tf);
            y += 70;
        }

        // Buttons
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");

        JButton btnBack = new JButton("Back");

        // Button Array
        JButton[] buttons = {btnAdd, btnUpdate, btnDelete, btnClear};

        int x = 40;
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setBounds(x, 380, 120, 40);
            background.add(btn);
            x += 140;
        }

        btnBack.setFont(buttonFont);
        btnBack.setBounds(650, 700, 200, 50); // bottom center
        background.add(btnBack);

        btnBack.addActionListener(e -> {
            new Dashboard();
            dispose();
        });

        // Button Listeners
        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
        btnClear.addActionListener(this);
        btnBack.addActionListener(e -> {
            new Dashboard();
            dispose();
        });


        // Table
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Supplier_Id", "Name", "Contact", "Address"});

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
                txtContact.setText(model.getValueAt(i, 2).toString());
                txtAddress.setText(model.getValueAt(i, 3).toString());
            }
        });

        loadTable(); // 🔥 load data from DB

        setVisible(true);
    }

    // 🔥 LOAD TABLE FROM DATABASE
    void loadTable() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM supplier";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("supplier_id"),
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("address")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {

        String id = txtId.getText();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();

        try {
            Connection con = DBConnection.getConnection();

            // ADD
            if (e.getSource() == btnAdd) {
                String query = "INSERT INTO supplier (name, contact, address) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setString(1, name);
                ps.setString(2, contact);
                ps.setString(3, address);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Supplier Added!");
                loadTable();
                clearFields();
            }

            // UPDATE
            if (e.getSource() == btnUpdate) {
                String query = "UPDATE supplier SET name=?, contact=?, address=? WHERE supplier_id=?";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setString(1, name);
                ps.setString(2, contact);
                ps.setString(3, address);
                ps.setString(4, id);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Updated!");
                loadTable();
                clearFields();
            }

            // DELETE
            if (e.getSource() == btnDelete) {
                String query = "DELETE FROM supplier WHERE supplier_id=?";
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
        txtContact.setText("");
        txtAddress.setText("");
    }
}
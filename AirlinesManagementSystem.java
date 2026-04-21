package malathi_airlines;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AirlinesManagementSystem extends JFrame {

    JPanel formPanel;

    JTextField nameField, phoneField, seatField, classField, genderField, regField, idField;

    JTable table;
    DefaultTableModel model;

    Connection con;
    String currentTable = "";

    public AirlinesManagementSystem() {

        setTitle("Airlines Management System");
        setSize(900, 600);
        setLayout(new BorderLayout(10, 10));

        JPanel northPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Airlines Management System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        northPanel.add(title, BorderLayout.NORTH);

        JPanel topPanel = new JPanel();
        JButton passengerBtn = new JButton("Passenger");
        JButton airhosterBtn = new JButton("Airhoster");
        JButton officerBtn = new JButton("Officer");

        topPanel.add(passengerBtn);
        topPanel.add(airhosterBtn);
        topPanel.add(officerBtn);

        northPanel.add(topPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        formPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(formPanel, BorderLayout.WEST);

        Dimension fieldSize = new Dimension(150, 25);

        nameField = new JTextField();
        phoneField = new JTextField();
        seatField = new JTextField();
        classField = new JTextField();
        genderField = new JTextField();
        regField = new JTextField();
        idField = new JTextField();

        model = new DefaultTableModel();
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 🔥 TABLE CLICK
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row == -1) return;

                if (currentTable.equals("passenger_list")) {
                    nameField.setText(model.getValueAt(row, 0).toString());
                    seatField.setText(model.getValueAt(row, 1).toString());
                    phoneField.setText(model.getValueAt(row, 2).toString());
                    classField.setText(model.getValueAt(row, 3).toString());
                    genderField.setText(model.getValueAt(row, 4).toString());
                }
                else if (currentTable.equals("airhosters")) {
                    nameField.setText(model.getValueAt(row, 0).toString());
                    phoneField.setText(model.getValueAt(row, 1).toString());
                    regField.setText(model.getValueAt(row, 2).toString());
                }
                else if (currentTable.equals("officers")) {
                    idField.setText(model.getValueAt(row, 0).toString());
                    nameField.setText(model.getValueAt(row, 1).toString());
                    phoneField.setText(model.getValueAt(row, 2).toString());
                }
            }
        });

        JPanel bottom = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton viewBtn = new JButton("View");

        bottom.add(addBtn);
        bottom.add(updateBtn);
        bottom.add(deleteBtn);
        bottom.add(viewBtn);

        add(bottom, BorderLayout.SOUTH);

        connectDB();

        passengerBtn.addActionListener(e -> loadPassengerForm());
        airhosterBtn.addActionListener(e -> loadAirhosterForm());
        officerBtn.addActionListener(e -> loadOfficerForm());

        addBtn.addActionListener(e -> addRecord());
        updateBtn.addActionListener(e -> updateRecord());
        deleteBtn.addActionListener(e -> deleteRecord());
        viewBtn.addActionListener(e -> viewRecords());

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/malathi_airlines",
                    "root",
                    "Kani@2006"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    // ✅ STRICT PHONE VALIDATION
    boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[6-9]\\d{9}$");
    }

    void loadPassengerForm() {
        currentTable = "passenger_list";
        formPanel.removeAll();

        formPanel.add(new JLabel("Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Seats:")); formPanel.add(seatField);
        formPanel.add(new JLabel("Phone:")); formPanel.add(phoneField);
        formPanel.add(new JLabel("Class:")); formPanel.add(classField);
        formPanel.add(new JLabel("Gender:")); formPanel.add(genderField);

        refreshUI();
    }

    void loadAirhosterForm() {
        currentTable = "airhosters";
        formPanel.removeAll();

        formPanel.add(new JLabel("Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Phone:")); formPanel.add(phoneField);
        formPanel.add(new JLabel("Reg No:")); formPanel.add(regField);

        refreshUI();
    }

    void loadOfficerForm() {
        currentTable = "officers";
        formPanel.removeAll();

        formPanel.add(new JLabel("ID:")); formPanel.add(idField);
        formPanel.add(new JLabel("Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Phone:")); formPanel.add(phoneField);

        refreshUI();
    }

    void refreshUI() {
        formPanel.revalidate();
        formPanel.repaint();
    }

    void viewRecords() {
        try {
            if (currentTable.equals("")) {
                JOptionPane.showMessageDialog(this, "Select table first!");
                return;
            }

            model.setRowCount(0);

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + currentTable);

            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();

            String[] colNames = new String[cols];
            for (int i = 0; i < cols; i++) {
                colNames[i] = md.getColumnName(i + 1);
            }
            model.setColumnIdentifiers(colNames);

            while (rs.next()) {
                Object[] row = new Object[cols];
                for (int i = 0; i < cols; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    void addRecord() {
        try {
            String phone = phoneField.getText().trim();

            if (phone.isEmpty() || !isValidPhone(phone)) {
                JOptionPane.showMessageDialog(this, "Enter valid 10-digit phone number!");
                return;
            }

            PreparedStatement pst;

            if (currentTable.equals("passenger_list")) {
            	pst = con.prepareStatement(
            		    "INSERT INTO passenger_list (passengername, seats, phone_no, classes, gender) VALUES (?,?,?,?,?)"
            		);
                pst.setString(1, nameField.getText());
                pst.setInt(2, Integer.parseInt(seatField.getText()));
                pst.setString(3, phone);
                pst.setString(4, classField.getText());
                pst.setString(5, genderField.getText());
            }
            else if (currentTable.equals("airhosters")) {
                pst = con.prepareStatement("INSERT INTO airhosters VALUES (?,?,?)");

                pst.setString(1, nameField.getText());
                pst.setString(2, phone);
                pst.setString(3, regField.getText());
            }
            else {
                pst = con.prepareStatement("INSERT INTO officers VALUES (?,?,?)");

                pst.setInt(1, Integer.parseInt(idField.getText()));
                pst.setString(2, nameField.getText());
                pst.setString(3, phone);
            }

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Added Successfully");
            clearFields();
            viewRecords();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    void updateRecord() {
        try {
            String phone = phoneField.getText().trim();

            if (phone.isEmpty() || !isValidPhone(phone)) {
                JOptionPane.showMessageDialog(this, "Enter valid phone!");
                return;
            }

            PreparedStatement pst;

            if (currentTable.equals("passenger_list")) {
                pst = con.prepareStatement(
                        "UPDATE passenger_list SET seats=?, phone_no=?, classes=?, gender=? WHERE passengername=?");

                pst.setInt(1, Integer.parseInt(seatField.getText()));
                pst.setString(2, phone);
                pst.setString(3, classField.getText());
                pst.setString(4, genderField.getText());
                pst.setString(5, nameField.getText());
            }
            else if (currentTable.equals("airhosters")) {
                pst = con.prepareStatement(
                        "UPDATE airhosters SET phone_no=?, reg_no=? WHERE airhostersname=?");

                pst.setString(1, phone);
                pst.setString(2, regField.getText());
                pst.setString(3, nameField.getText());
            }
            else {
                pst = con.prepareStatement(
                        "UPDATE officers SET name=?, phone=? WHERE id=?");

                pst.setString(1, nameField.getText());
                pst.setString(2, phone);
                pst.setInt(3, Integer.parseInt(idField.getText()));
            }

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Updated Successfully");
                clearFields();
                viewRecords();
            } else {
                JOptionPane.showMessageDialog(this, "Record Not Found");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    void deleteRecord() {
        try {
            PreparedStatement pst;

            if (currentTable.equals("passenger_list")) {
                pst = con.prepareStatement("DELETE FROM passenger_list WHERE passengername=?");
                pst.setString(1, nameField.getText());
            }
            else if (currentTable.equals("airhosters")) {
                pst = con.prepareStatement("DELETE FROM airhosters WHERE airhostersname=?");
                pst.setString(1, nameField.getText());
            }
            else {
                pst = con.prepareStatement("DELETE FROM officers WHERE id=?");
                pst.setInt(1, Integer.parseInt(idField.getText()));
            }

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Deleted Successfully");
                clearFields();
                viewRecords();
            } else {
                JOptionPane.showMessageDialog(this, "Record Not Found");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        seatField.setText("");
        classField.setText("");
        genderField.setText("");
        regField.setText("");
        idField.setText("");
    }

    public static void main(String[] args) {
        new AirlinesManagementSystem();
    }
}
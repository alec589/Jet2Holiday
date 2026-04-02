package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import List.ListInterface;
import Parking.ParkingData;
import Parking.Spot;

public class AdminPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private ParkingData parkingData;
    private Stack<Spot> undoStack = new Stack<>(); 

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> areaComboBox;
    
    // New Components for TextField interaction
    private JTextField priceField;
    private JLabel lblSelectedID;

    public AdminPage(ParkingData sharedData) {
        this.parkingData = sharedData;
        
        setTitle("Admin Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(300, 200, 750, 750);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- Title ---
        JLabel lblTitle = new JLabel("Parking Management Console");
        lblTitle.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 20, 750, 30);
        contentPane.add(lblTitle);

        // --- 1. Area Filter ---
        JLabel lblArea = new JLabel("Select Area:");
        lblArea.setBounds(50, 80, 100, 25);
        contentPane.add(lblArea);

        areaComboBox = new JComboBox<>(new String[] {"BackBay", "Downtown", "Newton", "Seaport", "Fenway"});
        areaComboBox.setBounds(140, 80, 150, 25);
        areaComboBox.addActionListener(e -> refreshTableData());
        contentPane.add(areaComboBox);

        // --- 2. Table with ScrollPane ---
        String[] columnNames = {"Spot ID", "Area", "Status", "Current Price"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Make table read-only
        };
        table = new JTable(tableModel);
        
        // --- IMPORTANT: Click Table to Fill TextField ---
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String id = (String) tableModel.getValueAt(row, 0);
                    String price = (String) tableModel.getValueAt(row, 3);
                    lblSelectedID.setText("Selected: " + id);
                    priceField.setText(price); // Auto-fill the price into textfield
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 130, 650, 300);
        contentPane.add(scrollPane);

        // --- 3. Price Modification Section ---
        lblSelectedID = new JLabel("Selected: None");
        lblSelectedID.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
        lblSelectedID.setBounds(50, 450, 200, 25);
        contentPane.add(lblSelectedID);

        JLabel lblNewPrice = new JLabel("New Price ($):");
        lblNewPrice.setBounds(50, 485, 100, 25);
        contentPane.add(lblNewPrice);

        priceField = new JTextField();
        priceField.setBounds(150, 485, 100, 25);
        contentPane.add(priceField);
        priceField.setColumns(10);

        JButton btnUpdatePrice = new JButton("Update Price");
        btnUpdatePrice.setBounds(270, 485, 120, 25);
        btnUpdatePrice.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Please select a spot from the table first!");
                return;
            }
            try {
                String id = (String) tableModel.getValueAt(row, 0);
                double newPrice = Double.parseDouble(priceField.getText());
                Spot spot = parkingData.getAllSpots().get(id);
                
                spot.setPrice(newPrice);
                refreshTableData(); // Refresh to show new price in table
                JOptionPane.showMessageDialog(null, "Price updated successfully for " + id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid numeric price!");
            }
        });
        contentPane.add(btnUpdatePrice);

        // --- 4. Status Management ---
        JButton btnMark = new JButton("Set Occupied");
        btnMark.setBackground(new Color(255, 182, 193));
        btnMark.setBounds(50, 540, 150, 40);
        btnMark.addActionListener(e -> changeStatus(true));
        contentPane.add(btnMark);

        JButton btnAvailable = new JButton("Set Available");
        btnAvailable.setBounds(210, 540, 150, 40);
        btnAvailable.addActionListener(e -> changeStatus(false));
        contentPane.add(btnAvailable);

        JButton btnUndo = new JButton("Undo Action");
        btnUndo.setBounds(550, 540, 150, 40);
        btnUndo.addActionListener(e -> {
            if (undoStack.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nothing to undo!");
                return;
            }
            Spot last = undoStack.pop();
            last.setOccupied(!last.isOccupied()); // Reverse status
            refreshTableData();
            JOptionPane.showMessageDialog(null, "Undo successful for " + last.getId());
        });
        contentPane.add(btnUndo);

        // --- 5. Logout ---
        JButton btnBack = new JButton("Back to Login");
        btnBack.addActionListener(e -> {
            new LoginPage().setVisible(true);
            dispose();
        });
        btnBack.setBounds(550, 650, 150, 30);
        contentPane.add(btnBack);

        refreshTableData(); // Initial load
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);
        String selectedArea = (String) areaComboBox.getSelectedItem();
        
        ListInterface<Spot> spots = parkingData.getSpotsByArea(selectedArea);
        for (Spot s : spots) {
            Object[] row = {s.getId(), s.getArea(), s.isOccupied() ? "Occupied" : "Available", String.format("%.2f", s.getPricePerHour())};
            tableModel.addRow(row);
        }
    }

    private void changeStatus(boolean occupied) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Select a spot from the table!");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        Spot spot = parkingData.getAllSpots().get(id);
        undoStack.push(spot);
        spot.setOccupied(occupied);
        refreshTableData();
        JOptionPane.showMessageDialog(null, id + " is now " + (occupied ? "Occupied" : "Available"));
    }
}
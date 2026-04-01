package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Parking.ParkingData;
import Parking.Spot;

public class AdminPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    // Core Data Structures
    private ParkingData parkingData;
    private Stack<Spot> undoStack = new Stack<>(); 

    /**
     * Create the frame.
     * @param sharedData 传入与 UserPage 共享的同一个 ParkingData 对象
     */
    public AdminPage(ParkingData sharedData) {
        this.parkingData = sharedData;
        
        setTitle("Admin Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 500, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- Title ---
        JLabel lblTitle = new JLabel("Parking Management (Admin)");
        lblTitle.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 20, 500, 30);
        contentPane.add(lblTitle);

        // --- Select Spot Label ---
        JLabel lblSelect = new JLabel("Select Spot ID:");
        lblSelect.setBounds(50, 80, 120, 25);
        contentPane.add(lblSelect);

        // --- Combo Box (Hashing usage: getting keys) ---
        JComboBox<String> spotComboBox = new JComboBox<>();
        for (String id : parkingData.getAllSpots().keySet()) {
            spotComboBox.addItem(id);
        }
        spotComboBox.setBounds(170, 80, 200, 25);
        contentPane.add(spotComboBox);

        // --- Mark as Occupied Button (The ACTION/PUSH) ---
        JButton btnMark = new JButton("Set Occupied");
        btnMark.setBackground(new Color(255, 100, 100));
        btnMark.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedId = (String) spotComboBox.getSelectedItem();
                Spot spot = parkingData.getAllSpots().get(selectedId);
                
                if (spot != null) {
                    if (spot.isOccupied()) {
                        JOptionPane.showMessageDialog(null, "Spot is already occupied!");
                    } else {
                        undoStack.push(spot);
                        spot.setOccupied(true);
                        
                        JOptionPane.showMessageDialog(null, "Success! " + selectedId + " is now RED.");
                    }
                }
            }
        });
        btnMark.setBounds(50, 150, 180, 40);
        contentPane.add(btnMark);

        // --- UNDO Button (The POP) ---
        JButton btnUndo = new JButton("Undo Last Action");
        btnUndo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (undoStack.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nothing to undo!");
                    return;
                }
                Spot lastModifiedSpot = undoStack.pop();
                lastModifiedSpot.setOccupied(false);

                JOptionPane.showMessageDialog(null, 
                    "Action Undone: " + lastModifiedSpot.getId() + " is now GREEN again.");
            }
        });
        btnUndo.setBounds(250, 150, 180, 40);
        contentPane.add(btnUndo);

        // --- Close Button ---
        JButton btnClose = new JButton("Back to Login");
        btnClose.addActionListener(e -> {
            new LoginPage().setVisible(true);
            dispose();
        });
        btnClose.setBounds(170, 253, 150, 30);
        contentPane.add(btnClose);
    }
}
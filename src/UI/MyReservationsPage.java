package UI;

import java.awt.Font;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import List.ListInterface;
import Parking.*;

public class MyReservationsPage extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private UserAccount user;
    private ReservationService reservationService;

    private JTable table;
    private JLabel missedLabel;
    private DefaultTableModel tableModel;
    private ListInterface<Reservation> reservations;

    private Timer timer;

    public MyReservationsPage(UserAccount user, ReservationService reservationService) {
        this.user = user;
        this.reservationService = reservationService;

        setTitle("My Reservations");
        setBounds(300, 200, 900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        initUI();
        loadData();
        startTimer();
    }

    private void initUI() {

        
        JLabel title = new JLabel("My Reservations");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(250, 20, 400, 40);
        contentPane.add(title);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);

       
        String[] cols = {"Spot", "Area", "Price", "Status", "Time Left"};

        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
       //miss counts
        missedLabel = new JLabel();
        missedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        missedLabel.setBounds(250, 60, 400, 25);
        contentPane.add(missedLabel);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(60, 90, 760, 250);
        contentPane.add(scroll);

        // Check In button
        JButton checkBtn = new JButton("Check In");
        checkBtn.setBounds(220, 370, 160, 40);  
        contentPane.add(checkBtn);

        // Cancel button
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(480, 370, 160, 40);      
        contentPane.add(cancelBtn);

        checkBtn.addActionListener(e -> checkIn());
        cancelBtn.addActionListener(e -> cancel());
    }

    private void loadData() {
        reservationService.cleanupExpiredReservations();
        reservations = reservationService.getReservationsByUser(user);
        missedLabel.setText("Missed Reservations (Last 30 Days): " + user.getMissedReservationCount());
        
        tableModel.setRowCount(0);

        if (reservations.isEmpty()) {
            return;
        }

        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            Spot s = r.getSpot();

            long remain = r.getRemainingTimeMillis();
            long min = remain / 60000;
            long sec = (remain % 60000) / 1000;

            String status = r.isCheckedIn() ? "Checked In" : "Reserved";

            tableModel.addRow(new Object[]{
                    s.getSpotId(),
                    s.getArea(),
                    String.format("$%.2f", s.getPrice()),
                    status,
                    String.format("%02d:%02d", min, sec)
            });
        }
    }

    private void checkIn() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation.");
            return;
        }

        Reservation r = reservations.get(row);

        if (r.isCheckedIn()) {
            JOptionPane.showMessageDialog(this, "Already checked in.");
            return;
        }

        boolean ok = reservationService.checkIn(r.getSpot().getSpotId());

        if (ok) {
            JOptionPane.showMessageDialog(this, "Check-in success!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Check-in failed.");
        }
    }

    private void cancel() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation.");
            return;
        }

        Reservation r = reservations.get(row);

        boolean ok = reservationService.cancelReservation(r.getSpot().getSpotId());

        if (ok) {
            JOptionPane.showMessageDialog(this, "Cancelled.");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Cancel failed.");
        }
    }

    private void startTimer() {
        timer = new Timer(1000, e -> refreshTimeOnly());
        timer.start();
    }
    private void refreshTimeOnly() {
        if (reservations == null || reservations.isEmpty()) return;

        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);

            long remain = r.getRemainingTimeMillis();
            long min = remain / 60000;
            long sec = (remain % 60000) / 1000;

            String status = r.isCheckedIn() ? "Checked In" : "Reserved";

            tableModel.setValueAt(status, i, 3); // status列
            tableModel.setValueAt(String.format("%02d:%02d", min, sec), i, 4); // time列
        }
    }
    @Override
    public void dispose() {
        if (timer != null) timer.stop();
        super.dispose();
    }
}
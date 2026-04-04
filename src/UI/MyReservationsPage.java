package UI;

import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import List.ListInterface;
import Parking.Reservation;
import Parking.ReservationService;
import Parking.Spot;
import Parking.UserAccount;

public class MyReservationsPage extends JFrame {

    private static final long serialVersionUID = 1L;

    private UserAccount user;
    private ReservationService reservationService;

    private DefaultListModel<String> listModel;
    private JList<String> reservationList;
    private ListInterface<Reservation> reservations;

    private Timer refreshTimer;

    public MyReservationsPage(UserAccount user, ReservationService reservationService) {
        this.user = user;
        this.reservationService = reservationService;

        setTitle("My Reservations");
        setSize(700, 420);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
        loadReservations();
        startAutoRefresh();
    }

    private void initializeUI() {
        JLabel titleLabel = new JLabel("My Reservations");
        titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 22));
        titleLabel.setBounds(250, 20, 220, 30);
        add(titleLabel);

        listModel = new DefaultListModel<>();
        reservationList = new JList<>(listModel);
        reservationList.setFont(new Font("Monospaced", Font.PLAIN, 18));

        JScrollPane scrollPane = new JScrollPane(reservationList);
        scrollPane.setBounds(40, 70, 600, 220);
        add(scrollPane);

        JButton checkInButton = new JButton("Check In");
        checkInButton.setBounds(110, 320, 140, 35);
        add(checkInButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(280, 320, 140, 35);
        add(cancelButton);

        JButton closeButton = new JButton("Close");
        closeButton.setBounds(450, 320, 140, 35);
        add(closeButton);

        checkInButton.addActionListener(e -> handleCheckIn());
        cancelButton.addActionListener(e -> handleCancel());
        closeButton.addActionListener(e -> {
            stopAutoRefresh();
            dispose();
        });
    }

    private void loadReservations() {
        reservationService.cleanupExpiredReservations();
        reservations = reservationService.getReservationsByUser(user);

        listModel.clear();

        if (reservations.isEmpty()) {
            listModel.addElement("No active reservations.");
            return;
        }

        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            Spot s = r.getSpot();

            long remaining = r.getRemainingTimeMillis();
            long minutes = remaining / 60000;
            long seconds = (remaining % 60000) / 1000;

            String status = r.isCheckedIn() ? "Checked In" : "Reserved";

            String row = String.format(
                "%-8s | %-9s | $%-5.2f | %-10s | %02d:%02d",
                s.getSpotId(),
                s.getArea(),
                s.getPrice(),
                status,
                minutes,
                seconds
            );

            listModel.addElement(row);
        }
    }

    private void handleCheckIn() {
        if (reservations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No reservation selected.");
            return;
        }

        int index = reservationList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation first.");
            return;
        }

        Reservation selected = reservations.get(index);

        if (selected.isCheckedIn()) {
            JOptionPane.showMessageDialog(this, "This reservation has already been checked in.");
            return;
        }

        boolean success = reservationService.checkIn(selected.getSpot().getSpotId());

        if (success) {
            JOptionPane.showMessageDialog(this, "Check-in successful.");
            loadReservations();
        } else {
            JOptionPane.showMessageDialog(this, "Check-in failed.");
        }
    }

    private void handleCancel() {
        if (reservations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No reservation selected.");
            return;
        }

        int index = reservationList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation first.");
            return;
        }

        Reservation selected = reservations.get(index);

        boolean success = reservationService.cancelReservation(selected.getSpot().getSpotId());

        if (success) {
            JOptionPane.showMessageDialog(this, "Reservation cancelled.");
            loadReservations();
        } else {
            JOptionPane.showMessageDialog(this, "Cancel failed.");
        }
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(1000, e -> loadReservations());
        refreshTimer.start();
    }

    private void stopAutoRefresh() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
}
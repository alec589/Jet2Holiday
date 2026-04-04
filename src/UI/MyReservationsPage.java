package UI;

import javax.swing.*;

import List.ListInterface;

import java.awt.event.*;
import Parking.*;

public class MyReservationsPage extends JFrame {

    private JList<String> listUI;
    private ListInterface<Reservation> reservations;

    public MyReservationsPage(UserAccount user, ReservationService service) {

        setTitle("My Reservations");
        setSize(500, 400);
        setLayout(null);

        reservations = service.getReservationsByUser(user);

        DefaultListModel<String> model = new DefaultListModel<>();

        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            Spot s = r.getSpot();

            long remaining = r.getRemainingTimeMillis();
            long min = remaining / 60000;
            long sec = (remaining % 60000) / 1000;

            String text = s.getSpotId() +
                    " | " + s.getArea() +
                    " | $" + String.format("%.2f", s.getPrice()) +
                    " | Time Left: " + String.format("%02d:%02d", min, sec);

            model.addElement(text);
        }

        listUI = new JList<>(model);
        listUI.setBounds(20, 20, 440, 200);
        add(listUI);

        // Check In
        JButton checkInBtn = new JButton("Check In");
        checkInBtn.setBounds(60, 250, 150, 30);
        add(checkInBtn);

        checkInBtn.addActionListener(e -> {
            int index = listUI.getSelectedIndex();
            if (index == -1) return;

            Reservation r = reservations.get(index);
            service.checkIn(r.getSpot().getSpotId());

            JOptionPane.showMessageDialog(null, "Checked in!");
        });

        // Cancel
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(260, 250, 150, 30);
        add(cancelBtn);

        cancelBtn.addActionListener(e -> {
            int index = listUI.getSelectedIndex();
            if (index == -1) return;

            Reservation r = reservations.get(index);
            service.cancelReservation(r.getSpot().getSpotId());

            JOptionPane.showMessageDialog(null, "Cancelled!");
        });
    }
}

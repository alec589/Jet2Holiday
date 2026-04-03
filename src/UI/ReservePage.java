package UI;
import javax.swing.*;
import java.awt.*;

import Parking.Spot;
import Parking.UserAccount;
import Parking.ReservationService;

public class ReservePage extends JFrame {

    public ReservePage(UserAccount user, Spot spot, ReservationService reservationService, Runnable onSuccess) {

        setTitle("Confirm Reservation");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel infoLabel = new JLabel(
            "<html>Spot: " + spot.getSpotId() +
            "<br>Price: $" + spot.getPrice() +
            "<br><br>Proceed to payment?</html>"
        );
        infoLabel.setBounds(50, 20, 250, 80);
        add(infoLabel);

        JButton confirmButton = new JButton("Confirm & Pay");
        confirmButton.setBounds(100, 100, 140, 30);
        add(confirmButton);

        confirmButton.addActionListener(e -> {

            boolean success = reservationService.reserve(user, spot);

            if (success) {
                JOptionPane.showMessageDialog(null, "Reservation successful!");

                
                if (onSuccess != null) {
                    onSuccess.run();
                }

                dispose(); // close window
            } else {
                JOptionPane.showMessageDialog(null, "Reservation failed.");
            }
        });

        setVisible(true);
    }
}
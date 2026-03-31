package UI;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ParkingUI extends JFrame{
	
	public ParkingUI() {
        setTitle("Smart Parking Recommendation System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 窗口居中
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ParkingUI();
        });
    }
}

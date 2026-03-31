package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Parking.Spot;

public class AreaPanel extends JPanel{

	private String areaName;
    private List<Spot> spots;
    private Spot recommendedSpot;

    private JPanel gridPanel;

    public AreaPanel(String areaName, List<Spot> spots) {
        this.areaName = areaName;
        this.spots = spots;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(areaName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 5, 15, 15));
        add(gridPanel, BorderLayout.CENTER);

        refreshDisplay();
    }

    public void setRecommendedSpot(Spot recommendedSpot) {
        this.recommendedSpot = recommendedSpot;
        refreshDisplay();
    }

    public void refreshDisplay() {
        gridPanel.removeAll();

        for (Spot spot : spots) {
            JPanel spotPanel = new JPanel();
            spotPanel.setPreferredSize(new Dimension(100, 80));
            spotPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            spotPanel.setLayout(new BorderLayout());

            JLabel idLabel = new JLabel(spot.getId(), SwingConstants.CENTER);
            idLabel.setFont(new Font("Arial", Font.BOLD, 12));

            JLabel statusLabel = new JLabel(
                spot.isOccupied() ? "Occupied" : "Available",
                SwingConstants.CENTER
            );

            if (recommendedSpot != null && spot.getId().equals(recommendedSpot.getId())) {
                spotPanel.setBackground(Color.BLUE);
                idLabel.setForeground(Color.WHITE);
                statusLabel.setForeground(Color.WHITE);
            } else if (spot.isOccupied()) {
                spotPanel.setBackground(Color.RED);
                idLabel.setForeground(Color.BLACK);
                statusLabel.setForeground(Color.BLACK);
            } else {
                spotPanel.setBackground(Color.GREEN);
                idLabel.setForeground(Color.BLACK);
                statusLabel.setForeground(Color.BLACK);
            }

            spotPanel.add(idLabel, BorderLayout.CENTER);
            spotPanel.add(statusLabel, BorderLayout.SOUTH);

            gridPanel.add(spotPanel);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }
}

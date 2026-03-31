package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collection;
import javax.swing.*;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import Graph.CityGraph;
import Parking.ParkingData;
import Parking.RecommendationService;
import Parking.Spot;

public class ParkingMapPanel extends JPanel{
	
	private Collection<Spot> spots;
    private Spot recommendedSpot;

    public ParkingMapPanel() {
        setPreferredSize(new Dimension(800, 500));
        setBackground(Color.WHITE);
    }

    public void setSpots(Collection<Spot> spots) {
        this.spots = spots;
        repaint();
    }

    public void setRecommendedSpot(Spot recommendedSpot) {
        this.recommendedSpot = recommendedSpot;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (spots == null) {
            return;
        }

        for (Spot spot : spots) {
            int x = spot.getX();
            int y = spot.getY();

            if (recommendedSpot != null && spot.getId().equals(recommendedSpot.getId())) {
                g.setColor(Color.BLUE);
            } else if (spot.isOccupied()) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }

            g.fillRect(x, y, 20, 20);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, 20, 20);
        }
    }
}

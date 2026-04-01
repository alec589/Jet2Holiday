package UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

import Parking.Spot;

public class AreaMapPanel extends JPanel{
	private String areaName;
    private List<Spot> spots;
    private Spot recommendedSpot;

    public AreaMapPanel(String areaName) {
        this.areaName = areaName;
        setBackground(Color.WHITE);
    }

    public void setSpots(List<Spot> spots) {
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

        drawRoads(g);

        if (spots != null) {
            for (Spot spot : spots) {
                drawSpot(g, spot);
            }
        }
    }

    private void drawRoads(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(130, 130, 130));
        g2.fillRect(20, 300, 870, 30);
        
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Main Road", 420, 320);
    }

    private void drawSpot(Graphics g, Spot spot) {
    	int x = spot.getX();
        int y = spot.getY();

        int spotWidth = 130;
        int spotHeight = 230;

        boolean isRecommended = recommendedSpot != null 
        	    && spot.getId().equals(recommendedSpot.getId());
        
        if (isRecommended) {
            g.setColor(Color.BLUE);
        } else if (spot.isOccupied()) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GREEN);
        }
        g.fillRect(x, y, spotWidth, spotHeight);
        
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, y, spotWidth, spotHeight);

        if (isRecommended) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(spot.getId(), x + 35, y + 52);
        
        String status = spot.isOccupied() ? "Occupied" : "Available";
        g.drawString(status, x+33, y+220);
    }
}

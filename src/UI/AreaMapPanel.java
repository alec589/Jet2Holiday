package UI;

import java.awt.BasicStroke;
import Parking.Destination;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

import List.ListInterface;
import Parking.Spot;

public class AreaMapPanel extends JPanel{
	private String areaName;

	private ListInterface<Spot> spots;
    private Spot recommendedSpot;
    private ListInterface<Destination> destinations;

    public AreaMapPanel(String areaName) {
        this.areaName = areaName;
        setBackground(Color.WHITE);
    }

    public void setSpots(ListInterface<Spot> spots) {
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
        
        if (destinations != null) {
            for (Destination destination : destinations) {
                drawDestination(g, destination);
            }
        }
    }

    private void drawRoads(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(130, 130, 130));
        g2.fillRect(20, 280, 870, 30);
        
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Main Road", 420, 300);
    }

    private void drawSpot(Graphics g, Spot spot) {
        int x = spot.getCoordinate().getX();
        int y = spot.getCoordinate().getY();

        int spotWidth = 60;
        int spotHeight = 40;

        boolean isRecommended = recommendedSpot != null
                && spot.getSpotId().equals(recommendedSpot.getSpotId());

        // 颜色逻辑
        if (isRecommended) {
            g.setColor(Color.BLUE);
        } else if (spot.isOccupied()) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GREEN);
        }

        g.fillRect(x, y, spotWidth, spotHeight);

        // 边框
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, y, spotWidth, spotHeight);

        // 文字颜色
        if (isRecommended) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }

        g.setFont(new Font("Arial", Font.BOLD, 10));

        // ID
        g.drawString(spot.getSpotId(), x + 4, y + 12);

        // 状态
        String status = spot.isOccupied() ? "Occupied" : "Available";
        g.drawString(status, x + 4, y + 24);

        // 价格
        g.drawString("$" + String.format("%.2f", spot.getPrice()), x + 4, y + 36);
    }
    
    public void setDestinations(ListInterface<Destination> destinations) {
        this.destinations = destinations;
        repaint();
    }
    
    private void drawDestination(Graphics g, Destination destination) {
        int x = destination.getCoordinate().getX();
        int y = destination.getCoordinate().getY();

        int width = 110;
        int height = 35;

        g.setColor(new Color(255, 200, 0)); // 黄橙色
        g.fillRoundRect(x, y, width, height, 10, 10);

        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, width, height, 10, 10);

        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString(destination.getName(), x + 6, y + 20);
    }
    
}

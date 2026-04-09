package UI;

import java.awt.BasicStroke;
import Parking.Destination;
import Parking.RoadNode;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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
        drawRoadNodes(g);

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
    
    private void drawCenteredTextBlock(Graphics g, String[] lines, int x, int y, int width, int height) {
        FontMetrics m = g.getFontMetrics();

        int lineHeight = m.getHeight();
        int totalHeight = lines.length * lineHeight;

        int startY = y + (height - totalHeight) / 2 + m.getAscent();

        for (int i = 0; i < lines.length; i++) {
            int textWidth = m.stringWidth(lines[i]);
            int textX = x + (width - textWidth) / 2;
            int textY = startY + i * lineHeight;

            g.drawString(lines[i], textX, textY);
        }
    }

    private void drawRoads(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(130, 130, 130));
        g2.fillRect(30, 240, 1050, 20);
        
        g2.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        drawCenteredTextBlock(g,
            new String[]{"Main Road"},
            30, 240, 1050, 20
        );
    }
    
    private void drawRoadNodes(Graphics g) {
        int size = 8;

        int[][] nodes = {
            {110, 250},
            {320, 250},
            {500, 250},
            {680, 250},
            {950, 250}
        };

        for (int[] n : nodes) {
            int x = n[0];
            int y = n[1];

            g.setColor(Color.WHITE);
            g.fillRect(x - size / 2, y - size / 2, size, size);

            g.setColor(Color.BLACK);
            g.drawRect(x - size / 2, y - size / 2, size, size);
        }
    }

    private void drawSpot(Graphics g, Spot spot) {
    	int centerX = spot.getCoordinate().getX();
        int centerY = spot.getCoordinate().getY();

        int spotWidth = 100;
        int spotHeight = 150;
        
        int x = centerX - spotWidth / 2;
        int y = centerY - spotHeight / 2;

        boolean isRecommended = recommendedSpot != null
                && spot.getSpotId().equals(recommendedSpot.getSpotId());

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

        g.setFont(new Font("Arial", Font.BOLD, 10));

        String[] text = {
            spot.getSpotId(),
            spot.isOccupied() ? "Occupied" : "Available",
            "$" + String.format("%.2f", spot.getPrice())
        };

        drawCenteredTextBlock(g, text, x, y, spotWidth, spotHeight);
    }
    
    public void setDestinations(ListInterface<Destination> destinations) {
        this.destinations = destinations;
        repaint();
    }
    
    private void drawDestination(Graphics g, Destination destination) {
    	int centerX = destination.getCoordinate().getX();
        int centerY = destination.getCoordinate().getY();

        int width = 125;
        int height = 40;

        int x = centerX - width / 2;
        int y = centerY - height / 2;

        g.setColor(new Color(255, 200, 0));
        g.fillRoundRect(x, y, width, height, 10, 10);

        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, width, height, 10, 10);

        g.setFont(new Font("Arial", Font.BOLD, 10));

        String[] text = {
            destination.getName()
        };

        drawCenteredTextBlock(g, text, x, y, width, height);
    }
    
    
    
}

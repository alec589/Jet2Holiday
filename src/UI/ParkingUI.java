package UI;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Graph.CityGraph;
import Parking.ParkingData;
import Parking.RecommendationService;
import Parking.Spot;

public class ParkingUI extends JFrame{
	
	private JComboBox<String> currentBox;
	private JComboBox<String> destinationBox;
    private JButton recommendButton;
    private JLabel resultLabel;

    private ParkingData parkingData;
    private RecommendationService recommendationService;
    private CityGraph graph;

    public ParkingUI() {
        // create new parking data
        parkingData = new ParkingData();

        graph = new CityGraph();
        graph.addEdge("BackBay", "Fenway", 2);
        graph.addEdge("BackBay", "Downtown", 3);
        graph.addEdge("Downtown", "Seaport", 4);
        graph.addEdge("Fenway", "Seaport", 5);
        graph.addEdge("Newton", "Fenway", 7);

        recommendationService = new RecommendationService(graph, parkingData);

        // set a main panel
        setTitle("Smart Parking Recommendation System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());

        String[] areas = {"BackBay", "Fenway", "Downtown", "Seaport", "Newton"};
        currentBox = new JComboBox<>(areas);
        destinationBox = new JComboBox<>(areas);

        // add button
        recommendButton = new JButton("Recommend Spot");
        resultLabel = new JLabel("Please select your current area and destination.");
        // add button listener
        recommendButton.addActionListener(e -> {
        	String currentArea = (String) currentBox.getSelectedItem();
        	String destinationArea = (String) destinationBox.getSelectedItem();

        	Spot recommendedSpot = recommendationService.recommend(currentArea, destinationArea);

            if (recommendedSpot == null) {
                resultLabel.setText("No available parking spot found.");
            } else {
                resultLabel.setText(
                    "Recommended: " + recommendedSpot.getId()
                    + " | Area: " + recommendedSpot.getArea()
                    + " | Price: $" + String.format("%.2f", recommendedSpot.getPricePerHour())
                );
            }
        });

        mainPanel.add(new JLabel("Current Area:"));
        mainPanel.add(currentBox);
        mainPanel.add(new JLabel("Destination Area:"));
        mainPanel.add(destinationBox);
        mainPanel.add(recommendButton);
        mainPanel.add(resultLabel);

        add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ParkingUI();
        });
    }
}

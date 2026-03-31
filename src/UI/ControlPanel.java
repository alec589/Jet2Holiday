package UI;

import java.awt.FlowLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Graph.CityGraph;
import Parking.RecommendationService;
import Parking.Spot;

public class ControlPanel extends JPanel{
	private JComboBox<String> currentBox;
    private JComboBox<String> destinationBox;
    private JButton recommendButton;
    private JLabel resultLabel;

    public ControlPanel(RecommendationService recommendationService,
                        CityGraph graph,
                        HashMap<String, AreaPanel> areaPanels) {

        setLayout(new FlowLayout());

        String[] areas = {"BackBay", "Fenway", "Downtown", "Seaport", "Newton"};

        currentBox = new JComboBox<>(areas);
        destinationBox = new JComboBox<>(areas);
        recommendButton = new JButton("Recommend Spot");
        resultLabel = new JLabel("Please select your current area and destination.");

        add(new JLabel("Current Area:"));
        add(currentBox);
        add(new JLabel("Destination Area:"));
        add(destinationBox);
        add(recommendButton);
        add(resultLabel);

        recommendButton.addActionListener(e -> {
            String currentArea = (String) currentBox.getSelectedItem();
            String destinationArea = (String) destinationBox.getSelectedItem();

            Spot recommendedSpot = recommendationService.recommend(currentArea, destinationArea);

            for (AreaPanel panel : areaPanels.values()) {
                panel.setRecommendedSpot(null);
            }

            if (recommendedSpot == null) {
                resultLabel.setText("No available parking spot found.");
            } else {
                double toSpot = graph.shortestDistance(currentArea, recommendedSpot.getArea());
                double toDestination = graph.shortestDistance(recommendedSpot.getArea(), destinationArea);

                resultLabel.setText(
                    "Recommended: " + recommendedSpot.getId()
                    + " | Area: " + recommendedSpot.getArea()
                    + " | Price: $" + String.format("%.2f", recommendedSpot.getPricePerHour())
                    + " | User→Spot: " + toSpot
                    + " | Spot→Destination: " + toDestination
                );

                areaPanels.get(recommendedSpot.getArea()).setRecommendedSpot(recommendedSpot);
            }
        });
    }
}

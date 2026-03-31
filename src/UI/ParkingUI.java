package UI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import Graph.CityGraph;
import Parking.ParkingData;
import Parking.RecommendationService;
import Parking.Spot;

public class ParkingUI extends JFrame{
	
	private ParkingData parkingData;
    private RecommendationService recommendationService;
    private CityGraph graph;

    private HashMap<String, AreaPanel> areaPanels;

    public ParkingUI() {
        parkingData = new ParkingData();

        graph = new CityGraph();
        graph.addEdge("BackBay", "Fenway", 2);
        graph.addEdge("BackBay", "Downtown", 3);

        graph.addEdge("Fenway", "Cambridge", 4);
        graph.addEdge("Fenway", "Seaport", 5);

        graph.addEdge("Downtown", "Seaport", 2);
        graph.addEdge("Downtown", "Cambridge", 6);

        graph.addEdge("Newton", "Fenway", 7);
        graph.addEdge("Newton", "BackBay", 8);

        recommendationService = new RecommendationService(graph, parkingData);

        setTitle("Smart Parking Recommendation System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        areaPanels = new HashMap<>();

        JTabbedPane tabbedPane = new JTabbedPane();

        String[] areas = {"BackBay", "Fenway", "Downtown", "Seaport", "Newton"};

        for (String area : areas) {
            AreaPanel panel = new AreaPanel(area, parkingData.getSpotsByArea(area));
            areaPanels.put(area, panel);
            tabbedPane.addTab(area, panel);
        }

        ControlPanel controlPanel = new ControlPanel(
            recommendationService,
            graph,
            areaPanels
        );

        add(controlPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ParkingUI());
    }
    
}

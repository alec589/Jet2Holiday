package UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Graph.CityGraph;
import Parking.ParkingData;
import Parking.RecommendationService;
import Parking.Spot;

import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;
import java.util.Collection;
import java.util.HashMap;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Stack;

import javax.swing.JSlider;
public class UserPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
	        new LoginPage().setVisible(true);
	    });
	}
	

	/**
	 * Create the frame.
	 */
	private ParkingData parkingData;
	private Stack<Spot> historyStack = new Stack<>();
    private RecommendationService recommendationService;
    private CityGraph graph;

    // make these panels available not only for initializeUI method
    private AreaMapPanel backBayPanel;
    private AreaMapPanel fenwayPanel;
    private AreaMapPanel downtownPanel;
    private AreaMapPanel seaportPanel;
    private AreaMapPanel newtonPanel;
    
    private JComboBox<String> comboBox;
    private JComboBox<String> comboBox_1;
    private JLabel resultLabel;
    
    private JSlider preferenceSlider; 
    
    private Collection<Spot> spots;
    private Spot recommendedSpot;
    private JButton btnNewButton_1;
    private JButton btnNewButton_2;
    
    
	public UserPage(ParkingData sharedData) {
		
		initializeUI();
		this.parkingData = sharedData;
		initializeData();
		refreshAreaPanels();

	}


	private void initializeUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Current Area : ");
		lblNewLabel.setBounds(32, 55, 98, 16);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Destination Area : ");
		lblNewLabel_1.setBounds(279, 55, 124, 16);
		contentPane.add(lblNewLabel_1);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"BackBay", "Downtown", "Newton", "Seaport", "Fenway"}));
		comboBox.setBounds(127, 52, 140, 27);
		contentPane.add(comboBox);
		
		comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"BackBay", "Downtown", "Newton", "Seaport", "Fenway"}));
		comboBox_1.setBounds(398, 52, 140, 27);
		contentPane.add(comboBox_1);
		
		JButton btnNewButton = new JButton("Recommend Spot");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentArea = (String) comboBox.getSelectedItem();
			    String destinationArea = (String) comboBox_1.getSelectedItem();
			    
			    double distanceWeight = preferenceSlider.getValue() / 100.0;
			    double priceWeight = 1.0 - distanceWeight;

			    recommendedSpot = recommendationService.recommend(currentArea, destinationArea,distanceWeight,
			    	    priceWeight);

			    if (recommendedSpot == null) {
			        resultLabel.setText("No available spot found");
			    } else {
			    	historyStack.push(recommendedSpot);
			        resultLabel.setText(
			            "Recommended: " + recommendedSpot.getId() +
			            " (" + recommendedSpot.getArea() + ")"
			        );
			    }

			    refreshAreaPanels();
			}
		});
		btnNewButton.setBounds(549, 49, 140, 29);
		contentPane.add(btnNewButton);
		
		resultLabel = new JLabel("Please select current and destination area!");
		resultLabel.setBounds(702, 54, 300, 16);
		contentPane.add(resultLabel);
		
		// User Preference Slider
		JLabel prefLabel = new JLabel("Preference:");
		prefLabel.setBounds(32, 90, 100, 16);
		contentPane.add(prefLabel);

		preferenceSlider = new JSlider(0, 100, 50); 
		preferenceSlider.setBounds(127, 85, 200, 40);
		preferenceSlider.setMajorTickSpacing(25);
		preferenceSlider.setMinorTickSpacing(5); 
		preferenceSlider.setPaintTicks(true);
		preferenceSlider.setPaintLabels(true);
		contentPane.add(preferenceSlider);

		// preference label description
		JLabel leftLabel = new JLabel("Affordable");
		leftLabel.setBounds(127, 120, 80, 16);
		contentPane.add(leftLabel);

		JLabel rightLabel = new JLabel("Convenient");
		rightLabel.setBounds(250, 120, 100, 16);
		contentPane.add(rightLabel);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(37, 150, 929, 560);
		contentPane.add(tabbedPane);
		
		// add 5 tabs
		backBayPanel = new AreaMapPanel("BackBay");
		tabbedPane.addTab("BackBay", null, backBayPanel, null);

		fenwayPanel = new AreaMapPanel("Fenway");
		tabbedPane.addTab("Fenway", null, fenwayPanel, null);

		downtownPanel = new AreaMapPanel("Downtown");
		tabbedPane.addTab("Downtown", null, downtownPanel, null);

		seaportPanel = new AreaMapPanel("Seaport");
		tabbedPane.addTab("Seaport", null, seaportPanel, null);

		newtonPanel = new AreaMapPanel("Newton");
		tabbedPane.addTab("Newton", null, newtonPanel, null);
		
		JLabel lblNewLabel_3 = new JLabel("Smart Parking System");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setFont(new Font("Lucida Grande", Font.BOLD, 18));
		lblNewLabel_3.setBounds(-1, 0, 1000, 40);
		contentPane.add(lblNewLabel_3);
		
		btnNewButton_1 = new JButton("Logout");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoginPage().setVisible(true);
			    dispose();
			}
		});
		btnNewButton_1.setBounds(652, 721, 117, 38);
		contentPane.add(btnNewButton_1);
		
		btnNewButton_2 = new JButton("View History");
		btnNewButton_2.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        if (historyStack.isEmpty()) {
		            javax.swing.JOptionPane.showMessageDialog(null, "No history found!");
		        } else {
		            Spot lastSpot = historyStack.pop();
		            
		            String info = "Last Viewed Spot: " + lastSpot.getId() + 
		                          "\nArea: " + lastSpot.getArea() + 
		                          "\nPrice: $" + String.format("%.2f", lastSpot.getPricePerHour());
		            
		            javax.swing.JOptionPane.showMessageDialog(null, info);
		        }
		    }
		});
		btnNewButton_2.setBounds(247, 721, 117, 38);
		contentPane.add(btnNewButton_2);
		
	}
	
	
	private void initializeData() {

		parkingData = new ParkingData();

        graph = new CityGraph();
        graph.addEdge("BackBay", "Fenway", 2);
        graph.addEdge("BackBay", "Downtown", 3);

        graph.addEdge("Fenway", "Seaport", 5);

        graph.addEdge("Downtown", "Seaport", 2);

        graph.addEdge("Newton", "Fenway", 7);
        graph.addEdge("Newton", "BackBay", 8);

        recommendationService = new RecommendationService(graph, parkingData);

    }


	private void refreshAreaPanels() {
		backBayPanel.setSpots(parkingData.getSpotsByArea("BackBay"));
	    fenwayPanel.setSpots(parkingData.getSpotsByArea("Fenway"));
	    downtownPanel.setSpots(parkingData.getSpotsByArea("Downtown"));
	    seaportPanel.setSpots(parkingData.getSpotsByArea("Seaport"));
	    newtonPanel.setSpots(parkingData.getSpotsByArea("Newton"));

	    backBayPanel.setRecommendedSpot(recommendedSpot);
	    fenwayPanel.setRecommendedSpot(recommendedSpot);
	    downtownPanel.setRecommendedSpot(recommendedSpot);
	    seaportPanel.setRecommendedSpot(recommendedSpot);
	    newtonPanel.setRecommendedSpot(recommendedSpot);
	}
	
	private void refreshOneArea(JPanel panel, String area) {
	    panel.removeAll();

	    for (Spot spot : parkingData.getSpotsByArea(area)) {
	        JPanel spotBox = new JPanel();
	        spotBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        spotBox.setLayout(new BorderLayout());
	        spotBox.setPreferredSize(new Dimension(100, 70));

	        JLabel idLabel = new JLabel(spot.getId(), SwingConstants.CENTER);
	        JLabel statusLabel = new JLabel(
	            spot.isOccupied() ? "Occupied" : "Available",
	            SwingConstants.CENTER
	        );

	        if (recommendedSpot != null && spot.getId().equals(recommendedSpot.getId())) {
	            spotBox.setBackground(Color.BLUE);
	            idLabel.setForeground(Color.WHITE);
	            statusLabel.setForeground(Color.WHITE);
	        } else if (spot.isOccupied()) {
	            spotBox.setBackground(Color.RED);
	            idLabel.setForeground(Color.BLACK);
	            statusLabel.setForeground(Color.BLACK);
	        } else {
	            spotBox.setBackground(Color.GREEN);
	            idLabel.setForeground(Color.BLACK);
	            statusLabel.setForeground(Color.BLACK);
	        }

	        spotBox.add(idLabel, BorderLayout.CENTER);
	        spotBox.add(statusLabel, BorderLayout.SOUTH);

	        panel.add(spotBox);
	    }

	    panel.revalidate();
	    panel.repaint();
	}
}

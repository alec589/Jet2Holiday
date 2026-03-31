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

public class MainJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainJFrame frame = new MainJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	private ParkingData parkingData;
    private RecommendationService recommendationService;
    private CityGraph graph;

    // make these panels available not only for initializeUI method
    private JPanel backBayPanel;
    private JPanel fenwayPanel;
    private JPanel downtownPanel;
    private JPanel seaportPanel;
    private JPanel newtonPanel;
    
    private Collection<Spot> spots;
    private Spot recommendedSpot;
    
    
	public MainJFrame() {
		
		initializeUI();
		
		initializeData();
		
		refreshAreaPanels();

	}


	private void initializeUI(){
		// main frame
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
		lblNewLabel_1.setBounds(279, 52, 124, 16);
		contentPane.add(lblNewLabel_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"BackBay", "Downtown", "Newton", "Seaport", "Fenway"}));
		comboBox.setBounds(127, 50, 140, 27);
		contentPane.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"BackBay", "Downtown", "Newton", "Seaport", "Fenway"}));
		comboBox_1.setBounds(398, 46, 140, 27);
		contentPane.add(comboBox_1);
		
		JButton btnNewButton = new JButton("Recommend Spot");
		btnNewButton.setBounds(549, 44, 140, 29);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_2 = new JLabel("New label");
		lblNewLabel_2.setBounds(702, 52, 156, 16);
		contentPane.add(lblNewLabel_2);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(37, 85, 929, 668);
		contentPane.add(tabbedPane);
		
		// add 5 tabs
		backBayPanel = new JPanel();
		backBayPanel.setLayout(new GridLayout(0, 4, 10, 10));
		tabbedPane.addTab("BackBay", null, backBayPanel, null);

		fenwayPanel = new JPanel();
		fenwayPanel.setLayout(new GridLayout(0, 4, 10, 10));
		tabbedPane.addTab("Fenway", null, fenwayPanel, null);

		downtownPanel = new JPanel();
		downtownPanel.setLayout(new GridLayout(0, 4, 10, 10));
		tabbedPane.addTab("Downtown", null, downtownPanel, null);

		seaportPanel = new JPanel();
		seaportPanel.setLayout(new GridLayout(0, 4, 10, 10));
		tabbedPane.addTab("Seaport", null, seaportPanel, null);

		newtonPanel = new JPanel();
		newtonPanel.setLayout(new GridLayout(0, 4, 10, 10));
		tabbedPane.addTab("Newton", null, newtonPanel, null);
		
		JLabel lblNewLabel_3 = new JLabel("Smart Parking System");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setFont(new Font("Lucida Grande", Font.BOLD, 18));
		lblNewLabel_3.setBounds(-1, 0, 1000, 40);
		contentPane.add(lblNewLabel_3);
		
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
	    refreshOneArea(backBayPanel, "BackBay");
	    refreshOneArea(fenwayPanel, "Fenway");
	    refreshOneArea(downtownPanel, "Downtown");
	    refreshOneArea(seaportPanel, "Seaport");
	    refreshOneArea(newtonPanel, "Newton");
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

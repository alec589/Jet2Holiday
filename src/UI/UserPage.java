package UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Graph.MyGraph;
import Parking.AreaType;
import Parking.ParkingData;
import Parking.ParkingGraphBuilder;
import Parking.RecommendationService;
import Parking.ReservationService;
import Parking.Spot;
import Parking.UserAccount;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import java.util.Collection;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	private ReservationService reservationService;
	private UserAccount user;	
    private RecommendationService recommendationService;
    private MyGraph cityGraph;

    // make these panels available not only for initializeUI method
    private AreaMapPanel backBayPanel;
    private AreaMapPanel fenwayPanel;
    private AreaMapPanel downtownPanel;
    private AreaMapPanel seaportPanel;
    private AreaMapPanel newtonPanel;
    private JComboBox<String> comboBox_1;
    private JLabel resultLabel;
    
    private JSlider preferenceSlider; 
    
    private Collection<Spot> spots;
    private Spot recommendedSpot;
    private JButton btnNewButton_1;
    private JButton btnNewButton_2;
    
    
	public UserPage(UserAccount user, ReservationService service,ParkingData sharedData) {
		this.parkingData = sharedData;
		this.user = user;
		this.reservationService = service;
		initializeData();
		initializeUI();
		refreshAreaPanels();

	}

	private void initializeData() {
		ParkingGraphBuilder builder = new ParkingGraphBuilder(parkingData);
		cityGraph = builder.buildGraph();
	    
        recommendationService = new RecommendationService(cityGraph, parkingData);
    }
	
	private void initializeUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 800);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 238, 238));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Destination : ");
		lblNewLabel_1.setBounds(30, 66, 124, 16);
		contentPane.add(lblNewLabel_1);
		
		comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"Prudential Center", 
				"Copley Square", 
				"Newbury Street", 
				"Fenway Park", 
				"Museum of Fine Arts", 
				"Northeastern University", 
				"Boston Common", 
				"Faneuil Hall", 
				"State Street Office", 
				"Seaport World Trade Center", 
				"ICA Museum", 
				"Harborwalk", 
				"Newton Centre", 
				"Boston College", 
				"Newton Library"}));
		comboBox_1.setBounds(115, 61, 185, 27);
		contentPane.add(comboBox_1);
		
		JButton btnNewButton = new JButton("Recommend Spot");
		btnNewButton.addActionListener(e -> {
            String destinationName = (String) comboBox_1.getSelectedItem();

            double distanceWeight = preferenceSlider.getValue() / 100.0;
            double priceWeight = 1.0 - distanceWeight;

            recommendedSpot = recommendationService.recommend(
                destinationName,
                distanceWeight,
                priceWeight
            );

            if (recommendedSpot == null) {
                resultLabel.setText("No available spot found.");
            } else {
                resultLabel.setText(
                    "Recommended: " + recommendedSpot.getSpotId()
                    + " (" + recommendedSpot.getArea() + ")"
                );
            }

            refreshAreaPanels();
        });
		btnNewButton.setBounds(623, 61, 140, 29);
		contentPane.add(btnNewButton);
		
		resultLabel = new JLabel("Please choose a destination.");
		resultLabel.setBounds(775, 66, 279, 16);
		contentPane.add(resultLabel);
		// Reservation Buttons
		JButton reserveButton = new JButton("Reserve");
		reserveButton.setBounds(1035, 60, 120, 30);
		contentPane.add(reserveButton);

	
		// Handle reservation action
		reserveButton.addActionListener(e -> {
		    reservationService.cleanupExpiredReservations();

		    if (recommendedSpot == null) {
		        JOptionPane.showMessageDialog(null, "Please recommend a spot first.");
		        return;
		    }

		    if (user.isBlacklisted()) {
		        JOptionPane.showMessageDialog(null,
		            "You are temporarily blocked due to multiple missed reservations.");
		        return;
		    }

		    boolean success = reservationService.reserve(user, recommendedSpot);

		    if (!success) {
		        if (recommendedSpot.isOccupied()) {
		            JOptionPane.showMessageDialog(null,
		                "This parking spot is no longer available.");
		        } else {
		            JOptionPane.showMessageDialog(null, "Reservation failed.");
		        }

		        refreshAreaPanels();
		        return;
		    }

		    JOptionPane.showMessageDialog(null,
		        "Reservation successful for " + recommendedSpot.getSpotId());

		    recommendedSpot = null; 
		    refreshAreaPanels();
		});
		
		
		// User Preference Slider
		JLabel prefLabel = new JLabel("Preference:");
		prefLabel.setBounds(322, 66, 100, 16);
		contentPane.add(prefLabel);

		preferenceSlider = new JSlider(0, 100, 50); 
		preferenceSlider.setBounds(391, 48, 200, 40);
		preferenceSlider.setMajorTickSpacing(25);
		preferenceSlider.setMinorTickSpacing(5); 
		preferenceSlider.setPaintTicks(true);
		preferenceSlider.setPaintLabels(true);
		contentPane.add(preferenceSlider);

		// preference label description
		JLabel leftLabel = new JLabel("Affordable");
		leftLabel.setBounds(401, 89, 80, 16);
		contentPane.add(leftLabel);

		JLabel rightLabel = new JLabel("Convenient");
		rightLabel.setBounds(519, 89, 100, 16);
		contentPane.add(rightLabel);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(37, 130, 1120, 560);
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
		lblNewLabel_3.setBounds(87, 0, 1000, 40);
		contentPane.add(lblNewLabel_3);
		
		btnNewButton_1 = new JButton("Logout");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoginPage().setVisible(true);
			    dispose();
			}
		});
		btnNewButton_1.setBounds(794, 712, 117, 38);
		contentPane.add(btnNewButton_1);
		
		btnNewButton_2 = new JButton("View My Reservations");
		btnNewButton_2.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        // clean up Expired Reservations
		        reservationService.cleanupExpiredReservations();
		        		      
		        //switch panel
		        new MyReservationsPage(user, reservationService).setVisible(true);
		    }
		});

		btnNewButton_2.setBounds(264, 712, 180, 38);
		contentPane.add(btnNewButton_2);
		
		
	}

	private void refreshAreaPanels() {
	    
		backBayPanel.setSpots(parkingData.getSpotsByArea(AreaType.BACKBAY));
        fenwayPanel.setSpots(parkingData.getSpotsByArea(AreaType.FENWAY));
        downtownPanel.setSpots(parkingData.getSpotsByArea(AreaType.DOWNTOWN));
        seaportPanel.setSpots(parkingData.getSpotsByArea(AreaType.SEAPORT));
        newtonPanel.setSpots(parkingData.getSpotsByArea(AreaType.NEWTON));

        backBayPanel.setRecommendedSpot(recommendedSpot);
        fenwayPanel.setRecommendedSpot(recommendedSpot);
        downtownPanel.setRecommendedSpot(recommendedSpot);
        seaportPanel.setRecommendedSpot(recommendedSpot);
        newtonPanel.setRecommendedSpot(recommendedSpot);
        
        backBayPanel.setDestinations(parkingData.getDestinationsByArea(AreaType.BACKBAY));
        fenwayPanel.setDestinations(parkingData.getDestinationsByArea(AreaType.FENWAY));
        downtownPanel.setDestinations(parkingData.getDestinationsByArea(AreaType.DOWNTOWN));
        seaportPanel.setDestinations(parkingData.getDestinationsByArea(AreaType.SEAPORT));
        newtonPanel.setDestinations(parkingData.getDestinationsByArea(AreaType.NEWTON));
	}
	
	
	private void refreshOneArea(JPanel panel, AreaType area) {
        panel.removeAll();

        for (Spot spot : parkingData.getSpotsByArea(area)) {
            JPanel spotBox = new JPanel();
            spotBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            spotBox.setPreferredSize(new Dimension(100, 70));

            JLabel idLabel = new JLabel(spot.getSpotId(), SwingConstants.CENTER);
            JLabel statusLabel = new JLabel(
                spot.isOccupied() ? "Occupied" : "Available",
                SwingConstants.CENTER
            );

            if (recommendedSpot != null &&
                spot.getSpotId().equals(recommendedSpot.getSpotId())) {
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

            spotBox.add(idLabel);
            spotBox.add(statusLabel);
            panel.add(spotBox);
        }

        panel.revalidate();
        panel.repaint();
    }
}

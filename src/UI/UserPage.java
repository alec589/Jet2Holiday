package UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Graph.MyGraph;
import Parking.AreaType;
import Parking.ParkingData;
import Parking.RecommendationService;
import Parking.Reservation;
import Parking.ReservationService;
import Parking.Spot;
import Parking.UserAccount;

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
import javax.swing.Timer;

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
	private ReservationService reservationService;
	private UserAccount user;
	private Stack<Spot> historyStack = new Stack<>();
    private RecommendationService recommendationService;
    private MyGraph graph;

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
    
    private JLabel timerLabel;
    private Timer countdownTimer;
    private Spot reservedSpot;
    
    
	public UserPage(UserAccount user, ReservationService service,ParkingData sharedData) {
		this.parkingData = sharedData;
		this.user = user;
		this.reservationService = service;
		initializeUI();
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
		
		JLabel lblNewLabel_1 = new JLabel("Destination : ");
		lblNewLabel_1.setBounds(30, 56, 124, 16);
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
		comboBox_1.setBounds(115, 51, 154, 27);
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
                historyStack.push(recommendedSpot);
                resultLabel.setText(
                    "Recommended: " + recommendedSpot.getSpotId()
                    + " (" + recommendedSpot.getArea() + ")"
                );
            }

            refreshAreaPanels();
        });
		btnNewButton.setBounds(549, 49, 140, 29);
		contentPane.add(btnNewButton);
		
		resultLabel = new JLabel("Please choose a destination.");
		resultLabel.setBounds(702, 54, 300, 16);
		contentPane.add(resultLabel);
		// Reservation Buttons
		JButton reserveButton = new JButton("Reserve");
		reserveButton.setBounds(120, 110, 120, 30);
		contentPane.add(reserveButton);

		JButton checkInButton = new JButton("Check In");
		checkInButton.setBounds(260, 110, 120, 30);
		contentPane.add(checkInButton);

		JButton undoButton = new JButton("Undo");
		undoButton.setBounds(400, 110, 120, 30);
		contentPane.add(undoButton);
		// Reservation Timer Display

		timerLabel = new JLabel("No current reservation");
		timerLabel.setBounds(560, 115, 250, 20);
		contentPane.add(timerLabel);
		
		// Handle reservation action
		reserveButton.addActionListener(e -> {
		    reservationService.cleanupExpiredReservations();

		    if (recommendedSpot == null) {
		        javax.swing.JOptionPane.showMessageDialog(null, "Please recommend a spot first.");
		        return;
		    }

		    boolean success = reservationService.reserve(user, recommendedSpot);

		    if (!success) {
		        javax.swing.JOptionPane.showMessageDialog(null, "Reservation failed.");
		        refreshAreaPanels();
		        return;
		    }

		    reservedSpot = recommendedSpot;
		    javax.swing.JOptionPane.showMessageDialog(null,
		        "Reservation successful for " + reservedSpot.getSpotId());

		    startCountdown();
		    refreshAreaPanels();
		});
		
		// Handle check-in
		checkInButton.addActionListener(e -> {
		    reservationService.cleanupExpiredReservations();

		    if (reservedSpot == null) {
		        javax.swing.JOptionPane.showMessageDialog(null, "No active reservation.");
		        return;
		    }

		    boolean success = reservationService.checkIn(reservedSpot.getSpotId());

		    if (success) {
		        timerLabel.setText("Checked in successfully.");
		        stopCountdown();
		        javax.swing.JOptionPane.showMessageDialog(null, "Check-in successful.");
		    } else {
		        javax.swing.JOptionPane.showMessageDialog(null, "Check-in failed.");
		    }

		    refreshAreaPanels();
		});
		// Handle undo (last reservation only)
		undoButton.addActionListener(e -> {
		    boolean success = reservationService.undoLastReservation();

		    if (success) {
		        reservedSpot = null;
		        stopCountdown();
		        timerLabel.setText("Reservation undone.");
		        javax.swing.JOptionPane.showMessageDialog(null, "Undo successful.");
		    } else {
		        javax.swing.JOptionPane.showMessageDialog(null, "Nothing to undo.");
		    }

		    refreshAreaPanels();
		});
		// User Preference Slider
		JLabel prefLabel = new JLabel("Preference:");
		prefLabel.setBounds(274, 55, 100, 16);
		contentPane.add(prefLabel);

		preferenceSlider = new JSlider(0, 100, 50); 
		preferenceSlider.setBounds(348, 43, 200, 40);
		preferenceSlider.setMajorTickSpacing(25);
		preferenceSlider.setMinorTickSpacing(5); 
		preferenceSlider.setPaintTicks(true);
		preferenceSlider.setPaintLabels(true);
		contentPane.add(preferenceSlider);

		// preference label description
		JLabel leftLabel = new JLabel("Affordable");
		leftLabel.setBounds(358, 93, 80, 16);
		contentPane.add(leftLabel);

		JLabel rightLabel = new JLabel("Convenient");
		rightLabel.setBounds(479, 93, 100, 16);
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
		            
		            String info = "Last Viewed Spot: " + lastSpot.getSpotId() + 
		                          "\nArea: " + lastSpot.getArea() + 
		                          "\nPrice: $" + String.format("%.2f", lastSpot.getPrice());
		            
		            javax.swing.JOptionPane.showMessageDialog(null, info);
		        }
		    }
		});
		btnNewButton_2.setBounds(247, 721, 117, 38);
		contentPane.add(btnNewButton_2);
		
	}
	
	
	private void initializeData() {
		graph = parkingData.buildGraph();
        recommendationService = new RecommendationService(graph, parkingData);
    }
// Start reservation countdown (10 minutes)
	private void startCountdown() {
	    stopCountdown();

	    countdownTimer = new javax.swing.Timer(1000, e -> {
	        if (reservedSpot == null) {
	            timerLabel.setText("No active reservation");
	            stopCountdown();
	            return;
	        }

	        Reservation r = reservationService.getReservation(reservedSpot.getSpotId());

	        if (r == null) {
	            timerLabel.setText("Reservation expired or cancelled.");
	            reservedSpot = null;
	            stopCountdown();
	            refreshAreaPanels();
	            return;
	        }

	        if (r.isCheckedIn()) {
	            timerLabel.setText("Checked in successfully.");
	            stopCountdown();
	            return;
	        }

	        long remaining = r.getRemainingTimeMillis();
	        long minutes = remaining / 60000;
	        long seconds = (remaining % 60000) / 1000;

	        timerLabel.setText(String.format("Time left: %02d:%02d", minutes, seconds));

	        if (remaining <= 0) {
	            reservationService.cleanupExpiredReservations();
	            timerLabel.setText("Reservation expired.");
	            reservedSpot = null;
	            stopCountdown();
	            refreshAreaPanels();
	        }
	    });

	    countdownTimer.start();
	}
	// Stop countdown timer
	private void stopCountdown() {
	    if (countdownTimer != null) {
	        countdownTimer.stop();
	        countdownTimer = null;
	    }
	}
	private void refreshAreaPanels() {
		
		System.out.println("BACKBAY spots:");
	    for (Spot s : parkingData.getSpotsByArea(AreaType.BACKBAY)) {
	        System.out.println(s);
	    }
	    
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

package UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import Parking.ParkingData;
import Parking.UserAccount;
import Parking.ReservationService;

import Map.MapInterface;
import Map.MyHashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;
import javax.swing.JButton;

public class LoginPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTextField usernameField;
    private JPasswordField passwordField;

    // Shared  system datab about users, reservation service, and Parkingdata
    private static MapInterface<String, UserAccount> users = new MyHashMap<>();
    private static ReservationService reservationService = new ReservationService();
    private static ParkingData sharedData = new ParkingData();
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginPage frame = new LoginPage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public LoginPage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 800);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel titleLabel = new JLabel("Smart Parking System");
        titleLabel.setBounds(360, 180, 340, 49);
        titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
        contentPane.add(titleLabel);

        JLabel userLabel = new JLabel("Username :");
        userLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        userLabel.setBounds(260, 313, 126, 16);
        contentPane.add(userLabel);

        JLabel passLabel = new JLabel("Password : ");
        passLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        passLabel.setBounds(260, 410, 109, 16);
        contentPane.add(passLabel);

        usernameField = new JTextField();
        usernameField.setBounds(377, 305, 350, 30);
        contentPane.add(usernameField);

        passwordField = new JPasswordField();
        passwordField.setBounds(377, 400, 350, 30);
        contentPane.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(445, 529, 117, 36);
        contentPane.add(loginButton);
        
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(445, 580, 117, 36);
        contentPane.add(registerButton);
        
        loginButton.addActionListener((ActionEvent e) -> {
            handleLogin();
        });
        registerButton.addActionListener((ActionEvent e) -> {
            handleRegister();
        });
    }
    

    /**
     * ⭐ 核心登录逻辑
     */
    private void handleLogin() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        //  admin 
        if (username.equals("admin") && password.equals("1234")) {
            new AdminPage(sharedData).setVisible(true);
            dispose();
            return;
        }

     // User does not exist → Login is not permitted
        if (!users.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "User does not exist!");
            return;
        }

        // get user
        UserAccount user = users.get(username);

        // wrong password
        if (!user.checkPassword(password)) {
            JOptionPane.showMessageDialog(this, "Wrong password!");
            return;
        }

        //  Blacklist Check
        if (user.isBlacklisted()) {
            JOptionPane.showMessageDialog(this, "You are blacklisted!");
            return;
        }

        // Login successful then Enter user page 
        new UserPage(user, reservationService, sharedData).setVisible(true);
        dispose();
    }
    private void handleRegister() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // NULL CHECK
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username or password cannot be empty!");
            return;
        }

        // The user already exists
        if (users.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "User already exists!");
            return;
        }

        // Create a new user
        UserAccount newUser = new UserAccount(username, password);
        users.put(username, newUser);

        JOptionPane.showMessageDialog(this, "Registration successful!");
    }
}
/*
package UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import Parking.ParkingData;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;
import javax.swing.JButton;

public class LoginPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	// Launch the application. 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage frame = new LoginPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	 //Create the frame.
	// set JPasswordField
	private JTextField usernameField;
	private JPasswordField passwordField;
	
	public LoginPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Username :");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(260, 313, 126, 16);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel("Smart Parking System\n");
		lblNewLabel.setBounds(380, 180, 340, 49);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Password : ");
		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(260, 410, 109, 16);
		contentPane.add(lblNewLabel_2);
		
		usernameField = new JTextField();
		usernameField.setBounds(377, 305, 350, 30);
		contentPane.add(usernameField);
		usernameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(377, 400, 350, 30);
		contentPane.add(passwordField);
		passwordField.setColumns(10);
		
		JButton loginButton = new JButton("Login\n");
		loginButton.setBounds(445, 529, 117, 36);
		contentPane.add(loginButton);
		
		loginButton.addActionListener((ActionEvent e) -> {
            handleLogin();
        });
		
	}
	
	private void handleLogin() {
	    String username = usernameField.getText();
	    String password = new String(passwordField.getPassword());

	    ParkingData sharedData = new ParkingData(); 

	    if (username.equals("admin") && password.equals("1234")) {
	        new AdminPage(sharedData).setVisible(true);
	        dispose();

	    } else if (username.equals("user") && password.equals("1234")) {
	        new UserPage(sharedData).setVisible(true); 
	        dispose();

	    } else {
	        JOptionPane.showMessageDialog(this, "Invalid username or password");
	    }
	}

}*/

package UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
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

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
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

        if (username.equals("admin") && password.equals("1234")) {

            new AdminPage().setVisible(true);
            dispose();

        } else if (username.equals("user") && password.equals("1234")) {

            new UserPage().setVisible(true);
            dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }
    }

}

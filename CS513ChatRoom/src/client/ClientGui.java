package client;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.Font;
/**
 * Creates the server log in user interface. Sets the locations of different elements in the 
 * interface. Sends the nickname and port number to the server side and gets response. 
 * If success, creates the chat room user interface and starts the client thread. 
 * @author QiWang
 *
 */
public class ClientGui extends JFrame{
	
	private JPanel jPanel1;
	private JButton jButton1;
	private JLabel jLabel1;
	private JLabel jLabel12;
	private JTextField username;
	private JTextField portNumber;
	
	

	public ClientGui() throws HeadlessException {
		super();
		this.setLayout();
	}
	
	public void setLayout() {
		this.jPanel1 = new JPanel();
		
		this.jLabel1 = new JLabel();
		jLabel1.setFont(new Font("Times New Roman", Font.PLAIN, 36));
		jLabel1.setBounds(161, 84, 177, 41);
		jLabel1.setText("Username: ");
		
		this.jLabel12 = new JLabel();
		jLabel12.setFont(new Font("Times New Roman", Font.PLAIN, 36));
		jLabel12.setBounds(161, 190, 222, 41);
		jLabel12.setText("Port Number: ");
		
		this.jButton1 = new JButton();
		jButton1.setFont(new Font("Times New Roman", Font.BOLD, 30));
		jButton1.setBounds(347, 342, 138, 54);
		jButton1.setText("Log In");
		
		this.username = new JTextField();
		username.setFont(new Font("Tahoma", Font.PLAIN, 30));
		username.setBounds(390, 84, 291, 54);
		username.setText("qq");
		
		this.portNumber = new JTextField();
		portNumber.setFont(new Font("Tahoma", Font.PLAIN, 30));
		portNumber.setBounds(390, 190, 291, 54);
		portNumber.setText("8888");
		jPanel1.setLayout(null);
		
		jPanel1.add(jLabel1);
		jPanel1.add(jLabel12);
		jPanel1.add(jButton1);
		jPanel1.add(username);
		jPanel1.add(portNumber);
		this.getContentPane().add(jPanel1);
		
		this.setVisible(true);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		int width = dim.width / 3;
		int height = dim.height / 2;
		
		this.setSize(width, height);
		
		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		
		this.setLocation(xPos, yPos);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ListenForButton lForButton = new ListenForButton();
		this.jButton1.addActionListener(lForButton);
		
	}
	
	private class ListenForButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ClientGui.this.logIn(e);
		}
		
	}
	/**
	 * Sends the log in information to the server and get result from server.
	 * Creates new client thread or displays error message.
	 * @param evt The event of pressing button.
	 */
	private void logIn(ActionEvent evt) {
		String username = this.username.getText();
		String portNumber = this.portNumber.getText();
		ClientConnect clientConnect = new ClientConnect(this, Integer.parseInt(portNumber), username);
		
		if (clientConnect.logIn()) {
			new Thread(clientConnect).start();
		} else {
			JOptionPane.showMessageDialog(this, "User name exists or wrong port number", "error",
					JOptionPane.ERROR_MESSAGE);
		}
	}



	public static void main(String[] args) {
		new ClientGui();
	}

}

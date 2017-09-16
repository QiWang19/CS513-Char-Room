package server;

import java.awt.*;
import javax.swing.*;

import util.XmlUtil;

import java.util.*;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
/**
 * Creates the start server graphical user interface. After click the start button the server 
 * thread will be started and wait for any client to connect to this server.
 * @author QiWang
 *
 */
public class ServerGui extends JFrame{
	
	private JPanel jPanel1;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JButton jButton1;
	private JTextField jTextField1;
	
	private Map<String, ServerChannel> map;
	
	

	public ServerGui() {
		
		map = new HashMap<String, ServerChannel>();
		setLayout();
	}
	/**
	 * Sets the locations of the user interface. Specifically, define the function of satrt button 
	 * and close window button.
	 */
	public void setLayout() {
		jLabel2 = new JLabel();
		jLabel2.setFont(new Font("Tahoma", Font.PLAIN, 22));
		jLabel2.setSize(190, 25);
		jLabel2.setLocation(225, 200);
		jPanel1 = new JPanel();
		jLabel1 = new JLabel();
		jLabel1.setBounds(21, 64, 202, 42);
		jLabel1.setFont(new Font("Times New Roman", Font.PLAIN, 36));
		jTextField1 = new JTextField(15);
		jTextField1.setBounds(233, 64, 325, 43);
		jTextField1.setFont(new Font("Tahoma", Font.PLAIN, 30));
		jButton1 = new JButton();
		jButton1.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		jButton1.setBounds(239, 137, 182, 35);
		
		jPanel1.setName("Server");
		this.setName("Start Server");
		this.setType(Type.POPUP);
		this.setTitle("Start Server");
		jLabel1.setText("Port Number: ");
		jTextField1.setText("8888");
		jButton1.setText("Start Server");
		jPanel1.setLayout(null);
		
		jPanel1.add(jLabel1);
		jPanel1.add(jTextField1);
		jPanel1.add(jButton1);
		jPanel1.add(jLabel2);
		
		this.getContentPane().add(jPanel1);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		this.pack();
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		int width = dim.width / 4;
		int height = dim.height / 3;
		this.setSize(width, height);
		
		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		this.setLocation(xPos, yPos);
		
		ListenForButton lForButton = new ListenForButton();
		jButton1.addActionListener(lForButton);
		
		ListenForWindow lForWindow = new ListenForWindow();
		this.addWindowListener(lForWindow);
		
	}
	/**
	 * If the server has been closed, send the message to all clients and close the system.
	 * @author QiWang
	 *
	 */
	private class ListenForWindow extends WindowAdapter {

		
		public void windowClosing(WindowEvent e) {
			try {
				String xml = XmlUtil.buildCloseServerWindowMsg();
				for(ServerChannel channel: ServerGui.this.getMap().values()) {
					channel.send(xml);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				System.exit(0);
			}
			
		}

		
	}
	/**
	 * Strat the server thread and wait for any client to connect to the server.
	 * @author QiWang
	 *
	 */
	private class ListenForButton implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			ServerGui.this.execute(e);
		}
		
	}
	
	public void execute(ActionEvent evt) {
		int port =Integer.parseInt(this.getjTextField1().getText()) ;
		new Thread( new ServerConnect(this, port)).start();
	}

	public Map<String, ServerChannel> getMap() {
		return map;
	}

	public void setMap(Map<String, ServerChannel> map) {
		this.map = map;
	}


	public void setjPanel1(JPanel jPanel1) {
		this.jPanel1 = jPanel1;
	}


	public JLabel getjLabel1() {
		return jLabel1;
	}



	public void setjLabel1(JLabel jLabel1) {
		this.jLabel1 = jLabel1;
	}


	public JButton getjButton1() {
		return jButton1;
	}


	public void setjButton1(JButton jButton1) {
		this.jButton1 = jButton1;
	}


	public JTextField getjTextField1() {
		return jTextField1;
	}


	public void setjTextField1(JTextField jTextField1) {
		this.jTextField1 = jTextField1;
	}

	

	public JLabel getjLabel2() {
		return jLabel2;
	}

	public void setjLabel2(JLabel jLabel2) {
		this.jLabel2 = jLabel2;
	}

	public static void main(String[] args) {
		new ServerGui();
	}

}

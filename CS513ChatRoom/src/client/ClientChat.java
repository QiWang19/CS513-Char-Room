package client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.TitledBorder;

import server.ServerChannel;
import server.ServerGui;

import util.XmlUtil;
/**
 * Creates the chat room graphical user interface. Sets the locations of elements in the user interface.
 * Objects of this class will be created when the client has successfully logged in. Sends messages 
 * to the server if the client has sent chat messages to other client or disconnected the server. 
 * @author QiWang
 *
 */
public class ClientChat extends JFrame{
	
	private ClientConnect clientConnect;
	
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	
	private JButton jButton1;
	
	private JTextArea jTextArea1;
	public JTextArea getjTextArea1() {
		return jTextArea1;
	}

	public void setjTextArea1(JTextArea jTextArea1) {
		this.jTextArea1 = jTextArea1;
	}

	public JTextArea getjTextArea2() {
		return jTextArea2;
	}

	public void setjTextArea2(JTextArea jTextArea2) {
		this.jTextArea2 = jTextArea2;
	}

	private JTextArea jTextArea2;
	private JTextField jTextField1;
	private JTextArea jTextArea3;
	
	public ClientChat(ClientConnect clientConnect ) {
		super();
		this.clientConnect = clientConnect;
		this.setLayout();
		
	}
	

	public void setLayout() {
		this.jPanel1 = new JPanel();
		this.jPanel2 = new JPanel();
		this.jPanel3 = new JPanel();
		
		this.jScrollPane1  = new JScrollPane();
		this.jScrollPane2 = new JScrollPane();
		this.jScrollPane3 = new JScrollPane();
		
		this.jButton1 = new JButton();
		jButton1.setFont(new Font("Tahoma", Font.PLAIN, 30));
		
		this.jTextArea1 = new JTextArea();
		jTextArea1.setFont(new Font("Monospaced", Font.PLAIN, 23));
		this.jTextArea2 = new JTextArea();
		jTextArea2.setFont(new Font("Monospaced", Font.PLAIN, 23));
		this.jTextField1 = new JTextField();
		
		this.jTextArea3 = new JTextArea();
		jTextArea3.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		
		ListenForFocus l = new ListenForFocus();
		jTextArea3.setText("Please type \nStart with '@name:' to whisper to some user. eg. @Bob: Hello");
		jTextArea3.addFocusListener(l);
		
		this.jScrollPane1.setViewportView(jTextArea1);
		this.jScrollPane2.setViewportView(jTextArea2);
		this.jScrollPane3.setViewportView(jTextArea3);
		jPanel1.setLayout(new BorderLayout(18, 17));
		this.jPanel1.add(jScrollPane1, BorderLayout.NORTH);
		
		this.jPanel1.add(jPanel3);
		
		BorderLayout bl_jPanel2 = new BorderLayout();
		bl_jPanel2.setVgap(25);
		bl_jPanel2.setHgap(18);
		jPanel2.setLayout(bl_jPanel2);
		this.jPanel2.add(jScrollPane2, BorderLayout.NORTH);
		this.jPanel2.add(jButton1);
		
		this.jPanel3.setLayout(new BorderLayout());
		this.jPanel3.add(jScrollPane3, BorderLayout.SOUTH);
		
		getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(jPanel1, BorderLayout.WEST);
		this.getContentPane().add(jPanel2, BorderLayout.EAST);
		
		jTextArea1.setEditable(false);
		jTextArea1.setColumns(36);
		jTextArea1.setRows(13);
		
		jTextArea2.setEditable(false);
		jTextArea2.setColumns(18);
		jTextArea2.setRows(15);
		
		
		jTextArea3.setColumns(36);
		jTextArea3.setRows(5);
		jTextArea3.setLineWrap(true);
		
		jPanel1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Chat Room Info", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		jPanel2.setBorder(BorderFactory.createTitledBorder("Online Users"));
		
		this.jButton1.setText("Send");

		this.pack();
		this.setVisible(true);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		int width = (int) (dim.width / 2.2);
		int height = (int) (dim.height / 2);
		this.setSize(width, height);
		
		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		this.setLocation(xPos, yPos);
		
		this.setTitle("Chat Room" + "--" + this.clientConnect.getUsername());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		ListenForWindow lForWindow = new ListenForWindow();
		this.addWindowListener(lForWindow);
		
		ListenForButton lForButton = new ListenForButton();
		this.jButton1.addActionListener(lForButton);
		
	}
	/**
	 * Sends message to the server when the client disconnects the server.
	 * @author QiWang
	 *
	 */
	private class ListenForWindow extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			try {
				ClientChat.this.clientConnect.sendMsg("Client leaves", 3);
				
			} catch (Exception e1) {
				
			}
			
		}
	}
	/**
	 * Sends message to the server when the client sends chat messages to other clients.
	 * @author QiWang
	 *
	 */
	private  class ListenForButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = ClientChat.this.jTextArea3.getText();
			ClientChat.this.jTextArea3.setText("");
			ClientChat.this.clientConnect.sendMsg(message, 2);
		}
		
	}
	
	private class ListenForFocus extends FocusAdapter {
		public void focusGained(FocusEvent e)
        {
			ClientChat.this.jTextArea3.setText("");
        }
	}

	
	
//	public static void main(String[] args) {
//		new ClientChat();
//	}
}

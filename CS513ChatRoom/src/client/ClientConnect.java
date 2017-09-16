package client;

import java.net.*;
import java.util.List;

import javax.swing.JOptionPane;

import util.XmlUtil;

import java.io.*;
/**
 * This class creates the clients thread to make connection with the server.
 * It can build messages and send it to server. And also can read the message from the server.
 * @author QiWang
 *
 */
public class ClientConnect implements Runnable{
	
	private static final int BUFF_LENGTH = 5000;
	private String hostAddress = "localhost";
	private int portNumber;
	private String username;
	private ClientGui clientGui;
	private Socket client;
	private InputStream dis;
	private OutputStream dos;
	private ClientChat clientChat;
	
	private boolean isRunning = true;
	/**
	 * Create a new server socket.
	 * @param clientGui The log in interface.
	 * @param portNumber Get port number from the log in user interface.
	 * @param username Get the user name from the log in user interface.
	 */
	public ClientConnect(ClientGui clientGui, int portNumber, String username) {
		this.clientGui = clientGui;
		this.portNumber = portNumber;
		this.username = username;
		this.connect();
	}
	
	
	
	public Socket getClient() {
		return client;
	}



	public String getUsername() {
		return username;
	}



	private void connect() {
		try {
			this.client = new Socket(this.hostAddress, this.portNumber);
			this.dis = this.client.getInputStream();
			this.dos = this.client.getOutputStream();
		
		} catch (IOException e) {
			JOptionPane.showMessageDialog(clientGui, "Server does not exist !", "Info", JOptionPane.INFORMATION_MESSAGE);
			
		}
	}
	
	public boolean logIn() {
		
		try {
			String xml = XmlUtil.buildLoginMsg(this.username);
			dos.write(xml.getBytes());
			
			byte[] buffer = new byte[BUFF_LENGTH];
			int length = dis.read(buffer);
			
			String loginResponse = new String(buffer, 0, length);
			String response = XmlUtil.getLoginResponse(loginResponse);
			
			if (response.equals("Success")) {
				this.clientChat = new ClientChat(this);
				this.clientGui.setVisible(false);
				String msg = XmlUtil.buildAddUser(this.username);
				dos.write(msg.getBytes());
				return true;
			}
			
			
		} catch (IOException e) {
			
		}
		return false;
	}
	
	public void sendMsg(String message, int type) {
		try {
			int num = type;
			String xml = "";
			if (type == 2) {
				xml = XmlUtil.buildMsg(this.username, message);
			} else if (type == 3) {
				xml = XmlUtil.buildCloseClientWindowMsg(this.username);
			}
			this.dos.write(xml.getBytes());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
/**
 * Reads the message sent by server, and gives the response according to different kinds of messages.
 */
	@Override
	public void run() {
		try {
			while(isRunning) {
				byte[] buffer = new byte[BUFF_LENGTH];
				int length = this.dis.read(buffer);
				
				String xml = new String(buffer, 0, length);
				int type = Integer.parseInt(XmlUtil.getType(xml));
				/**server message*/
				if (type == 4) {
					String content = XmlUtil.getMsg(xml);
					this.clientChat.getjTextArea1().append(content + "\n");
				/**server window has closed*/
				} else if (type == 5) {
					JOptionPane.showMessageDialog(clientChat, "Server has closed, exiting", "Info", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				/**client closed window response*/	
				}else if (type == 7) {
					try {
						this.getClient().getInputStream().close();
						this.getClient().getOutputStream().close();
						this.getClient().close();
					} catch (Exception e) {
					} finally {
						System.exit(0);
					}
				/** update the online user list*/
				} else if (type == 8) {
					String str = "";
					List<String> names = XmlUtil.getUserList(xml);
					for(String name: names) {
						str = str + name + "\n";
						
					}
					this.clientChat.getjTextArea2().setText(str);
				/** handle the situation if the client sends message to a non-existing client*/
				} else if (type == 9) {
					JOptionPane.showMessageDialog(clientChat, "User does not exist!", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

}

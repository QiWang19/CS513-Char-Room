package server;

import java.io.IOException;
import java.net.*;
import java.io.*;
import util.XmlUtil;

import javax.swing.JOptionPane;
/**
 * Implements the server thread concerning to log in process. Meanly has two steps, first start 
 * the server to wait for any client to connect to this server. Second, start the thread if any 
 * client has connect to this server, check if the client can enter the chat room.
 *  
 * @author QiWang
 *
 */
public class ServerConnect implements Runnable{
	
	private ServerSocket server;
	private ServerGui serverGui;
	private boolean isRunning = true;
	public static final int BUFF_LENGTH = 5000;
	
/**
 * 
 * @param serverGui	Get the log in user interface in order to send error message in case.
 * @param portNumber	Get the port number to start the server.
 */
	public ServerConnect(ServerGui serverGui, int portNumber) {
		super();
		try {
			this.serverGui = serverGui;
			this.server = new ServerSocket(portNumber);
			this.serverGui.getjLabel2().setText("Server is working");
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.serverGui, "Invalid port number!", "Warning", JOptionPane.ERROR_MESSAGE);
		}
	}


/**
 * Check if the user name has been used by other clients and return the success or failure result 
 * back to the client. If success, start a new thread for the client and update the online user list.
 */
	@Override
	public void run() {
		while(this.isRunning ) {
			try {
				Socket client  = this.server.accept();
				
				InputStream dis = client.getInputStream();
				OutputStream dos = client.getOutputStream();
				
				byte[] buffer = new byte[BUFF_LENGTH];
				int length = dis.read(buffer);
				
				String loginXml = new String(buffer, 0, length);
				String userName = XmlUtil.getUserName(loginXml);
				String loginMsg;
				boolean isLogin = false;
				
				if (this.serverGui.getMap().containsKey(userName)) {
					loginMsg = "Fail";
				} else {
					loginMsg = "Success";
					isLogin = true;
				}
				
				String xml = XmlUtil.buildLoginResponseMsg(loginMsg);
				dos.write(xml.getBytes());
				
				if (isLogin) {
					ServerChannel channel = new ServerChannel(this.serverGui, client);
					this.serverGui.getMap().put(userName, channel);
					channel.updateUserList(userName);
					
					new Thread(channel).start();
					String str =userName +  " is online now!";
					String msg = XmlUtil.buildServerMsg(str);
//					for (ServerChannel c:  this.serverGui.getMap().values()) {
//						c.send(msg);
//					}
					
				}
						 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

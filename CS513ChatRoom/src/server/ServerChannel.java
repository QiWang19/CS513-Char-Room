package server;

import java.io.*;
import java.net.*;
import java.util.*;

import util.CloseUtil;
import util.XmlUtil;
/**
 * A class used to transfer message between client and server. The server can send  and receive
 * message through this class. To see if the client has sent chat message of a client has disconnected.
 * @author QiWang
 *
 */
public class ServerChannel implements Runnable{
	
	private static final int BUFF_LENGTH = 5000;
	private ServerGui serverGui;
	private InputStream dis;
	private OutputStream dos;
	private boolean isRunning = true;
	
	public ServerChannel(ServerGui serverGui,  Socket client) {
		try {
				this.serverGui = serverGui;
				this.dos = client.getOutputStream();
				this.dis = client.getInputStream();
		} catch (IOException e) {
			isRunning = false;
			CloseUtil.closeAll(dis, dos);
		}
	}
	/**
	 * Used to send message through input and output stream.
	 * @param msg
	 */
	public void send(String msg) {
		
		try {
				dos.write(msg.getBytes());
				dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			this.isRunning = false;
			CloseUtil.closeAll(dos);
		}
	}
	
	public String updateUserList(String name) {
		Set<String> users = this.serverGui.getMap().keySet();
		String xml = XmlUtil.buildUserList(users);
		for (ServerChannel channel:  this.serverGui.getMap().values()) {
			channel.send(xml);
		}
		if(name != null) {
			return name;
		}
		return null;
	}
	
/**
 * Processes the chat and whisper message.
 * If a client has left, update the user list and inform other clients.
 */
	@Override
	public void run() {
		while(isRunning) {
			
			try {
				byte[] buffer = new byte[BUFF_LENGTH];
				int length = this.dis.read(buffer);
				String xml = new String(buffer, 0, length);
				System.out.println(xml.toString());
				int type = Integer.parseInt(XmlUtil.getType(xml));
				
				switch(type) {
				/** client sent message */
					case 2: 
						String userName = XmlUtil.getUserName(xml);
						String content = XmlUtil.getMsg(xml);
						String message = userName + ": " + content;
						String msgXml = XmlUtil.buildServerMsg(message);
						Map<String, ServerChannel> map = this.serverGui.getMap();
						if (content.startsWith("@") && content.indexOf(":") > -1) {
							
							String name = content.substring(1, content.indexOf(":"));
							String c = content.substring(content.indexOf(":") + 1);
							String str = userName + " whispers to " + name + ": " + c;
							String msg = XmlUtil.buildServerMsg(str);
							if (!map.containsKey(name)) {
								String error = XmlUtil.buildNonUserResponse();
								this.send(error);
								break;
							}
							this.send(msg);
							for (String s: map.keySet()) {
								if (s.equals(name)) {
									map.get(s).send(msg);
									
								}
							}
						} else {
							for(ServerChannel channel: map.values()) {
								channel.send(msgXml);
							}
						}
						
						
						break;
					/** client closed window */
					case 3: 
						String userName1 = XmlUtil.getUserName(xml);
						ServerChannel channel = this.serverGui.getMap().get(userName1);
						String response = XmlUtil.buildCloseClientWindowResponse();
						channel.send(response);
						this.serverGui.getMap().remove(userName1);
						this.updateUserList(null);
						this.dis.close();
						this.dos.close();
						
						String str = userName1 + " disconnected!";
						String msg = XmlUtil.buildServerMsg(str);
						for(ServerChannel c: this.serverGui.getMap().values()) {
							c.send(msg);
						}
						break;
					/** Send the message that a new client is on line */
					case 10: 
						String userName2 = XmlUtil.getUserName(xml);
						String str1 = userName2 + " is on line now !";
						String msg1 = XmlUtil.buildServerMsg(str1);
						for(ServerChannel c: this.serverGui.getMap().values()) {
							c.send(msg1);
						}
				}
				
			} catch (IOException e) {
				
			}
		}
	}
	

}

package util;

import org.dom4j.io.*;
import org.dom4j.*;
import java.io.*;
import java.util.*;
/**
 * Contains several methods to read and build XML documents using dom4j.
 * @author QiWang
 *
 */
public class XmlUtil {
	
	
	
	public static String getUserName(String xml) {
		
		
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml) );
			Element userName = document.getRootElement().element("username");
			return userName.getText();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getLoginResponse(String xml) {
		String response = null;
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			Element root = document.getRootElement();
			response = root.element("response").getText();
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return response;
		
	}
	
	public static String buildLoginMsg(String xml) {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("message");
		Element type = document.getRootElement().addElement("type"); 
		type.setText("1");
		
		Element username = rootElement.addElement("username");
		username.setText(xml);
		return document.asXML();
	}
	
//	public static void buildUserList(Set<String> users) {
//		Document document = DocumentHelper.createDocument();
//		
//	}
	
	public static String getType(String xml) {
		
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml) );
			Element type = document.getRootElement().element("type");
			return type.getText();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getMsg(String xml) {
		
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml) );
			Element msgElement = document.getRootElement().element("content");
			return msgElement.getText();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String buildServerMsg(String xml) {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("message");
		Element type = rootElement.addElement("type"); 
		type.setText("4");
		
		Element content = rootElement.addElement("content");
		content.setText(xml);
		return document.asXML();
	}
	
	public static String buildCloseServerWindowMsg() {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("message");
		Element type = rootElement.addElement("type"); 
		type.setText("5");
		return document.asXML();
	}
	
	public static String buildCloseClientWindowMsg(String username) {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("message");
		Element type = rootElement.addElement("type"); 
		type.setText("3");
		Element user = rootElement.addElement("username");
		user.setText(username);
		return document.asXML();
	}
	
	public static String buildLoginResponseMsg(String xml) {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("message");
		Element type = rootElement.addElement("type");
		type.setText("6");
		Element response = rootElement.addElement("response");
		response.setText(xml);
		return document.asXML();
	}
	
	public static String buildMsg(String username, String message) {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("message");
		Element type = rootElement.addElement("type");
		type.setText("2");
		Element user = rootElement.addElement("username");
		user.setText(username);
		Element response = rootElement.addElement("content");
		response.setText(message);
		return document.asXML();
	}

	public static String buildCloseClientWindowResponse() {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("message");
		Element type = rootElement.addElement("type");
		type.setText("7");
		return document.asXML();
	}
	
	public static String buildUserList(Set<String> users) {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("message");
		Element type = rootElement.addElement("type");
		type.setText("8");
		for (String name: users) {
			Element username = rootElement.addElement("username");
			username.setText(name);
			
		}
		return document.asXML();
	}
	
	public static List<String> getUserList(String xml) {
		List<String> names = new ArrayList<String>();
		
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml) );
			
			for (Iterator i = document.getRootElement().elementIterator("username"); i.hasNext();) {
				Element e = (Element) i.next();
				names.add(e.getText());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return names;
	}

	public static String buildNonUserResponse() {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("message");
		Element type = rootElement.addElement("type"); 
		type.setText("9");
		return document.asXML();
	}
	public static String buildAddUser(String username) {
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("message");
		Element name = rootElement.addElement("username");
		name.setText(username);
		Element type = rootElement.addElement("type"); 
		type.setText("10");
		return document.asXML();
		
	}
}

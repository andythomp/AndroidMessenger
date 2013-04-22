package edu.carleton.comp4104.assignment2.client;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import edu.carleton.comp4104.assignment2.common.Acceptor;
import edu.carleton.comp4104.assignment2.common.Connector;
import edu.carleton.comp4104.assignment2.common.HttpAcceptor;
import edu.carleton.comp4104.assignment2.common.HttpConnector;
import edu.carleton.comp4104.assignment2.common.Message;
import edu.carleton.comp4104.assignment2.common.ObjectAcceptor;
import edu.carleton.comp4104.assignment2.common.ObjectConnector;
import edu.carleton.comp4104.assignment2.common.Reactor;
import edu.carleton.comp4104.assignment2.common.Services;

public class ClientNetworkManager implements Runnable{

	
	String host;
	
	
	Acceptor acceptor;
	Connector connector;
	


	protected Handler guihandle;
	protected MainActivity guiactivity;
	protected ArrayList<String> messages;
	protected Reactor reactor;
	private String serviceType;
	private int connectorPort, acceptorPort;
	
	//Constructor for our Connector
	public ClientNetworkManager(Handler guihandle, MainActivity guiactivity, Properties config){
		//Initialize variables
		this.messages = new ArrayList<String>();
		this.guihandle = guihandle;
		this.guiactivity = guiactivity;
		
		try{
			//Read in from the property file
			String filename = config.getProperty(Services.SERVICES);
			serviceType = config.getProperty(Services.SERVICE_TYPE);
			this.acceptorPort = Integer.valueOf(config.getProperty(Acceptor.ACCEPTOR_PORT));
			this.host = config.getProperty(Connector.CONNECTOR_ADDRESS);
			this.connectorPort = Integer.valueOf(config.getProperty(Connector.CONNECTOR_PORT));
			Resources r = guiactivity.getResources();
			AssetManager assets =  r.getAssets();
			InputStream inputStream;
			
			try {
				//Get the services file
				inputStream = assets.open(filename);
			  	Properties services = new Properties();
			  	services.load(inputStream);
			  	inputStream.close();
			  	//Determine which kind of service we are running today
			    if (this.serviceType.equals(Services.HTTP_TYPE))
					acceptor = new HttpAcceptor(acceptorPort, connectorPort, services);
				else if (this.serviceType.equals(Services.OBJECT_TYPE))
					acceptor = new ObjectAcceptor(acceptorPort, connectorPort, services);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e){
			displayGUIMessage("Error: Could not read some of config file.");
			e.printStackTrace();
		}
	
		
		
	}
	

	//This is just a method to simplify the act of posting a message
	//to the GUI
	private void displayGUIMessage(final String message){
		guihandle.post(new Runnable(){
			public void run(){
				guiactivity.showMessage(message);
			}
		});
	}
	
	
	
	
	/*
	 * These functions are involved in sending Instant Messages
	 * ****************************************************************************************
	 */
	
	//This is to send a post message.
	public void sendPostMessage(String message, String username){

		MainActivity.logClient("Connecting to: " + host + " on port: " + connectorPort);
		try{
			if(serviceType.equals(Services.HTTP_TYPE)){
				sendHttpIM(message, username);
			}
			else if (serviceType.equals(Services.OBJECT_TYPE)){
				sendObjectIM(message, username);
			}
		} catch (NullPointerException e){
			displayGUIMessage("Error: Current service type defined is: " + serviceType);
			e.printStackTrace();
		}

	}
	
	
	private void sendHttpIM(String im, String username) {
		Message message = new Message(Message.IM);
		message.setHeader(Message.POST + " " + Message.IM + " " + Message.HTTP);
		message.addContent(Message.MESSAGE, im);
		message.addContent(Message.USER_NAME, username);
		connector = new HttpConnector(host, connectorPort);
		connector.setPayload(message);
		new Thread(connector).start();
		
	}


	private void sendObjectIM(String im, String username) {
		Message message = new Message(Message.IM);
		message.addContent(Message.MESSAGE, im);
		message.addContent(Message.USER_NAME, username);
		connector = new ObjectConnector(host, connectorPort);
		connector.setPayload(message);
		new Thread(connector).start();
	}

	
	/*
	 * ****************************************************************************************
	 */
	
	
	/*
	 * These functions are involved in sending Login Messages
	 * ****************************************************************************************
	 */
	
	//This is to send a post message.
	public void sendLoginMessage(){
		MainActivity.logClient("Connecting to: " + host + " on port: " + connectorPort);
		try{
			if(serviceType.equals(Services.HTTP_TYPE)){
				sendHttpLogin();
			}
			else if (serviceType.equals(Services.OBJECT_TYPE)){
				sendObjectLogin();
			}
		} catch (NullPointerException e){
			displayGUIMessage("Error: Current service type defined is: " + serviceType);
			e.printStackTrace();
		}
	}
	
	
	private void sendHttpLogin() {
		Message message = new Message(Message.LOGIN);
		message.setHeader(Message.POST + " " + Message.LOGIN + " " + Message.HTTP);
		message.addContent(Message.USER_NAME, guiactivity.getClientName());
		connector = new HttpConnector(host, connectorPort);
		connector.setPayload(message);
		new Thread(connector).start();
	}


	private void sendObjectLogin() {
		Message message = new Message(Message.LOGIN);
		message.addContent(Message.USER_NAME, guiactivity.getClientName());
		connector = new ObjectConnector(host, connectorPort);
		connector.setPayload(message);
		new Thread(connector).start();
	}

	
	/*
	 * ****************************************************************************************
	 */
	
	
	/*
	 * These functions are involved in sending Logout Messages
	 * ****************************************************************************************
	 */
	
	//This is to send a post message.
	public void sendLogoutMessage(){
		MainActivity.logClient("Connecting to: " + host + " on port: " + connectorPort);
		try{
			if(serviceType.equals(Services.HTTP_TYPE)){
				sendHttpLogout();
			}
			else if (serviceType.equals(Services.OBJECT_TYPE)){
				sendObjectLogout();
			}
		} catch (NullPointerException e){
			displayGUIMessage("Error: Current service type defined is: " + serviceType);
			e.printStackTrace();
		}
	}
	
	
	private void sendHttpLogout() {
		Message message = new Message(Message.LOGOUT);
		message.setHeader(Message.POST + " " + Message.LOGOUT + " " + Message.HTTP);
		message.addContent(Message.USER_NAME, guiactivity.getClientName());
		connector = new HttpConnector(host, connectorPort);
		connector.setPayload(message);
		new Thread(connector).start();
		
	}


	private void sendObjectLogout() {
		Message message = new Message(Message.LOGOUT);
		message.addContent(Message.USER_NAME, guiactivity.getClientName());
		connector = new ObjectConnector(host, connectorPort);
		connector.setPayload(message);
		new Thread(connector).start();
	}

	
	/*
	 * ****************************************************************************************
	 */
	
	
	
	@Override
	public void run() {
		
		new Thread(acceptor).start();
		sendLoginMessage();
	}
}

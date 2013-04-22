package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;
import java.net.Socket;

public abstract class Connector implements Runnable{

	//Variables
	protected Socket peer;
	protected String host;
	protected int port;
	protected Message message;
	
	//CONSTANTS
	public static final String CONNECTOR_ADDRESS = "CONNECTOR_ADDRESS";
	public static final String CONNECTOR_PORT = "CONNECTOR_PORT";

	public Connector(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	
	//this function creates a connection to the server.
	public void connect(String host, int port) throws IOException{
		peer = new Socket(host, port);
	}
	
	//This does a more accurate check as to whether or not the socket is connected.
	public boolean isConnected(){
		try{
			if (peer.isConnected() && !peer.isClosed()){
				return true;
			}
			else{
				return false;
			}
		}
		catch(Exception e){
			return false;
		}
	}
	public void close(){
		if (!peer.equals(null)){
			try {
				peer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Sets the message payload
	public void setPayload(Message message){
		this.message = message;
	}
	
	//Must be overridden by which ever kind of connector you implement
	public abstract void sendMessage() throws IOException;
	
	//This connects to the host, sends a message, and then terminates.
	@Override
	public void run() {
		try {
			//System.out.println("Delivering payload to :" + host + " on port: " + port);
			connect(host,port);
			System.out.println("Connected to: " + host);
			sendMessage();
		}  catch (IOException e) {
			System.out.println("Error: Could not connect to: " + host + " on port: " + port);
		}
	
	}
}

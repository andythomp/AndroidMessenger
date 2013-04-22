package edu.carleton.comp4104.assignment2.server;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import edu.carleton.comp4104.assignment2.common.Acceptor;
import edu.carleton.comp4104.assignment2.common.Connector;
import edu.carleton.comp4104.assignment2.common.Services;




public class IMServer {

	public static final int SERVER_PORT = 4567;
	public static final String DEFAULT_FILE_NAME = "server.cfg";
	public static final int MINPORT = 0;
	public static final int MAXPORT = 65536;
	
	private static ServerNetworkManager networkManager;
	
	

	//Initialize the vault
	public IMServer(){
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int acceptorPort, connectorPort;
		String filename = null;
		//Read in arguements
		for (int i = 0; i < args.length; i++){
			if (args[i].equals("-c")){
				try{
					filename = args[i+1];
				}
				catch (NullPointerException e){
					System.out.println("Error: No file name was given with -c");
					return;
				}
			}
		}
		//Check our file name
		if (filename==null){
			System.out.println("Error: Please pass file name via command arguements (i.e. -c server.cfg");
			return;
		}
		//Read in some property files
		Properties properties = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(filename);
			properties.load(in);
			//Initialize some variables based on the property file
			acceptorPort = Integer.valueOf(properties.getProperty(Acceptor.ACCEPTOR_PORT));
			connectorPort = Integer.valueOf(properties.getProperty(Connector.CONNECTOR_PORT));
			String serviceType = properties.getProperty(Services.SERVICE_TYPE);
			Properties services = new Properties();
			//Start the next propery file
			in.close();
			in = new FileInputStream(properties.getProperty(Services.SERVICES));
			services.load(in);
			//Ok, lets start the network manager now
			networkManager = new ServerNetworkManager(acceptorPort, connectorPort, services, serviceType);
			networkManager.start();
		} catch (FileNotFoundException e) {
			System.out.println("Error: File " + filename + " was not found ");
		} catch (IOException e) {
			System.out.println("Error: File could not be opened. Please see if it is in use.");
			return;
		}

	}
	
}

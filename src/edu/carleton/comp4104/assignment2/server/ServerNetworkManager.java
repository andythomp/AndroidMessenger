package edu.carleton.comp4104.assignment2.server;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.util.Properties;

import edu.carleton.comp4104.assignment2.common.Acceptor;
import edu.carleton.comp4104.assignment2.common.HttpAcceptor;
import edu.carleton.comp4104.assignment2.common.ObjectAcceptor;
import edu.carleton.comp4104.assignment2.common.Services;

/* 
 * The purpose of this class is to run, and wait for connections. When connections arrive,
 * it delegates them to the threadpool, where they run on their own accord.
 * The acceptor also keep's track of all connections using a hashmap.
 * I feel like this should actually be the responsibility of a Network Manager, but for now
 * we will maintain the Acceptor as the Server's network manager.
 */


public class ServerNetworkManager {

	
	
	//SOCKETS
	private Acceptor acceptor;
	private String serviceType;
	
	
	
	
	
	
	public ServerNetworkManager(int acceptorPort, int connectorPort, Properties services, String serviceType){
		this.serviceType = serviceType;
		if (this.serviceType.equals(Services.HTTP_TYPE))
			acceptor = new HttpAcceptor(acceptorPort, connectorPort, services);
		else if (this.serviceType.equals(Services.OBJECT_TYPE))
			acceptor = new ObjectAcceptor(acceptorPort, connectorPort, services);
	}
	/*
	public void accept() throws IOException{
		
		final Socket s = listener.accept();
		System.out.println("Connection received from: " + s.getInetAddress());
		Connection connection = new Connection(s);
		connection.setReactor(reactor);
		map.put(s.getInetAddress().toString(), connection);
		threadpool.execute(connection);
	}
*/
	

	public void start() {

		System.out.println("Starting acceptor on port: " + acceptor.getPort());
		System.out.println("Listening for " + serviceType + " messages.");
		new Thread(acceptor).start();
	
		// TODO Auto-generated method stub
		
	}
	
}

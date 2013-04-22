package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class ObjectAcceptor extends Acceptor {

	public ObjectAcceptor(int port, int connectorPort,Properties config){
		super(port, connectorPort, config);
	}

	//creates a worker off the socket and the acceptor, and puts it to work
	@Override
	public void getEvent(Socket s) throws IOException {
		ObjectWorker worker = new ObjectWorker(this, s);
		threadpool.execute(worker);
	}
	
}

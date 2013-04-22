package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 * This acceptor is configured for Http messages
 * It uses a Data Input Stream
 */
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class HttpAcceptor extends Acceptor {

	public HttpAcceptor(int port, int connectorPort, Properties config){
		super(port, connectorPort, config);
	}

	//creates a worker off the socket and the acceptor, and puts it to work
	@Override
	public void getEvent(Socket s) throws IOException {
		HttpWorker worker = new HttpWorker(this, s);
		threadpool.execute(worker);
		
		
	}
	
}

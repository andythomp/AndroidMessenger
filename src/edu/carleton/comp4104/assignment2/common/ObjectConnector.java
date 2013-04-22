package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectConnector extends Connector {

	public ObjectConnector(String host, int port) {
		super(host, port);
	}

	//Writes an object to an object stream then closes the socket on it's end.
	@Override
	public void sendMessage() throws IOException {
		ObjectOutputStream output = new ObjectOutputStream(peer.getOutputStream());
		output.writeObject(message);
		peer.close();
	}

}

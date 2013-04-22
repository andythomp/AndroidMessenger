package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 * This connector is designed to send Http post messages to the peer.
 * It uses Data Output Streams
 */
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

public class HttpConnector extends Connector {

	public HttpConnector(String host, int port) {
		super(host, port);
	}

	
	@Override
	public void sendMessage() throws IOException {
		//Open up a data output stream for the header
		DataOutputStream output = new DataOutputStream(peer.getOutputStream());
		String send = message.getHeader() +"\n";
		output.writeUTF(send);
		
		//Now finish off the rest with a json formatted message
		Gson gson = new Gson();
		JsonWriter writer =  new JsonWriter(new BufferedWriter(new OutputStreamWriter(peer.getOutputStream(), "UTF-8")));
		gson.toJson(message.getBody(),message.getBody().getClass(),writer);
	    writer.flush();
		peer.close();
	}

}

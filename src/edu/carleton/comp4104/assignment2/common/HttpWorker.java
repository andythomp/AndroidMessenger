package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class HttpWorker implements Runnable{

	private Acceptor acceptor;
	private Socket peer;
	
	
	
	public HttpWorker(Acceptor acceptor, Socket peer){
		this.acceptor = acceptor;
		this.peer = peer;
	}

	@Override
	public void run() {
		try{
			//Read in the header
			DataInputStream input = new DataInputStream(peer.getInputStream());
			String header = input.readUTF();
			//Now read in the body
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(peer.getInputStream(), "UTF-8")));
			HashMap<String, Object> body = gson.fromJson(reader, HashMap.class);
	
			//Now construct a message from the header and body
			Message m = new Message((String)body.get(Message.TYPE));
			m.setHeader(header);
			m.setBody(body);
			
			//Run message through a pipe with filters
			Event event = new Event();
			event = new HttpFilter().filterMessage(m,event);
			event = new TypeFilter().filterMessage(m,event);
			event = new UserFilter().filterMessage(m,event);
			event = new MessageFilter().filterMessage(m,event);
			event = new UserListFilter().filterMessage(m,event);
			//Add in some extra resources
			event.addResource(Event.HOST, peer.getInetAddress().getHostAddress());
			event.addResource(Event.ACCEPTOR, acceptor);
			
			//Finish up our socket
			peer.close();
			
			//Dispatch our fresh new event
			acceptor.getReactor().dispatch(event);
		}
		catch (Exception e){
			//If we don't understand the message, make an unknown event and send it to the reactor
			Event event = new Event();
			event.addResource(Event.TYPE, Event.UNKNOWN);
			event.addResource(Event.HOST, peer.getInetAddress());
			acceptor.getReactor().dispatch(event);
		}
		
	}
	
}

package edu.carleton.comp4104.assignment2.server;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.carleton.comp4104.assignment2.common.Acceptor;
import edu.carleton.comp4104.assignment2.common.Event;
import edu.carleton.comp4104.assignment2.common.EventHandler;
import edu.carleton.comp4104.assignment2.common.ObjectConnector;
import edu.carleton.comp4104.assignment2.common.Message;

public class ObjectIMHandler implements EventHandler {

	@Override
	public void handleEvent(Event event) throws IOException {
		//Get all the information we need off the event
				String message = (String) event.getResource(Event.MESSAGE);
				String toUser = (String) event.getResource(Event.USER);
				Acceptor acceptor = (Acceptor) event.getResource(Event.ACCEPTOR);
				String fromUser = acceptor.getUser((String)event.getResource(Event.HOST));
				
				//Log it out to the System
				Date date = new Date();
		    	SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
		    	String formattedDate = sdf.format(date);
				System.out.println(fromUser + " said " + message + " to " + toUser + " at " + formattedDate);
				
				//Format the message for reply
				message = "[" + formattedDate + "] " + message;
				
				//Send the message to the toUser
				String host = acceptor.getConnection(toUser);
				if (!host.equals(Acceptor.FAKE_HOST)){
					ObjectConnector connector = new ObjectConnector(host, acceptor.getConnectorPort());
					Message m = new Message(Message.IM);
					connector.setPayload(m);
					new Thread(connector).start();
				}
				else{
					System.out.println("Not sending a message to a fake host.");
				}

	}

}

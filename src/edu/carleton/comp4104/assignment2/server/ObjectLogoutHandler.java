package edu.carleton.comp4104.assignment2.server;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import edu.carleton.comp4104.assignment2.common.Acceptor;
import edu.carleton.comp4104.assignment2.common.Event;
import edu.carleton.comp4104.assignment2.common.EventHandler;
import edu.carleton.comp4104.assignment2.common.ObjectConnector;
import edu.carleton.comp4104.assignment2.common.Message;

public class ObjectLogoutHandler implements EventHandler{

	@Override
	public void handleEvent(Event event) throws IOException {

		try{
			//Get resources off of the event
			Acceptor acceptor = (Acceptor) event.getResource(Event.ACCEPTOR);
			String host = (String) event.getResource(Event.HOST);
			String logoutuser = (String)event.getResource(Event.USER);
			HashMap<String, String> connections = acceptor.getConnections();
			
			//If the user is already logged in.
			if (acceptor.getConnection(logoutuser) != null){
				acceptor.removeConnection(logoutuser);
				for (int i = 0; i < connections.values().size(); i++){
					String temphost = (String) connections.values().toArray()[i];
					if (!temphost.equals(host) && !temphost.equals(Acceptor.FAKE_HOST)){
						ObjectConnector connector = new ObjectConnector(temphost, acceptor.getConnectorPort());
						Message m = new Message(Message.LOGOUT);
						connector.setPayload(m);
						new Thread(connector).start();
					}
				}
			}
			//Else hes not, this is a very strange message...
			else{
				System.out.println("A user who was not logged in, just tried to log out...");
			}
			//Create a date and print it out
			Date date = new Date();
	    	SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
	    	String formattedDate = sdf.format(date);
			System.out.println(logoutuser + " is now offline at: " + formattedDate);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}

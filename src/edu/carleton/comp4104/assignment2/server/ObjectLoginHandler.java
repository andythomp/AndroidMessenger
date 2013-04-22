package edu.carleton.comp4104.assignment2.server;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import edu.carleton.comp4104.assignment2.common.Acceptor;
import edu.carleton.comp4104.assignment2.common.Event;
import edu.carleton.comp4104.assignment2.common.EventHandler;
import edu.carleton.comp4104.assignment2.common.HttpConnector;
import edu.carleton.comp4104.assignment2.common.Message;
import edu.carleton.comp4104.assignment2.common.ObjectConnector;

public class ObjectLoginHandler implements EventHandler {

	@Override
	public void handleEvent(Event event) throws IOException {
	
		try{
			//Get resources off of the event
			Acceptor acceptor = (Acceptor) event.getResource(Event.ACCEPTOR);
			String host = (String) event.getResource(Event.HOST);
			String newuser = (String)event.getResource(Event.USER);
		
			//Check that the user doesn't already exist.
			if (acceptor.addConnection((String)event.getResource(Event.USER), host)){
				synchronized (acceptor.getConnections()){
					HashMap<String, String> connections = acceptor.getConnections();
					
					//Send a new user login message to each connection
					for (int i = 0; i < connections.values().size(); i++){
						String temphost = (String) connections.values().toArray()[i];
						HttpConnector connector = new HttpConnector(temphost, acceptor.getConnectorPort());
						Message m = new Message(Message.LOGIN);
						m.setHeader(Message.POST + " " + Message.LOGIN + " " + Message.HTTP);
						
						//If this is the original connector, they need to know everyone that is online.
						if (temphost.equals(host)){
							ArrayList<String> temparray = new ArrayList<String>();
							for (int j = 0; j < connections.keySet().size(); j++){
								temparray.add((String) connections.keySet().toArray()[j]);
							}
							m.addContent(Message.USER_LIST, temparray);
							connector.setPayload(m);
							new Thread(connector).start();
						}
						else if(temphost.equals(Acceptor.FAKE_HOST)){
							//This is a fake host for demonstration purposes (easier than connecting a million different hosts.)
						}
						//Else just send them a single user name
						else{
							m.addContent(Message.USER_NAME, newuser);
							connector.setPayload(m);
							new Thread(connector).start();
						}
						
						
					}
					//Create a date, format it, and print it
					Date date = new Date();
			    	SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss a");
			    	String formattedDate = sdf.format(date);
					System.out.println(newuser + " is now online at: " + formattedDate);
				}
			}
			//Otherwise that user already exists
			else{
				//Send them a message saying that user name exists.
				ObjectConnector connector = new ObjectConnector(host, acceptor.getConnectorPort());
				Message m = new Message(Message.ERROR);
				m.addContent(Message.MESSAGE, "That user name is already in use.");
				connector.setPayload(m);
				new Thread(connector).start();
			}
			
		}
		//Something went wrong (should never happen)
		catch (Exception e){
			e.printStackTrace();
		}

	}

}

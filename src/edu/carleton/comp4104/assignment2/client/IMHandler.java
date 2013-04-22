package edu.carleton.comp4104.assignment2.client;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;

import edu.carleton.comp4104.assignment2.common.Event;
import edu.carleton.comp4104.assignment2.common.EventHandler;

public class IMHandler implements EventHandler {

	@Override
	public void handleEvent(Event event) throws IOException {
		final String forUser = (String) event.getResource(Event.USER);
		final String message = (String) event.getResource(Event.MESSAGE);
		
		//Got an instant message, add it to the list of messages for the user specified
		MainActivity.getHandle().post(new Runnable(){
			public void run(){
				MainActivity.getActivity().addMessage(forUser, message);
			}
		});
	
	}

}

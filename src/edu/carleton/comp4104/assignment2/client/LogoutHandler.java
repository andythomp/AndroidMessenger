package edu.carleton.comp4104.assignment2.client;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;

import edu.carleton.comp4104.assignment2.common.Event;
import edu.carleton.comp4104.assignment2.common.EventHandler;

public class LogoutHandler implements EventHandler{

	@Override
	public void handleEvent(Event event) throws IOException {
		final String user = (String) event.getResource(Event.USER);
		//Remove the user from the list of users
		MainActivity.getHandle().post(new Runnable(){
			public void run(){
				MainActivity.getActivity().removeUser(user);
			}
		});
	}

}

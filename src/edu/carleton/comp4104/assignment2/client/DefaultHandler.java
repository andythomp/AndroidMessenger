package edu.carleton.comp4104.assignment2.client;

/*
 * This handler responds to unknown messages,
 * if the server ever does not understand a message, it will 

 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;

import edu.carleton.comp4104.assignment2.common.Event;
import edu.carleton.comp4104.assignment2.common.EventHandler;

public class DefaultHandler implements EventHandler {

	@Override
	public void handleEvent(Event event) throws IOException {
		MainActivity.getHandle().post(new Runnable(){
			public void run(){
				MainActivity.getActivity().showMessage("Received an Unknown message");
			}
		});

	}

}

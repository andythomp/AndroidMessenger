package edu.carleton.comp4104.assignment2.server;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;

import edu.carleton.comp4104.assignment2.common.Event;
import edu.carleton.comp4104.assignment2.common.EventHandler;

public class HttpDefaultHandler implements EventHandler{

	@Override
	public void handleEvent(Event event) throws IOException {
		System.out.println("Received an Unknown Event.");
	}

}

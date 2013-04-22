package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;

public interface EventHandler {

	public void handleEvent(Event event) throws IOException;
	
}

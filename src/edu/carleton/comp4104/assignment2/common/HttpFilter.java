package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */



public class HttpFilter extends Filter{

	@Override
	public Event filterMessage(Message m, Event e) {
		//I didn't see anything to do with the actual HTTP Post that was important,
		//Given that the rest of the HTTP message is stored in the body...
		//So with that in mind, this filter does nothing basically.
		//I'm assuming the point of the HTTP Protocol is to demonstrate sending messages using
		//Json readers, writers, and data input output streams.
		m.getHeader();
		return e;
	}

}

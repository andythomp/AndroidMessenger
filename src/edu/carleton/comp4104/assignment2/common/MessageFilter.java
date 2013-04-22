package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */



public class MessageFilter extends Filter {

	@Override
	public Event filterMessage(Message m, Event e) {
		try{
			e.addResource(Event.MESSAGE, m.getBody().get(Message.MESSAGE));
		}
		catch (Exception err){
			System.out.println("No message found in event.");
		}
		return e;
	}

}

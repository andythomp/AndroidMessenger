package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */



public class UserFilter extends Filter {

	
	@Override
	public Event filterMessage(Message m, Event e) {
		try{
			e.addResource(Event.USER, m.getBody().get(Message.USER_NAME));
		}
		catch (Exception err){
			System.out.println("No user found in event.");
		}
		return e;
	}

}

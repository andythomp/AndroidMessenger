package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


public class UserListFilter extends Filter {

	
	@Override
	public Event filterMessage(Message m, Event e) {
		try{
			e.addResource(Event.USER_LIST, m.getBody().get(Message.USER_LIST));
		}
		catch (Exception err){
			System.out.println("No user list found in event.");
		}
		return e;
	}

}
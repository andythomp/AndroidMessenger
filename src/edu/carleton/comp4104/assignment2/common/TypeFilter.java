package edu.carleton.comp4104.assignment2.common;


/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


public class TypeFilter extends Filter{

	@Override
	public Event filterMessage(Message m, Event e) {
		try{
			e.addResource(Event.TYPE, m.getBody().get(Message.TYPE));
		
		}
		catch (Exception err){
			System.out.println("No type found in event.");
		}
		return e;
	}

}

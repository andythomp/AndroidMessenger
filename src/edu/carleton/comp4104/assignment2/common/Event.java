package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.util.HashMap;



public class Event{
	
	//TYPE CONSTANTS
	public static final String TYPE = "type";
	public static final String UNKNOWN = "unknown";
	
	//PARAMETER CONSTANTS
	public static final String USER = "user";
	public static final String MESSAGE = "message";
	public static final String HOST = "host";
	public static final String HANDLER = "handler";
	public static final String ACTIVITY = "activity";
	public static final String ACCEPTOR = "acceptor";
	public static final String USER_LIST = "user_list";

	
	private HashMap<String, Object> resources;
	
	
	public Event(){
		resources = new HashMap<String, Object>();
	}
	
	
	public void addResource(String key, Object resource){
		resources.put(key, resource);
	}
	
	public Object getResource(String key){
		return resources.get(key);
	}

	
}

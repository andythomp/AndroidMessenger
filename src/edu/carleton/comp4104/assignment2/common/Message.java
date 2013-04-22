package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */



import java.io.Serializable;
import java.util.HashMap;


public class Message implements Serializable {
	
	//HTTP POST CONSTANT
	public static final String POST = "POST";
	public static final String HTTP = "HTTP/1.1";
			
	
	//TYPE CONSTANTS
	public static final String TYPE = "TYPE";
	public static final String LOGIN = "LOGIN";
	public static final String LOGOUT = "LOGOUT";
	public static final String IM = "IM";
	public static final String ERROR = "ERROR";
	
	//PARAMETER CONSTANTS
	public static final String USER_NAME = "USER_NAME";
	public static final String MESSAGE = "MESSAGE";
	public static final String USER_LIST = "USER_LIST";		
	
	
	
	private static final long serialVersionUID = 4114950879755603608L;
	
	
	private String header;
	private HashMap<String, Object> body;
	

	
	public Message(String aType){
			setBody(new HashMap<String, Object>());
			addContent(TYPE, aType);
	}
	
	public void addContent(String key, Object value){
		body.put(key, value);
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public HashMap<String, Object> getBody() {
		return body;
	}

	public void setBody(HashMap<String, Object> body) {
		this.body = body;
	}
	
	public String toString(){
		return header + ":" + body.toString();
	}

}

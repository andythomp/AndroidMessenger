package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;


public class Services {
	

	//SERVICE CONSTANTS
	public static final String HTTP_TYPE = "HTTP";
	public static final String OBJECT_TYPE = "OBJECT";
	public static final String SERVICES = "SERVICES";
	public static final String SERVICE_TYPE = "SERVICES_TYPE";
	
	//Variables
	private HashMap<String, EventHandler> handlers;
	private Properties file;
	
	//Gets an event handler for a key
	public EventHandler getEventHandler(String key){
		return handlers.get(key);
	}
	

	public Services(Properties file){
		handlers = new HashMap<String, EventHandler>();
		this.file = file;
	}

	public void init(){
		//Goes over each entry in the properties file,
		//Then adds each key and class combination to the handlers
		try{
			Enumeration<Object> enumerator = file.keys();
			while (enumerator.hasMoreElements()){
				
				String key = (String) enumerator.nextElement();
				System.out.println("Adding this handler: " + key);
				handlers.put(key, createEventHandler(file, key));
			}
		} catch (Exception e){
			System.out.println("Error?");
			e.printStackTrace();
		}
	}
	//Uses reflection to find classes based on class names in the properties file
	private EventHandler createEventHandler(java.util.Properties properties, String handlerName)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException{
		String clazzName = properties.getProperty(handlerName);
		Class<?> clazz = Class.forName(clazzName);
		return (EventHandler)clazz.newInstance();
		
	}


}

package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;



public class Reactor {
		private Services services;
		
		//Constructor
		public Reactor(java.util.Properties file){
			services = new Services(file);
			loadEventHandlers();
		}
		
		//Initialize our services
		private void loadEventHandlers(){
			services.init();
		}
		
		//Takes an event, gets the eventhandler for the event type
		public void dispatch(Event event){
			if (event == null){
			}
			else{
				EventHandler e = services.getEventHandler((String)event.getResource(Event.TYPE));
				try {
					e.handleEvent(event);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			
		}
        
		
		
        public void registerHandler(String s, EventHandler m){
        	//This is now done in services.
        }
        
        public void waitForMessages(){
        	//This is if we implement a Queue
        }
        
  
        
}

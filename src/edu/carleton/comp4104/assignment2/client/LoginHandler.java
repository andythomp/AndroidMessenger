package edu.carleton.comp4104.assignment2.client;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.util.ArrayList;

import edu.carleton.comp4104.assignment2.common.Event;
import edu.carleton.comp4104.assignment2.common.EventHandler;

public class LoginHandler implements EventHandler{

	
	
	@Override
	public void handleEvent(final Event event){
		//If the user list is not null, then we made that login request
		if (event.getResource(Event.USER_LIST) != null){
			MainActivity.getHandle().post(new Runnable(){
				public void run(){
					//Mention that the login was successful
					MainActivity.getActivity().showMessage("Login Successful!");
					Object o = event.getResource(Event.USER_LIST);
					if (o instanceof ArrayList<?>){
						
						//I am checking if it's an array list, I know it's an arraylist of strings.
						//Would love to know a good way to check templates...find that out later
						@SuppressWarnings("unchecked")
						ArrayList<String> hostlist = (ArrayList<String>) o;
						//For each person in the user list, add em to our user list
						for (int i = 0; i < hostlist.size(); i++){
							MainActivity.getActivity().addUser(hostlist.get(i));
						}
					}
					else{
						MainActivity.getActivity().showMessage(o.getClass().toString());
					}
				}
			});
		}
		//We didnt make that login request so its a new user logging on
		else{
			
			if (event.getResource(Event.USER) != null){
				MainActivity.getHandle().post(new Runnable(){
					public void run(){
						MainActivity.getActivity().addUser((String)event.getResource(Event.USER));
						MainActivity.getActivity().showMessage("New User!");
					}
				});
			}
		}
		
	}

}

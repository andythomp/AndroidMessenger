package edu.carleton.comp4104.assignment2.client;

import java.util.ArrayList;

public class User {
	
	private String username;
	private boolean selected;
	private ArrayList<String> messages;
	public User(String username){
		setUsername(username);
		messages = new ArrayList<String>();
		setSelected(false);
	}
	
	public void addMessage(String message){
		messages.add(message);
	}
	
	public String getMessages(){
		String temp = "Talking to: " + username + "\n";
		for (int i = 0; i < messages.size(); i++){
			temp = temp + messages.get(i) + "\n";
		}
		return temp;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	public String toString(){
		return username;
	}

}

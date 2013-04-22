package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

public class ObjectWorker implements Runnable{
	
	private Acceptor acceptor;
	private Socket peer;
	
	
	
	public ObjectWorker(Acceptor acceptor, Socket peer){
		this.acceptor = acceptor;
		this.peer = peer;
	}

	@Override
	public void run() {
		ObjectInputStream input;
		try {
			
			//System.out.println("Got an object message!");
			
			input = new ObjectInputStream(peer.getInputStream());
			Object o = new Object();
			Message m;
			try {
				//Read in the object
				o = input.readObject();
			} catch (ClassNotFoundException e) {
				//We got an object that isnt even an object so, return a default event
				System.out.println("Don't know what class that is.");
			}
			//Check to make sure this is indeed a message
			if (o.getClass().equals(Message.class)){
				m = (Message) o;
				System.out.println(m);
				//Run our message through a pipe and filter
				Event event = new Event();
				event = new TypeFilter().filterMessage(m,event);
				event = new UserFilter().filterMessage(m,event);
				event = new MessageFilter().filterMessage(m,event);
				event = new UserListFilter().filterMessage(m,event);
				
				//Add some extra resources to the event
				event.addResource(Event.HOST, peer.getInetAddress().getHostAddress());
				event.addResource(Event.ACCEPTOR, acceptor);
				//Close up the streams and sockets
				input.close();
				//Dispatch the event
				acceptor.getReactor().dispatch(event);
			}
			else{
				//Create an unknown event and send that  to the reactor.
				Event event = new Event();
				event.addResource(Event.TYPE, Event.UNKNOWN);
				event.addResource(Event.HOST, peer.getInetAddress());
				acceptor.getReactor().dispatch(event);
			}
		} catch (StreamCorruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
		
		
	}

}

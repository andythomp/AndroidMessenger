package edu.carleton.comp4104.assignment2.common;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class Acceptor implements Runnable {

	//CONSTANTS
	public final static int MAXPOOLSIZE = 10;
	public final static int LIFESPAN = 10;
	public static final String ACCEPTOR_PORT = "ACCEPTOR_PORT";
	
	//THREAD POOL VARIABLES
	public final static ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(5);
	public static final String FAKE_HOST = "fakehost";
	protected ThreadPoolExecutor threadpool;
	private int poolSize;
	
	private HashMap<String, String> connections; //Should be UserName / IPAddress
	private ServerSocket listener;
	protected Reactor reactor;
	private Properties config;
	private int port;
	private int connectorPort;
	
	//Gets a list of all known connections
	public synchronized HashMap<String, String> getConnections(){
		return connections;
	}
	
	//Removes selected username from the list of known connections
	public synchronized void removeConnection(String username){
		connections.remove(username);
	}
	
	//Gets the host tied to the username given
	public synchronized String getConnection(String username){
		if (connections.containsKey(username)){
			return connections.get(username);
		}
		return null;
	}
	
	//Adds a connection to the list of known connections
	public synchronized boolean addConnection(String username, String host){
		if (connections.containsKey(username)){
			return false;
		}
		connections.put(username, host);
		return true;
	}
	
	//Gets the connectorPort
	public int getConnectorPort(){
		return connectorPort;
	}
	
	//Gets the username tied to the host address given
	public String getUser(String host){
		String user = "Unknown";
		
		for (int i = 0; i < connections.keySet().size(); i++){
			if (connections.values().toArray()[i].equals(host)){
				user = (String) connections.keySet().toArray()[i];
			}
		}
		return user;
	}
	
	//This is just to test out things
	public void addFakeHosts(){
		connections.put("Jim", (String) FAKE_HOST);
		connections.put("Frank", (String) FAKE_HOST);
		connections.put("Billy", (String) FAKE_HOST);
		connections.put("Bob", (String) FAKE_HOST);
		connections.put("Tina", (String) FAKE_HOST);
		connections.put("Cindy", (String) FAKE_HOST);
	}
	
	//CONSTRUCTOR
	public Acceptor(int port, int connectorPort, Properties config){
		this.port = port;
		this.config = config;
		this.connectorPort = connectorPort;
		this.connections = new HashMap<String, String>();
		addFakeHosts();
		init();
	}
	//CALLED FROM CONSTRUCTOR
	private void init(){
		poolSize = MAXPOOLSIZE;
		threadpool = new ThreadPoolExecutor(poolSize, MAXPOOLSIZE, LIFESPAN, TimeUnit.SECONDS, queue);
		try {
			listener = new ServerSocket(port);
			reactor = new Reactor(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Reactor getReactor(){
		return reactor;
	}
	
	//Gets the acceptor port
	public int getPort(){
		return listener.getLocalPort();
	}
	
	public void accept() throws IOException{
		Socket s = listener.accept();
		getEvent(s);
	}
	
	//Must be overridden by which ever kind of acceptor you make
	public abstract void getEvent(Socket s) throws IOException;
	public void run(){
		while(true){
			try {
				System.out.println("Listener listening on port: " + listener.getLocalPort());
				System.out.println("Listener accepting at address: " + InetAddress.getLocalHost());
				accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

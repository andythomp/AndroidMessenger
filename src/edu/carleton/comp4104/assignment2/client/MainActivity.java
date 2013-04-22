package edu.carleton.comp4104.assignment2.client;

/*
 * Andrew Thompson 	SN: 100745521
 * Roger Cheung 	SN: 100841823
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.carleton.comp4104.assignment2.common.Services;

public class MainActivity extends Activity {

	
	 //MENU CONSTANTS
    private static final int MAIN_MENU_ID = 1;
    private static final int LOGIN_ID = Menu.FIRST;
    private static final int LOGOUT_ID = Menu.FIRST+1;
    
	//GUI CONSTANTS
	public static final String DEFAULT_FILE_NAME = "client.cfg";
	
	//GUI VARIABLE
	private Button sendmessagebutton;
	private ListView userlist;
	private User selected;
	private ArrayAdapter<User> adapter;
	private TextView messages;
	private static MainActivity guiactivity;
	
	private TextView currentuser;
	private EditText messagefield;
	private ProgressDialog progress;
	private static Handler guihandle;
	
	//PROPERTY VARIABLES
	private Properties properties;
	private Resources resources;
	private AssetManager assetManager;
	private String messageTypeFileName;
	
	//NETWORK VARIABLES
	private String clientname;
	private ClientNetworkManager netManager;
	
	
	
	public static Handler getHandle(){
		return guihandle;
	}
	
	public static  MainActivity getActivity(){
		return guiactivity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 * If you hit the back button on this app, it will terminate the app.
	 * This is primarily because it is much easier to terminate the app if you need to restart it.
	 * 
	 */
	
	@Override
	public void onBackPressed() {
		if (netManager != null){
			netManager.sendLogoutMessage();
		}
		try {
			//Give it a chance to send the message
			//I know this isn't proper but I felt it a minor short cut
			//I promise I won't do this in the real world.
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    System.exit(0);
	}
	
	//this is just a simple function to make logging to my client tag a bit faster.
	public static void logClient(String s){
		if (s != null){
			if (s.length() > 0){
				Log.i("ClientTag", s);
			}
		}
	}	
	
	/*
	 * This loads all the GUI references inside the client.
	 */
	public void loadGUIReferences(){
        sendmessagebutton = (Button) findViewById(R.id.sendbutton);
        currentuser = (TextView) findViewById(R.id.username);
        messages = (TextView) findViewById(R.id.messages);
        messagefield = (EditText) findViewById(R.id.messagefield);
        userlist = (ListView) findViewById(R.id.userlist);
        
	}
	
	public void loadResources(){
		resources = this.getResources();
		assetManager = resources.getAssets();
		guihandle = new Handler();
	}
	
	public void initNetwork(){
		netManager = new ClientNetworkManager(guihandle, this, properties);
	    new Thread(netManager).start();
	}
	
	/*
	 * This function is responsible for the prompt at onCreate that
	 * gets all the user info.
	 */
	public String getClientName(){
		return clientname;
	}
	
	public void getUserInfo(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

    	alert.setTitle("Please enter User Name:");

    	// Set an EditText view to get user input 
    	final EditText input = new EditText(this);
    	alert.setView(input);

    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    	  Editable value = input.getText();
    	  if (!value.toString().equals("")){
    	  		clientname = value.toString();
    	  		setCurrentUserName(clientname);
    	  }
    	  initNetwork();
    	  }
    	});

    	alert.setCancelable(false);
    	alert.show();
	}
	
	/*
	 * This function is responsible for the prompt at onCreate that
	 * gets the cfg file
	 */
	public void getConfigFile(){
		//Create a prompt for a file name
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	alert.setTitle("Please Enter CFG File Name");

    	// Set an EditText view to get user input 
    	final EditText input = new EditText(this);
    	input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    	input.setText(DEFAULT_FILE_NAME);
    	alert.setView(input);

    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    	  Editable value = input.getText();
    	  	String filename = value.toString();
    	  	try {
			    InputStream inputStream = assetManager.open(filename);
			    properties = new Properties();
			    properties.load(inputStream);
			    messageTypeFileName = properties.getProperty(Services.SERVICES);
			    
			    //This line will just check to see if the services file exists.
			    //If it doesn't, we'll display an error and stop the user from continuing
			    try{
			    	inputStream = assetManager.open(messageTypeFileName);
			    }
			    catch (IOException e){
			    	showMessage("Services file: " + messageTypeFileName + " defined in: " + filename + " does not exist.");
			    	logClient("Services file: " + messageTypeFileName + " defined in: " + filename + " does not exist.");
			    }
			    
			    
			    getUserInfo();
			} catch (IOException e) {
			    showMessage("Config file not found");
			    getConfigFile();
			    e.printStackTrace();
			}
    	  }
    	});

    	alert.setCancelable(false);
    	alert.show();
	}
	
	//Sets the current user field
	public void setCurrentUserName(String username){
		currentuser.setText("User: " + username);
	}
	
	
	
	/*MAIN FUNCTION
	 * Program Starts Here!
	 * onCreate()
	 */
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {  	
    	//Initialize the window
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	guiactivity = this;
    	//Load in GUI References
    	loadGUIReferences();
    	
    	//Load Assets and Resources
    	loadResources();
    	
    	//Set User name
    	clientname = "User#" +  Math.round((Math.random() * 10000));
    	setCurrentUserName(clientname);
    	
    	//Prompt for the config file.
		getConfigFile();
		
        //Run some miscelaneous initialization stuff
        sendmessagebutton.setEnabled(false);
        selected = null;
        
        
        
        //Initialize the user list and adapter
        adapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1);
        userlist.setAdapter(adapter);
        userlist.setOnItemClickListener(new OnItemClickListener()
        {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectUser((int)arg3);
			}
       });
       
    }
 
 
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     * This function is primarily responsible for registering the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	    menu.add(MAIN_MENU_ID, LOGIN_ID, LOGIN_ID, "Login");
	    menu.add(MAIN_MENU_ID, LOGOUT_ID, LOGOUT_ID, "Logout");
	    return super.onCreateOptionsMenu(menu); 
    }
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     * This function is where the magic happens when you select an item from the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
	
	case LOGIN_ID:
		netManager.sendLoginMessage();
		break;
	case LOGOUT_ID:
		netManager.sendLogoutMessage();
		break;
	default:
	    break;
	
	       }
	    return super.onOptionsItemSelected(item);
	}
    
    /*
     * This function is meant to be called from a handler.
     * It compresses code, and shows a "connecting" dialog
     * 
     */
    public void displayProgress(){
    	progress = new ProgressDialog(this);
    	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	progress.setMessage("Connecting...");
		progress.setCancelable(false);
		progress.show();	
    }
    
    /*
     * This function needs to be called after displayProgress is called.
     */
    public void hideProgress(){
    	progress.dismiss();
    	
    }
    
    /*
     * showMessage shows a string as a toast prompt.
     */
    
    public void showMessage(String message){
    	Toast.makeText(guiactivity, message , Toast.LENGTH_SHORT).show();
    }
    
    /*
     * selectUser takes a position, and sets the selected user to user in the adapter at that position.
     * Called when someone touches or selects a user from the userlist
     */
    private void selectUser(int pos){
    	try{
	    	selected = adapter.getItem(pos);
	    	messages.setText(selected.getMessages());
	    	sendmessagebutton.setEnabled(true);
    	}
    	catch (Exception e){
    		showMessage("Unable to select user at position: " + pos);
    	}
    }
    
    /*
     * This removes a user from the userlist,
     * Called when a user disconnects from the server.
     */
    public void removeUser(String username){
    	//Cycle through all the users in the adapter and see if any of them have the same user name.
    	try{
	    	User user = getUser(username);
	    	if (user.equals(selected)){
	    		selected = null;
	    		sendmessagebutton.setEnabled(false);
	    	}
    	}
    	catch(Exception e){
    		showMessage("Message received for unknown user: " + username);
    	}
    }
    
    /*
     * This functions adds a new user to the userlist
     */
    public void addUser(String user){
    	if (!user.equals(clientname))
    		adapter.add(new User(user));
    }
    
    /*
     * This functions gets a user from the adapter / userlist
     */
    private User getUser(String user) throws NullPointerException{
    	for (int i = 0; i < adapter.getCount(); i++){
    		if (adapter.getItem(i).toString().equals(user)){
    			return adapter.getItem(i);
    		}
    	}
    	return null;
    }
    
    /*
     * This function adds a message to the given user's message list.
     * This function is called when a new message arrives at the client.
     */
    public void addMessage(String username, String message){

    	try{
	    	User user = getUser(username);
	    	user.addMessage(message);
	    	if (user.equals(selected)){
	    		messages.setText(user.getMessages());
	    	}
    	}
    	catch(NullPointerException e){
    		showMessage("Message received for unknown user: " + username);
    	}
    	
    	
    	
    }
    

    //This function will take what ever is in the send field, and send that string to the server in message form.
    public void sendMessage(View view){
    	String message = messagefield.getText().toString();
    	addMessage(selected.getUsername(), "You: " + message);
    	netManager.sendPostMessage(message, selected.getUsername());
    	messagefield.setText("");
    }
}

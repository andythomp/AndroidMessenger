Android Messenger Client and Server

Server is a java application.

Project I worked on earlier, still shows some of my older poorer programming practices (improper comments across most classes, should be updated to double stars to work with compiler). Ended up having a few glitches since the android AVD wouldn't communicate with our test device.

Supports a reactor pattern for handling messages, object or http (though the desired message type must be chosen at startup). Also supports a connector pattern, there is no persistent connection from the clients to the server. Is able to handle Instant messages, logout messages, login messages, and error messages from the server though once again, the program had some flaws.

Fork at your own risk.

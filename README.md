# welcome to BOYA!
 ![boya_logo](https://github.com/BarelHeby/ADP2_EX3/assets/107642301/3a4ee655-8233-454e-b96b-cb50fb741205)

This project will be a Android application named Boya, that will work as social media for sharing messages with a server that suppor also web users.

The application will work under the server and every user who want to join it will need to go to http://SERVER_IP:2500/ (where SERVER_IP is the ip of the computer that running the server and the android device need to be connected with this computer via router) and then the server will give him the login page.

![8289c4d8-1ca4-475c-9fb7-12323792a1ae](https://github.com/BarelHeby/ADP2_EX3/assets/107642301/ef29de51-a3ef-46ff-be75-92169423a6ab)

The server supports multiusers and Realtime messages by using socket.io. To communicate with other user you have to verify that you both are connected to the same server. The server will store all the data on mongo dB-server online such that all users will be synchronized with the same DB.

In the application the user can register to the app and then to login and start chatting with other users that logged in.

The app's homepage is the login page, since the user need to register first, click on the "click here" under the sign in button (Not registered? Click Here to register), then you will be moved to the register page.

![0388dd6c-4992-4dd2-b0e7-c40366d54f73](https://github.com/BarelHeby/ADP2_EX3/assets/107642301/6218a1c2-a1a3-40e1-9460-1a9999490cd8)

To register the app, the user need to choose a username and difficult password that will be longer than 7 characters, and will contain at least one capital letter, lowercase letter and special symbol and will not contain '#'.

After well registration the user will be moved to the login page, and after logging he will be moved to the chats page.

The app has default user (username: "Boya") to demonstrate the use of the app without registering with 2 users.

To start a new chat with other signed in users:

type the otyer username and click on the add button

click to open the chat.

To log out click on the setting icon and then click the log out button.

in the setting you can:

![f91fed2c-5b73-465b-a441-335c30ee05c7](https://github.com/BarelHeby/ADP2_EX3/assets/107642301/5b4ea2e7-1585-4f8f-99f5-afaf954ae957)

- update the ip of the server
- change the app to dark/light mode
- allow notification
- get the app info
  
How to run:

- run the server by get into the server folder in the terminal and then run:
node app.js
- and then run the android app in your device or emulator
- go to the app setting and type under the "Update server address" http://SERVER_IP (to get the server ip go to the server terminal and type ipconfig/ifconfig
- click on the update button
- now if you have signed in user log in else register and then log in
- you now chat with other android or web user
- enjoy!

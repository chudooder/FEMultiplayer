package net.fe.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import net.fe.FEMultiplayer;
import net.fe.network.message.ClientInit;
import net.fe.network.message.JoinLobby;

public class Client {
	
	Socket serverSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	Thread serverIn;
	boolean open = true;
	byte id;
	public volatile ArrayList<Message> messages;
	
	public Client() {
		messages = new ArrayList<Message>();
		try {
			System.out.println("CLIENT: Connecting to server at port 21255...");
			serverSocket = new Socket(
					java.net.InetAddress.getLocalHost().getHostName(), 
					21255);
			System.out.println("CLIENT: Successfully connected!");
			out = new ObjectOutputStream(serverSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(serverSocket.getInputStream());
			System.out.println("CLIENT: I/O streams initialized");
			serverIn = new Thread() {
				public void run() {
					try {
						Message message;
						while((message = (Message)in.readObject()) != null) {
							processInput(message);
						}
						in.close();
						out.close();
						serverSocket.close();
					} catch (IOException e) {
						System.out.println("CLIENT: EXIT");
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		serverIn.start();
	}
	
	private void processInput(Message message) {
		if(message instanceof ClientInit) {
			id = ((ClientInit)message).clientID;
			System.out.println("CLIENT: Recieved ID "+id+" from server");
			// Send a join lobby request
			sendMessage(new JoinLobby(id, FEMultiplayer.getLocalPlayer().getName()));
		}
	}
	
	public ArrayList<Message> getMessages() {
		return messages;
	}
	
	public void close() {
		try {
			in.close();
			out.close();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Message message) {
		try {
			out.writeObject(message);
			System.out.println("CLIENT: Sent message ["+message.toString()+"]");
		} catch (IOException e) {
			System.err.println("CLIENT Unable to send message!");
		}
	}
}

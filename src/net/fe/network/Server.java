package net.fe.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import chu.engine.Stage;
import net.fe.Player;

public class Server {
	ServerSocket serverSocket;
	boolean closeRequested = false;
	volatile ArrayList<ServerListener> clients;
	public volatile ArrayList<Message> messages;
	byte counter = 1;
	
	public Server() {
		messages = new ArrayList<Message>();
		clients = new ArrayList<ServerListener>();
	}
	
	public void start(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("SERVER: Waiting for connections...");
			while(!closeRequested) {
				Socket connectSocket = serverSocket.accept();
				System.out.println("SERVER: Connection #"+counter+" accepted!");
				ServerListener listener = new ServerListener(this, connectSocket);
				clients.add(listener);
				listener.start();
				counter++;
			}
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a message to all clients
	 * @param line
	 */
	public void broadcastMessage(Message message) {
		for(ServerListener out : clients) {
			out.sendMessage(message);
		}
	}
	
	/**
	 * Sends a message only to the given client
	 * @param client
	 * @param line
	 */
	public void sendMessage(ServerListener client, Message message) {
		client.sendMessage(message);
	}
	
	public byte getCount() {
		return counter;
	}

	public HashMap<Integer, Player> getPlayers() {
		Stage stage = FEServer.getCurrentStage();
		if(stage instanceof ServerLobby) {
			return ((ServerLobby)stage).players;
		} else {
			return null;
		}
	}
}

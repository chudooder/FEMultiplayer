package chu.engine.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * The one game to rule them all. Not really.
 * When the host starts a game, it automatically
 * starts a server linked to the host's copy of
 * the game. All commands from all clients,
 * even from the host, is run through this
 * Server.
 * 
 * All clients (including the host) then receive
 * the inputs from the server, and replicate it 
 * on their copy of the game. This ensures that
 * all versions of the game are in the same state.
 * 
 * During a game round, the server will send out two
 * types of messages:
 * - Input messages Inputs the most recent mercs.
 * 			Once every frame.
 * - Update messages: Absolute positions for the mercs.
 * 			This is in case some packets get dropped, and
 * 			prevents desync. Once per 7 frames.
 * - End-of-round update: At the end of a round, the server
 * 			sends out the complete input record for the 
 * 			previous round. If any frames were dropped, this
 * 			should resolve desync issues.
 * 
 * To summarize:
 * - One Server, running on host computer
 * - Two Clients, running on host and remote,
 * 		connected to the server.
 * - Two TimeLapses, running on host and remote,
 * 		linked to their respective clients 
 */
public class Server {
	ServerSocket serverSocket;
	boolean closeRequested = false;
	volatile ArrayList<ServerListener> clients; 
	byte counter = 0;
	
	public Server(int port) {
		clients = new ArrayList<ServerListener>();
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("SERVER: WAITING FOR CONNECTION");
			while(!closeRequested) {
				Socket connectSocket = serverSocket.accept();
				System.out.println("SERVER: CONNECTION ACCEPTED");
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
	public void sendMessage(Message message) {
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
}

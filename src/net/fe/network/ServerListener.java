package net.fe.network;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import net.fe.lobbystage.LobbyStage;
import net.fe.network.message.ClientInit;
import net.fe.network.message.CommandMessage;
import net.fe.network.message.JoinTeam;
import net.fe.network.message.PartyMessage;
import net.fe.network.message.QuitMessage;
import net.fe.network.message.ReadyMessage;

public class ServerListener extends Thread {
	
	private Socket socket = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Server main;
	private boolean clientQuit;
	final byte[] begin = new byte[]{0x42,0x45,0x47,0x49,0x4e};
	
	public ServerListener(Server main, Socket socket) {
		super("Listener "+main.getCount());
		try {
			this.socket = socket;
			this.main = main;
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			System.out.println("LISTENER: I/O streams initialized");
			sendMessage(new ClientInit((byte) 0, main.getCount(), main.getSession()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			System.out.println("LISTENER: Start");
			Message message;
			clientQuit = false;
			while(!clientQuit) {
				message = (Message) in.readObject();
				main.log.logMessage(message, false);
				processInput(message);
			}
			System.out.println("LISTENER: Exit");
			main.clients.remove(this);
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			System.err.println("Exception occurred, writing to logs...");
			e.printStackTrace();
			try{
				File errLog = new File("error_log_server_listener" + System.currentTimeMillis()%100000000 + ".log");
				PrintWriter pw = new PrintWriter(errLog);
				e.printStackTrace(pw);
				pw.close();
			}catch (IOException e2){
				e2.printStackTrace();
			}
		} finally {
			main.clients.remove(this);
		}
	}
	
	public void processInput(Message message) {
		if(message instanceof QuitMessage) {
			clientQuit = true;
		}
		else if(message instanceof JoinTeam || message instanceof ReadyMessage) {
			// Prevent late-joining players from switching teams or readying up
			if(!(FEServer.getCurrentStage() instanceof LobbyStage))
				return;
		}
		else if(message instanceof CommandMessage) {
			// If the unit attacked, we need to generate battle results
			main.messages.add(message);
			return;	// Wait for the server's overworld stage to get results
		}
		else if(message instanceof PartyMessage) {
			main.messages.add(message);
			return;
		}
		main.broadcastMessage(message);
		main.messages.add(message);
	}
	
	public void sendMessage(Message message) {
		try {
			out.writeObject(message);
			out.flush();
//			System.out.println("SERVER sent message: [" + message.toString() + "]");
		} catch (IOException e) {
			System.err.println("SERVER Unable to send message!");
		}
	}

}

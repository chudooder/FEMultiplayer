package chu.engine.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerListener extends Thread {
	
	private Socket socket = null;
	OutputStream out;
	InputStream in;
	Server main;
	static final byte INPUT = 0;
	static final byte PRINT = 1;
	static final byte RESET = 2;
	static final byte START = 3;
	static final byte INIT = 4;
	
	public ServerListener(Server main, Socket socket) {
		super("Listener");
		try {
			this.socket = socket;
			this.main = main;
			in = socket.getInputStream();
			out = socket.getOutputStream();
			sendMessage(new byte[] {0, INIT, main.getCount()});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			System.out.println("SERVER: START");
			byte[] line = new byte[16];
			while(in.read(line) != -1) {
				processInput(line);
			}
			
			System.out.println("SERVER: EXIT");
			main.clients.remove(this);
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void processInput(byte[] line) {
		if(line.length > 0) {
			int player = line[0];
			switch(line[1]) {
			case INPUT :
				main.sendMessage(line);
				break;
			case PRINT :
				System.out.println(String.valueOf(line));
				break;
			case RESET :
				main.sendMessage(line);
				break;
			case START :
				main.sendMessage(line);
				break;
			}
		}
	}
	
	public void sendMessage(byte[] line) {
		try {
			out.write(line);
		} catch (IOException e) {
			System.err.println("SERVER Unable to send message!");
		}
	}

}

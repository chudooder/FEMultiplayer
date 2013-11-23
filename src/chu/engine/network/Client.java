package chu.engine.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Client {
	
	Socket serverSocket;
	OutputStream out;
	InputStream in;
	boolean open = true;
	public volatile ArrayList<byte[]> messages;
	
	public Client() {
		messages = new ArrayList<byte[]>();
		try {
			System.out.println("CLIENT: CONNECTING TO SERVER");
			serverSocket = new Socket(
					java.net.InetAddress.getLocalHost().getHostName(), 
					21255);
			System.out.println("CLIENT: SUCCESSFULLY CONNECTED");
			in = serverSocket.getInputStream();
			out = serverSocket.getOutputStream();
			
			Thread serverIn = new Thread() {
				public void run() {
					try {
						byte[] line = new byte[16];
						while(in.read(line) != -1) {
							processInput(line);
						}
					
						in.close();
						out.close();
						serverSocket.close();
					} catch (IOException e) {
						System.out.println("CLIENT: EXIT");
					}
				}
			};
			
			serverIn.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void processInput(byte[] line) {
		if(line.length > 0)
			messages.add(line);
	}
	
	public ArrayList<byte[]> getMessages() {
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
	
	public void sendMessage(byte[] message) {
		try {
			out.write(message);
		} catch (IOException e) {
			System.err.println("CLIENT Unable to send message!");
		}
	}
}

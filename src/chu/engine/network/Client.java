package chu.engine.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
	
	Socket serverSocket;
	OutputStream out;
	InputStream in;
	boolean open = true;
	public volatile ArrayList<Message> messages;
	
	public static void main(String[] args) {
		new Client();
	}
	
	public Client() {
		messages = new ArrayList<Message>();
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
						byte[] header = new byte[8];
						while(in.read(header) != -1) {
							// MSGBEGINx5 ORIGIN TYPE LENGTH
							byte[] begin = new byte[]{0x42,0x45,0x47,0x49,0x4e};
							for(int i=0; i<5; i++) {
								if(!(header[i] == begin[i])) {
									continue;
								}
							}
							byte origin = header[5];
							byte type = header[6];
							byte length = header[7];
							byte[] data = new byte[length];
							in.read(data);
							Message message = new Message(origin, type, data);
							processInput(type, message);
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
	
	private void processInput(byte type, Message message) {
		if(message.length > 0) {
			messages.add(message);
			System.out.println("Got a message!");
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
	
	public void sendMessage(byte[] message) {
		try {
			out.write(message);
		} catch (IOException e) {
			System.err.println("CLIENT Unable to send message!");
		}
	}
}

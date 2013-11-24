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
	final byte[] begin = new byte[]{0x42,0x45,0x47,0x49,0x4e};
	
	public ServerListener(Server main, Socket socket) {
		super("Listener");
		try {
			this.socket = socket;
			this.main = main;
			in = socket.getInputStream();
			out = socket.getOutputStream();
			sendMessage(new Message(0, Message.INIT,
					new byte[]{main.getCount()}));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			System.out.println("SERVER: START");
			byte[] header = new byte[8];
			while(in.read(header) != -1) {
				// MSGBEGINx5 ORIGIN TYPE LENGTH
				for(int i=0; i<5; i++) {
					if(!(header[i] == begin[i])) {
						continue;
					}
				}
				byte origin = header[5];
				byte type = header[6];
				byte length = header[7];
				byte[] message = new byte[length];
				in.read(message);
				processInput(type, message);
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
	
	public void processInput(byte type, byte[] message) {
		
	}
	
	public void sendMessage(Message message) {
		try {
			out.write(message.getBytes());
		} catch (IOException e) {
			System.err.println("SERVER Unable to send message!");
		}
	}

}

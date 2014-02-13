package net.fe.network;

import java.io.*;
import java.util.ArrayList;

public class ServerLog {
	public ArrayList<String> messageLog;

	public ServerLog() {
		File logDir = new File("logs");
		if(!logDir.isDirectory()) logDir.mkdir();
		messageLog = new ArrayList<String>();
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				spitLog();
			}
		});
	}

	public void logMessage(Message m, boolean sent) {
		messageLog.add((sent?"[SEND]":"[RCVE]") + m.toString());
	}

	public void spitLog() {
		if(messageLog.size() == 0) return;
		System.out.println("Spitting logs...");
		try {
			PrintWriter p = new PrintWriter(new File("logs/server_log"
					+ System.currentTimeMillis() % 100000000 + ".log"));
			for(String message: messageLog){
				System.out.println(message);
				p.println(message.trim());
			}
			p.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

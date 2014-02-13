package net.fe.network;

import java.io.*;
import java.util.ArrayList;

import net.fe.RNG;

public class ServerLog {
	private ArrayList<String> messageLog;

	public ServerLog() {
		File logDir = new File("logs");
		if(!logDir.isDirectory()) logDir.mkdir();
		messageLog = new ArrayList<String>();
		messageLog.add("[SEED] " + RNG.seed);
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				spitLog();
			}
		});
	}

	public void logMessage(Message m, boolean sent) {
		log((sent?"[SEND]":"[RCVE]") + m.toString());
	}
	
	public void log(String s){
		messageLog.add(s);
		System.out.println(s);
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

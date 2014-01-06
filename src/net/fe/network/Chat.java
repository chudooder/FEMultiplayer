package net.fe.network;

import java.util.ArrayList;
import java.util.List;

import net.fe.Player;

/**
 * Manages list of chat messages
 * @author Shawn
 *
 */
public class Chat {
	
	private ArrayList<Chatlog> chatMessages;

	public Chat() {
		chatMessages = new ArrayList<Chatlog>();
	}
	
	public void add(Player p, String line) {
		chatMessages.add(new Chatlog(p, line));
	}
	
	public String get(int i) {
		return chatMessages.get(i).toString();
	}
	
	public List<String> getLast(int i) {
		List<String> list = new ArrayList<String>();
		for(int j = chatMessages.size()-i; j < chatMessages.size(); j++) {
			if(j < 0) {
				list.add("");
			} else {
				list.add(chatMessages.get(j).toString());
			}
		}
		return list;
	}
	
	private class Chatlog {
		Player player;
		String line;
		public Chatlog(Player p, String s) {
			player = p;
			line = s;
		}
		
		public String toString() {
			if(player == null) {
				return line;
			} else {
				return player.getName() + ": " + line;
			}
		}
	}
	
}

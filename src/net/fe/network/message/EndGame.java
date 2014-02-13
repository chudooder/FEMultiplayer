package net.fe.network.message;

import net.fe.network.Message;

public class EndGame extends Message {

	public int winner;
	/**
	 * 
	 */
	private static final long serialVersionUID = 654326008504474145L;
	
	public EndGame(byte origin, int winner) {
		super(origin);
		this.winner = winner;
	}
	
	public String toString(){
		return "ENDGAME:: winner = " + winner;
	}

}

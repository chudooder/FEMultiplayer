package net.fe.network;

import java.io.Serializable;

public abstract class Message implements Serializable {
	
	private static final long serialVersionUID = 8838417404744137405L;
	public byte origin;
	public Message() {
		
	}
	public Message(byte origin) {
		this.origin = origin;
	}
	public String toString(){
		String classname = getClass().getSimpleName().toUpperCase();
		classname.replaceAll("MESSAGE", "");
		return origin + " " + getClass().getSimpleName().toUpperCase() + "::";
	}
}

package net.fe.editor;

import java.io.Serializable;

import org.newdawn.slick.Color;

public class SpawnPoint implements Serializable {
	
	private static final long serialVersionUID = 8955139984944016201L;
	public int x;
	public int y;
	public Color team;
	
	public SpawnPoint(int x, int y, Color team) {
		this.x = x;
		this.y = y;
		this.team = team;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpawnPoint other = (SpawnPoint) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	

}

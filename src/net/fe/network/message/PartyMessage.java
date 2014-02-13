package net.fe.network.message;

import java.util.List;

import net.fe.network.Message;
import net.fe.unit.Unit;

public class PartyMessage extends Message {

	public List<Unit> teamData;
	
	public PartyMessage(List<Unit> data) {
		super();
		teamData = data;
	}
	private static final long serialVersionUID = -6657648098112938492L;
	public String toString(){
		String ans = super.toString() + "\n";
		for(Unit u: teamData){
			ans += "\t" + u.name + " Lv" + u.get("Lvl") + "\n";
		}
		return ans;
	}
	
}

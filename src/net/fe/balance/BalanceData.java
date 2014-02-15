package net.fe.balance;

import java.util.HashMap;

import net.fe.unit.Unit;

public class BalanceData {
	private Unit unit;
	public String name;
	public HashMap<String, Integer> bases;
	public HashMap<String, Integer> growths;
	
	public static final String[] ORDER = 
		{"HP", "Str", "Mag", "Skl", "Spd", "Lck", "Def", "Res"};
	
	public BalanceData(Unit u){
		unit = u;
		name = u.name;
		bases = new HashMap<String, Integer>(u.bases);
		growths = new HashMap<String, Integer>(u.growths);
	}
	
	public String exportString(){
		String ans = String.format("%-12s%-16s20\t", name, unit.noGenderName());
		for(String stat: ORDER){
			ans += bases.get(stat) + "\t";
		}
		ans += unit.get("Con") + "\t" + unit.get("Mov") + "\t\t";
		for(String stat: ORDER){
			ans += growths.get(stat) + "\t";
		}
		ans += "\t" + unit.gender + "\n";
		return ans;
	}
}

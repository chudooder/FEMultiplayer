package net.fe.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.newdawn.slick.util.ResourceLoader;

public class UnitFactory {
	private static HashMap<String, Unit> units = new HashMap<String, Unit>();
	
	static{
		Scanner in = new Scanner(ResourceLoader.getResourceAsStream("res/stats.txt"));
		while(in.hasNextLine()){
			String line = in.nextLine();
			if(line.equals("") || line.startsWith("#")) continue;
			String[] args = line.split("\\s+");
			String name = args[0];
			Class clazz = Class.createClass(args[1]);
			int lv = Integer.parseInt(args[2]);
			float hpBase = Integer.parseInt(args[3]);
			float strBase = Integer.parseInt(args[4]);
			float magBase = Integer.parseInt(args[5]);
			float sklBase = Integer.parseInt(args[6]);
			float spdBase = Integer.parseInt(args[7]);
			float lckBase = Integer.parseInt(args[8]);
			float defBase = Integer.parseInt(args[9]);
			float resBase = Integer.parseInt(args[10]);
			int con = Integer.parseInt(args[11]);
			int mov = Integer.parseInt(args[12]);
			int hpGrowth = Integer.parseInt(args[13]);
			int strGrowth = Integer.parseInt(args[14]);
			int magGrowth = Integer.parseInt(args[15]);
			int sklGrowth = Integer.parseInt(args[16]);
			int spdGrowth = Integer.parseInt(args[17]);
			int lckGrowth = Integer.parseInt(args[18]);
			int defGrowth = Integer.parseInt(args[19]);
			int resGrowth = Integer.parseInt(args[20]);
			for(int i = lv; i < 20; i++){
				hpBase += hpGrowth/100.0f;
				strBase += strGrowth/100.0f;
				magBase += magGrowth/100.0f;
				sklBase += sklGrowth/100.0f;
				spdBase += spdGrowth/100.0f;
				lckBase += lckGrowth/100.0f;
				defBase += defGrowth/100.0f;
				resBase += resGrowth/100.0f;
			}
			
			HashMap<String, Integer> bases = new HashMap<String, Integer>();
			bases.put("Lvl", 1);
			bases.put("HP", (int)hpBase);
			bases.put("Str", (int)strBase);
			bases.put("Mag", (int)magBase);
			bases.put("Skl", (int)sklBase);
			bases.put("Spd", (int)spdBase);
			bases.put("Lck", (int)lckBase);
			bases.put("Def", (int)defBase);
			bases.put("Res", (int)resBase);
			bases.put("Con", con);
			bases.put("Mov", mov);
			
			HashMap<String, Integer> growths = new HashMap<String, Integer>();
			growths.put("HP", hpGrowth);
			growths.put("Str", strGrowth);
			growths.put("Mag", magGrowth);
			growths.put("Skl", sklGrowth);
			growths.put("Spd", spdGrowth);
			growths.put("Def", defGrowth);
			growths.put("Res", resGrowth);
			growths.put("Lck", lckGrowth);
			
			if(clazz == null){
				System.err.println(line);
			}
			Unit u = new Unit(name, clazz, bases, growths);
			if(name.equals("Roy")){
				u.addToInventory(WeaponFactory.getWeapon("Sealed Sword"));
			} else if (name.equals("Lyn")){
				u.addToInventory(WeaponFactory.getWeapon("Sol Katti"));
			} else if (name.equals("Eliwood")){
				u.addToInventory(WeaponFactory.getWeapon("Durandal"));
			} else if (name.equals("Hector")){
				u.addToInventory(WeaponFactory.getWeapon("Armads"));
			} else if(name.equals("Eirika")){
				u.addToInventory(WeaponFactory.getWeapon("Sieglinde"));
			} else if(name.equals("Ephraim")){
				u.addToInventory(WeaponFactory.getWeapon("Siegmund"));
			} else if(name.equals("Marth")){
				u.addToInventory(WeaponFactory.getWeapon("Falchion"));
			} else if(name.equals("Ike")){
				u.addToInventory(WeaponFactory.getWeapon("Ragnell"));
			}
			
			units.put(name, u);
		}
		in.close();
	}
	
	public static Unit getUnit(String name){
		return units.get(name).getCopy();
	}
	
	public static ArrayList<Unit> getAllUnits() {
		ArrayList<Unit> ans = new ArrayList<Unit>();
		for(Unit u : units.values()) {
			ans.add(u.getCopy());
		}
		return ans;
	}
}

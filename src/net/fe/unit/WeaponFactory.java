package net.fe.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.newdawn.slick.util.ResourceLoader;

public class WeaponFactory {
	private static final List<String> mounted = 
			Arrays.asList("Paladin", "Valkyrie", "Falcon Knight", 
					"Ephraim", "Eirika", "Eliwood");
	private static final List<String> armored =
			Arrays.asList("Paladin", "General");
	private static HashMap<String, Weapon> weapons = new HashMap<String, Weapon>();
	
	static{
		Scanner in = new Scanner(ResourceLoader.getResourceAsStream("res/weapons.txt"));
		int id = 0;
		while(in.hasNextLine()){
			String line = in.nextLine();
			if(line.startsWith("#") || line.equals("")){
				continue;
			}
			String[] args = line.split("\\t+");
			String name = args[0];
			Weapon w = new Weapon(name);
			w.id = id++;
			w.type = Weapon.Type.valueOf(args[1].toUpperCase());
			
			
			List<Integer> range = new ArrayList<Integer>();
			String[] rangeArgs = args[2].split("-");
			if(rangeArgs.length == 1){
				range.add(Integer.parseInt(rangeArgs[0]));
			} else {
				int min = Integer.parseInt(rangeArgs[0]);
				int max = Integer.parseInt(rangeArgs[1]);
				for(int i = min; i <= max; i++){
					range.add(i);
				}
			}
			w.range = range;
			
			w.mt = Integer.parseInt(args[3]);
			w.hit = Integer.parseInt(args[4]);
			w.crit = Integer.parseInt(args[5]);
			w.setMaxUses(Integer.parseInt(args[6]));
			
			if(!args[7].equals("-")){
				w.setCost(Integer.parseInt(args[7]));
			}
			
			if(args[8].equals("Mount")){
				w.effective.addAll(mounted);
			} else if (args[8].equals("Armor")){
				w.effective.addAll(armored);
			} else if (args[8].equals("Flier")){
				w.effective.add("Falcon Knight");
			}
			
			if(!args[9].equals("-")){
				w.pref = args[9];
			}
			
			if(!args[10].equals("-")){
				String[] modArgs = args[10].split(" ");
				w.modifiers.put(modArgs[0], Integer.parseInt(modArgs[1]));
			}
			
			weapons.put(name, w);
		}
		in.close();
	}
	
	public static Weapon getWeapon(String name){
		return weapons.get(name).getCopy();
	}
	
	public static Iterable<Weapon> getAllWeapons(){
		return weapons.values();
	}
}

package net.fe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fe.fightStage.FightUnit;
import net.fe.unit.Unit;

import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.Texture;

import chu.engine.anim.ShaderArgs;

public class PaletteSwapper {
	
	public static Map<String, List<String>> lookup;
	public static Map<String, Texture> palettes;
	
	static {
		String[] p = new String[] {"overworld", "general", "assassin", 
				"berserker", "hero", "paladin", "sagem", "sagef", "sniperm", 
				"sniperf", "sorcerer", "swordmasterm", 
				"swordmasterf", "valkyrie", "falconknight"};
		palettes = new HashMap<String, Texture>();
		lookup = new HashMap<String, List<String>>();
		lookup.put("general", Arrays.asList(new String[] {"Wallace", "Oswin", "Amelia", "Gilliam"}));
		lookup.put("assassin", Arrays.asList(new String[] {"Jaffar", "Matthew", "Marisa"}));
		lookup.put("berserker", Arrays.asList(new String[] {"Dart", "Ross"}));
		lookup.put("hero", Arrays.asList(new String[] {"Harken", "Garcia", "Gerik", "Raven"}));
		lookup.put("paladin", Arrays.asList(new String[] {"Cameron", "Sain", "Franz", "Forde", "Kyle", "Kent", "Lowen", "Marcus"}));
		lookup.put("sagem", Arrays.asList(new String[] {"Erk", "Lucius", "Artur"}));
		lookup.put("sagef", Arrays.asList(new String[]{"Lute"}));
		lookup.put("sniperm", Arrays.asList(new String[] {"Innes", "Wil"}));
		lookup.put("sniperf", Arrays.asList(new String[]{"Neimi", "Rebecca"}));
		lookup.put("sorcerer", Arrays.asList(new String[]{"Knoll", "Ewan", "Canas"}));
		lookup.put("swordmasterm", Arrays.asList(new String[]{"Karel", "Guy", "Joshua", "Edward"}));
		lookup.put("swordmasterf", Arrays.asList(new String[]{"NOTMIA", "Mia"}));
		lookup.put("valkyrie", Arrays.asList(new String[]{"Priscilla", "L'Arachel", "Natasha"}));
		lookup.put("falconknight", Arrays.asList(new String[]{"???", "Vanessa", "Tana", "Florina"}));
		
		for(String s : p) {
			palettes.put(s, FEResources.getTexture("palette_"+s));
		}
	}

	public static ShaderArgs setup(FightUnit u) {
		Unit unit = u.getUnit();
		ShaderArgs args = new ShaderArgs();
		if(unit.getTheClass().name.equals("Lord")) return args;
		String c = unit.functionalClassName();
		
		Texture t = palettes.get(c);
		if(t == null) return args;
		if(lookup.get(c) == null) return args;
		int offset = lookup.get(c).indexOf(unit.name);
		if(offset < 0) return args;
		args.programName = "paletteSwap";
		args.args = new float[] {t.getTextureWidth(), t.getTextureHeight(), offset, t.getImageWidth()};
		GL13.glActiveTexture(GL13.GL_TEXTURE8);
		t.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		return args;
	}
	
	public static ShaderArgs setup(Unit u) {
		ShaderArgs args = new ShaderArgs();
		int offset = u.getPartyColor().equals(Party.TEAM_BLUE) ? 0 : 1;
		if(offset == 0) return args;
		
		Texture t = palettes.get("overworld");
		args.programName = "paletteSwap";
		args.args = new float[] {t.getTextureWidth(), t.getTextureHeight(), offset, t.getImageWidth()};
		GL13.glActiveTexture(GL13.GL_TEXTURE8);
		t.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		return args;
	}
	
}

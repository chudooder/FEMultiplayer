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
	
	static {
		lookup = new HashMap<String, List<String>>();
		List<String> general = Arrays.asList(new String[] {"Wallace", "Oswin", "Amelia", "Gilliam"});
		lookup.put("general", general);
	}

	public static ShaderArgs setup(FightUnit u) {
		Unit unit = u.getUnit();
		ShaderArgs args = new ShaderArgs();
		String c = unit.getTheClass().name.toLowerCase();
		if(c.equals("lord")) return args;
		
		Texture t = FEResources.getTexture("palette_"+c);
		if(t == null) return args;

		int offset = lookup.get(c).indexOf(unit.name);
		args.programName = "paletteSwap";
		args.args = new float[] {t.getImageWidth(), t.getImageHeight(), offset};
		GL13.glActiveTexture(GL13.GL_TEXTURE8);
		t.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		return args;
	}
	
	public static ShaderArgs setup(Unit u) {
		ShaderArgs args = new ShaderArgs();
		int offset = u.getPartyColor().equals(Party.TEAM_BLUE) ? 0 : 1;
		if(offset == 0) return args;
		
		Texture t = FEResources.getTexture("palette_overworld");
		args.programName = "paletteSwap";
		args.args = new float[] {t.getImageWidth(), t.getImageHeight(), offset};
		GL13.glActiveTexture(GL13.GL_TEXTURE8);
		t.bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		return args;
	}
	
}

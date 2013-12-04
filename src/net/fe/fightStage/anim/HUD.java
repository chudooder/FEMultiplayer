package net.fe.fightStage.anim;

import net.fe.fightStage.FightStage;
import net.fe.unit.Unit;
import chu.engine.Entity;
import chu.engine.anim.Renderer;

public class HUD extends Entity {
	private Unit u1;
	private Unit u2;
	private int sign;
	private FightStage stage;
	public HUD(Unit u1, Unit u2, FightStage stage) {
		super(0, 0);
		this.u1 = u1;
		this.u2 = u2;
		sign = stage.isLeft(u1)? -1:1;
		this.stage = stage;
		
		renderDepth = FightStage.HUD_DEPTH;
	}
	
	public void render(){
		//Main status
		Renderer.drawRectangle(FightStage.CENTRAL_AXIS + sign*120, FightStage.FLOOR + 14, 
				FightStage.CENTRAL_AXIS, FightStage.FLOOR + 56, 0, FightStage.BORDER_DARK);
		Renderer.drawRectangle(FightStage.CENTRAL_AXIS + sign*119, FightStage.FLOOR + 15, 
				FightStage.CENTRAL_AXIS + sign, FightStage.FLOOR + 55, 0, FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(FightStage.CENTRAL_AXIS + sign*118, FightStage.FLOOR + 16, 
				FightStage.CENTRAL_AXIS + sign*2, FightStage.FLOOR + 54, 0, 
				u1.getPartyColor().darker(0.5f));
		
		//Weapon
		Renderer.drawRectangle(FightStage.CENTRAL_AXIS + sign*119, FightStage.FLOOR + 15, 
				FightStage.CENTRAL_AXIS + sign, FightStage.FLOOR+31, 0, FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(FightStage.CENTRAL_AXIS + sign*118, FightStage.FLOOR + 16, 
				FightStage.CENTRAL_AXIS + sign*2, FightStage.FLOOR+30, 0, FightStage.NEUTRAL);
		Renderer.drawString("default_small" , u1.getWeapon().name,
				FightStage.CENTRAL_AXIS + sign*39 - 20, FightStage.FLOOR + 17);
		
		//Attack Stats
		Renderer.drawRectangle(FightStage.CENTRAL_AXIS + sign*120, FightStage.FLOOR - 1,
				FightStage.CENTRAL_AXIS + sign*76, FightStage.FLOOR + 32, 0, FightStage.BORDER_DARK);
		Renderer.drawRectangle(FightStage.CENTRAL_AXIS + sign*119, FightStage.FLOOR ,
				FightStage.CENTRAL_AXIS + sign*77, FightStage.FLOOR + 31, 0, FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(FightStage.CENTRAL_AXIS + sign*118, FightStage.FLOOR + 1,
				FightStage.CENTRAL_AXIS + sign*78, FightStage.FLOOR + 30, 0, 
				u1.getPartyColor());
		
		String hit, crit, dmg;
		
		if(!u1.getWeapon().range.contains(stage.getRange())){
			hit = "  -";
			crit = "  -";
			dmg = "  -";
		} else {
			hit = String.format("%3d", 
					Math.min(100, Math.max(u1.hit()-u2.avoid(),0)));
			crit = String.format("%3d", 
					Math.min(100, Math.max(u1.crit()-u2.dodge(),0)));
			dmg = String.format("%3d", 
					Math.min(100, Math.max(FightStage.calculateBaseDamage(u1,u2),0)));
		}
		
		Renderer.drawString("default_small", "HIT", 
				FightStage.CENTRAL_AXIS + sign*98 - 18, FightStage.FLOOR);
		Renderer.drawString("number", hit, 
				FightStage.CENTRAL_AXIS + sign*98, FightStage.FLOOR);
		
		Renderer.drawString("default_small", "CRT", 
				FightStage.CENTRAL_AXIS + sign*98 - 18, FightStage.FLOOR + 9);
		Renderer.drawString("number", crit, 
				FightStage.CENTRAL_AXIS + sign*98, FightStage.FLOOR + 9);
		
		Renderer.drawString("default_small", "DMG", 
				FightStage.CENTRAL_AXIS + sign*98 - 18, FightStage.FLOOR + 18);
		Renderer.drawString("number", dmg, 
				FightStage.CENTRAL_AXIS + sign*98, FightStage.FLOOR + 18);
		
		//Name
		Renderer.drawRectangle(FightStage.CENTRAL_AXIS + sign*120, FightStage.FLOOR - 99, 
				FightStage.CENTRAL_AXIS + sign*63, FightStage.FLOOR - 77, 0, FightStage.BORDER_DARK);
		Renderer.drawRectangle(FightStage.CENTRAL_AXIS + sign*120, FightStage.FLOOR - 98, 
				FightStage.CENTRAL_AXIS + sign*64, FightStage.FLOOR - 78, 0, FightStage.BORDER_LIGHT);
		Renderer.drawRectangle(FightStage.CENTRAL_AXIS + sign*120, FightStage.FLOOR - 97, 
				FightStage.CENTRAL_AXIS + sign*65, FightStage.FLOOR - 79, 0, 
				u1.getPartyColor());
		Renderer.drawString("default", u1.name, 
				FightStage.CENTRAL_AXIS + sign*94 - 16, 9);
	}
}

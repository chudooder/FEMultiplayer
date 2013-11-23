package net.fe;

import java.util.ArrayList;
import java.util.HashMap;

import chu.engine.Entity;
import chu.engine.Stage;

public class FightStage extends Stage {
	private Unit left, right;
	
	public FightStage() {
		super();
		// TODO: Beta testing stuff, delete later
		HashMap<String, Float> stats1 = new HashMap<String, Float>();
		stats1.put("Skl", 10f);
		stats1.put("Lck", 1f);
		stats1.put("HP", 15f);
		stats1.put("Str", 10f);
		stats1.put("Mag", 10f);
		stats1.put("Def", 10f);
		stats1.put("Res", 10f);
		stats1.put("Spd", 10f);
		stats1.put("Lvl", 10f);
		stats1.put("Mov", 3f);
		HashMap<String, Float> stats2 = new HashMap<String, Float>();
		stats2.put("Skl", 10f);
		stats2.put("Lck", 3f);
		stats2.put("HP", 15f);
		stats2.put("Str", 10f);
		stats2.put("Mag", 10f);
		stats2.put("Def", 10f);
		stats2.put("Res", 10f);
		stats2.put("Spd", 8f);
		stats2.put("Lvl", 10f);
		stats2.put("Mov", 3f);
		left = new Unit(stats1, null);
		left.addToInventory(Weapon.createWeapon("sord"));
		left.equip(0);
		left.setClass(new Class());
		right = new Unit(stats2, null);
		right.addToInventory(Weapon.createWeapon("lunce"));
		right.equip(0);
		right.setClass(new Class());
		calculate();
	}

	public void calculate() {
		// Determine turn order
		ArrayList<Boolean> attackOrder = new ArrayList<Boolean>();
		attackOrder.add(true);
		attackOrder.add(false);
		if (left.get("Spd") >= right.get("Spd") + 4) {
			attackOrder.add(true);
		}
		if (right.get("Spd") >= left.get("Spd") + 4) {
			attackOrder.add(false);
		}
		
		System.out.println("Starting health | Left: "+left.getHp()
				+" | Right: "+right.getHp());
		for (Boolean i : attackOrder) {
			attack(i, 1, true);
		}
		System.out.println("Ending health | Left: "+left.getHp()
				+" | Right: "+right.getHp());
	}

	public boolean attack(boolean dir, int times, boolean skills) {
		Unit a, d;
		if (dir) {
			a = left;
			d = right;
			System.out.print("Left attacks: ");
		} else {
			a = right;
			d = left;
			System.out.print("Right attacks: ");
		}
		
		ArrayList<Trigger> triggers = new ArrayList<Trigger>();
		triggers.addAll(a.getTriggers());
		triggers.addAll(d.getTriggers());
		
		for(Trigger t: triggers){
			t.attempt();
		}
		
		for (int i = 0; i < times; i++) {
			String animation = null;
			if(skills){
				boolean cancel = false;
				for(Trigger t: triggers){
					if(t.success && t.type == Trigger.Type.PRE_ATTACK){
						cancel = t.run(this, a, d) != 0;
						animation = t.getClass().getSimpleName();
					}
				}
				if(cancel) break;
			}
			if (!(RNG.get() < a.hit() - d.avoid()
					+ a.getWeapon().triMod(d.getWeapon()) * 10)) {
				// Miss
				addToAttackQueue(dir, "Miss", 0);
				if(a.getWeapon().isMagic()) a.getWeapon().use(a);
				System.out.println("Miss!");
				break;
			}
			int crit = 1;
			if (RNG.get() < a.crit() - d.dodge()) {
				crit = 3;
			}
			int damage;
			if(a.getWeapon().isMagic()){
				damage = a.get("Mag") + (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon())) 
						*(a.getWeapon().effective.contains(d.getTheClass())?3:1)
						- d.get("Res");
				//TODO Terrain modifier
			} else {
				damage = a.get("Str") + (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon())) 
						*(a.getWeapon().effective.contains(d.getTheClass())?3:1)
						- d.get("Res");
			}
			damage *= crit;
			
			for(Trigger t: triggers){
				if(t.success && t.type == Trigger.Type.DAMAGE_MOD){
					damage = t.run(damage);
				}
			}
			
			if(animation == null){
				animation = crit == 1? "Attack" : "Critical";
			}
			addToAttackQueue(dir, animation, damage);
			
			if(crit == 3) {
				System.out.print("Crit for ");
			}
			System.out.println(damage+" damage!");
			d.setHp(d.getHp()-damage);
			a.clearTempMods();
			d.clearTempMods();
			if(skills){
				for(Trigger t: triggers){
					if(t.success && t.type == Trigger.Type.POST_ATTACK){
						t.run(this, a, d);
					}
				}
			}
		}
		return d.getHp() == 0;
	}

	public void addToAttackQueue(boolean dir, String animation, int damage) {
		// TODO
	}

	@Override
	public void beginStep() {
		for(Entity e : entities) {
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
	}

	@Override
	public void onStep() {
		for(Entity e : entities) {
			e.onStep();
		}
		processAddStack();
		processRemoveStack();
	}

	@Override
	public void endStep() {
		for(Entity e : entities) {
			e.endStep();
		}
		processAddStack();
		processRemoveStack();
	}

}

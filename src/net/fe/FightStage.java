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
		stats1.put("Spd", 12f);
		stats1.put("Lvl", 1f);
		stats1.put("Mov", 3f);
		
		HashMap<String, Integer> growths1 = new HashMap<String, Integer>();
		growths1.put("HP", 70);
		growths1.put("Str", 50);
		growths1.put("Mag", 10);
		growths1.put("Skl", 70);
		growths1.put("Spd", 70);
		growths1.put("Def", 40);
		growths1.put("Res", 30);
		growths1.put("Lck", 60);
		
		HashMap<String, Float> stats2 = new HashMap<String, Float>();
		stats2.put("Skl", 10f);
		stats2.put("Lck", 3f);
		stats2.put("HP", 15f);
		stats2.put("Str", 10f);
		stats2.put("Mag", 10f);
		stats2.put("Def", 10f);
		stats2.put("Res", 10f);
		stats2.put("Spd", 8f);
		stats2.put("Lvl", 1f);
		stats2.put("Mov", 3f);
		
		HashMap<String, Integer> growths2 = new HashMap<String, Integer>();
		growths2.put("HP", 70);
		growths2.put("Str", 60);
		growths2.put("Mag", 10);
		growths2.put("Skl", 60);
		growths2.put("Spd", 50);
		growths2.put("Def", 40);
		growths2.put("Res", 30);
		growths2.put("Lck", 60);
		
		left = new Unit("Marth",Class.createClass("Assassin"), stats1, growths1);
		left.addToInventory(Weapon.createWeapon("sord"));
		left.equip(0);
		
		right = new Unit("Roy",Class.createClass(null), stats2, growths2);
		right.addToInventory(Weapon.createWeapon("lunce"));
		right.equip(0);
		
		for(int i = 0; i < 15; i++){
			left.levelUp();
			right.levelUp();
		}
		
		calculate(1);
	}
	
	public FightStage(Unit u1, Unit u2) {
		left = u1;
		right = u2;
		calculate(1);
	}

	public void calculate(int range) {
		// Determine turn order
		ArrayList<Boolean> attackOrder = new ArrayList<Boolean>();
		if(left.getWeapon()!= null && left.getWeapon().range.contains(range))
			attackOrder.add(true);
		if(right.getWeapon()!= null && right.getWeapon().range.contains(range))
			attackOrder.add(false);
		if (left.get("Spd") >= right.get("Spd") + 4 && 
				left.getWeapon()!= null && left.getWeapon().range.contains(range)) {
			attackOrder.add(true);
		}
		if (right.get("Spd") >= left.get("Spd") + 4 && 
				right.getWeapon()!= null && right.getWeapon().range.contains(range)) {
			attackOrder.add(false);
		}
		
		System.out.println("Battle!\n" + left + "\n" + right);
		System.out.println("Starting health | "+left.name+": "+left.getHp()
				+" | "+right.name+": "+right.getHp());
		for (Boolean i : attackOrder) {
			attack(i, true);
		}
		System.out.println("Ending health | "+left.name+": "+left.getHp()
				+" | "+right.name+": "+right.getHp());
	}

	public void attack(boolean dir, boolean skills) {
		Unit a, d;
		if (dir) {
			a = left;
			d = right;
		} else {
			a = right;
			d = left;
		}
		
		if(a.getHp() == 0 || d.getHp() == 0){
			return;
		}
		
		ArrayList<Trigger> aTriggers = a.getTriggers();
		ArrayList<Trigger> dTriggers = d.getTriggers();
		
		for(Trigger t: aTriggers){
			t.attempt(a);
		}
		for(Trigger t: dTriggers){
			t.attempt(a);
		}
		
		String animation = "Attack";
		if (!(RNG.get() < a.hit() - d.avoid()
				+ a.getWeapon().triMod(d.getWeapon()) * 10)) {
			// Miss
			addToAttackQueue(a, d, "Miss", 0);
			if(a.getWeapon().isMagic()) a.getWeapon().use(a);
			return;
		}
		
		if(skills){
			boolean cancel = false;
			for(Trigger t: aTriggers){
				if(t.success && t.type.contains(Trigger.Type.PRE_ATTACK)){
					cancel = t.run(this, a, d) != 0;
					animation = t.getClass().getSimpleName();
				}
			}
			if(cancel){
				for(Trigger t: aTriggers){
					t.clear();
				}
				for(Trigger t: dTriggers){
					t.clear();
				}
				return;
			}
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
		} else {
			damage = a.get("Str") + (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon())) 
					*(a.getWeapon().effective.contains(d.getTheClass())?3:1)
					- d.get("Def");
		}
		damage *= crit;
		
		for(Trigger t: dTriggers){
			if(t.success && t.type.contains(Trigger.Type.DAMAGE_MOD)){
				damage = t.run(damage);
			}
		}
		damage = Math.min(damage, d.getHp());

		
		if(crit == 3){
			animation += " Critical";
		}
		addToAttackQueue(a, d, animation, damage);
		d.setHp(d.getHp()-damage);
		a.clearTempMods();
		d.clearTempMods();
		if(skills){
			for(Trigger t: aTriggers){
				if(t.success && t.type.contains(Trigger.Type.POST_ATTACK)){
					t.run(this, a, d, damage);
				}
			}
		}
		for(Trigger t: aTriggers){
			t.clear();
		}
		for(Trigger t: dTriggers){
			t.clear();
		}
	}

	public void addToAttackQueue(Unit a, Unit d, String animation, int damage) {
		System.out.print(animation + "! ");
		System.out.println(a.name + " hit " + d.name + " for " + damage + " damage!");
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

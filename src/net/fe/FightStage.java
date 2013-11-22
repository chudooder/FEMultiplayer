package net.fe;

import java.util.ArrayList;

import chu.engine.Stage;

public class FightStage extends Stage {
	private Unit left, right;

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

		for (Boolean i : attackOrder) {
			attack(i, 1, true);
		}
	}

	public boolean attack(boolean dir, int times, boolean skills) {
		Unit a, d;
		if (dir) {
			a = left;
			d = right;
		} else {
			a = right;
			d = left;
		}
		for (int i = 0; i < times; i++) {
			String animation = null;
			if(skills){
				boolean cancel = false;
				//Run Pre Triggers (Aether, Colossus, Luna, Deadeye, Lethality)
				if(cancel) break;
			}
			if (RNG.get() >= a.hit() - d.avoid()
					+ a.getWeapon().triMod(d.getWeapon()) * 10) {
				// Miss
				addToAttackQueue(dir, "Miss", 0);
				if(a.getWeapon().isMagic()) a.getWeapon().use(a);
				break;
			}
			int crit = 1;
			if (RNG.get() >= a.crit() - d.dodge()) {
				crit = 3;
			}
			int damage;
			if(a.getWeapon().isMagic()){
				damage = a.get("Mag") + (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon())) 
						*(a.getWeapon().effective.contains(d.getClazz())?3:1)
						- d.get("Res");
				//TODO Terrain modifier
			} else {
				damage = a.get("Str") + (a.getWeapon().mt + a.getWeapon().triMod(d.getWeapon())) 
						*(a.getWeapon().effective.contains(d.getClazz())?3:1)
						- d.get("Res");
			}
			damage *= crit;
			
			//Run Passive Triggers (Great Shield, Miracle)
			
			if(animation == null){
				animation = crit == 1? "Attack" : "Critical";
			}
			addToAttackQueue(dir, animation, damage);
			if(skills){
				//Run Post Triggers (Sol, Nosferatu, Astra, Brave, Stun)
			}
			a.clearTempMods();
			d.clearTempMods();
		}
		return d.getHp() == 0;
	}

	public void addToAttackQueue(boolean dir, String animation, int damage) {
		// TODO
	}

	@Override
	public void beginStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub

	}

}

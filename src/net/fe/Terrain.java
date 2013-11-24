package net.fe;

public enum Terrain {
	PLAIN(1), 
	FOREST(2), 
	FLOOR(1), 
	PILLAR(2), 
	MOUNTAIN(4), 
	PEAK(127), 
	GATE(2), 
	FORT(2), 
	SEA(127), 
	RIVER(127), 
	DESERT(2),
	WALL(127);

	private int baseMoveCost;

	Terrain(int baseMoveCost) {
		this.baseMoveCost = baseMoveCost;
	}

	/**
	 * Applies stat modifiers to the target unit
	 * 
	 * @param u
	 */
	public void applyBonuses(Unit u) {
		if (this == FOREST || this == PILLAR) {
			u.setTempMod("Def", 1);
			u.setTempMod("Avo", 20);
		} else if (this == MOUNTAIN) {
			u.setTempMod("Def", 2);
			u.setTempMod("Avo", 30);
		} else if (this == PEAK) {
			u.setTempMod("Def", 2);
			u.setTempMod("Avo", 40);
		} else if (this == FORT) {
			u.setTempMod("Def", 1);
			u.setTempMod("Avo", 15);
			// TODO: Forts have variable bonuses? End of turn HP regen?
		} else if (this == SEA) {
			u.setTempMod("Avo", 10);
		} else if (this == DESERT) {
			u.setTempMod("Avo", 5);
		}
	}

	public int getMoveCost(Class c) {
		if (c == null)
			return baseMoveCost;
		String name = c.name;
		if (this == RIVER) {
			if (name.equals("Berserker") || name.equals("Chameleon")
					|| name.equals("Pirate")) {
				return 2;
			}
			else if (name.equals("Sniper")) {
				return 4;
			}
			else if (name.equals("Lord") || name.equals("Hero")
					|| name.equals("Swordmaster") || name.equals("Thief")) {
				return 5;
			}
		}
		
		else if (this == SEA) {
			if (name.equals("Berserker") || name.equals("Chameleon")
					|| name.equals("Pirate")) {
				return 2;
			}
		}
		
		else if (this == FOREST || this == PILLAR) {
			if (name.equals("Archer") || name.equals("Ballistician")
					|| name.equals("Cavalier") || name.equals("Horseman")
					|| name.equals("Paladin")) {
				return 3;
			}
		}
		
		else if (this == DESERT) {
			if (name.equals("Archer") || name.equals("Ballistician")
					|| name.equals("General") || name.equals("Paladin")) {
				return 3;
			}
			else if (name.equals("Cavalier")) {
				return 4;
			}
			else if (name.equals("Horseman")) {
				return 5;
			}
		}
		
		else if (this == MOUNTAIN) {
			if (name.equals("Berserker") || name.equals("Chameleon")
					|| name.equals("Fighter") || name.equals("Hero")
					|| name.equals("Hunter") || name.equals("Sniper")
					|| name.equals("Swordmaster")) {
				return 3;
			}
			else if(name.equals("Horseman") || name.equals("Paladin")) {
				return 6;
			}
		}
		
		return baseMoveCost;
	}
}

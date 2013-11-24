package net.fe;

public enum Terrain {
	PLAIN,
	FOREST,
	FLOOR,
	PILLAR,
	MOUNTAIN,
	PEAK,
	GATE,
	FORT,
	SEA,
	DESERT;
	
	/**
	 * Applies stat modifiers to the target unit
	 * @param u
	 */
	public void applyBonuses(Unit u) {
		if(this == FOREST || this == PILLAR) {
			u.setTempMod("Def", 1);
			u.setTempMod("Avo", 20);
		}
		else if(this == MOUNTAIN) {
			u.setTempMod("Def", 2);
			u.setTempMod("Avo", 30);
		}
		else if(this == PEAK) {
			u.setTempMod("Def", 2);
			u.setTempMod("Avo", 40);
		}
		else if(this == FORT) {
			u.setTempMod("Def", 1);
			u.setTempMod("Avo", 15);
			//TODO: Forts have variable bonuses? End of turn HP regen?
		}
		else if(this == SEA) {
			u.setTempMod("Avo", 10);
		}
		else if(this == DESERT) {
			u.setTempMod("Avo", 5);
		}
	}
}
